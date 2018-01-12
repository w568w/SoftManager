package cn.ifreedomer.com.softmanager.activity.clean;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ProgressBar;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.ifreedomer.com.softmanager.R;
import cn.ifreedomer.com.softmanager.activity.BaseActivity;
import cn.ifreedomer.com.softmanager.adapter.QQCleanAdapter;
import cn.ifreedomer.com.softmanager.bean.FileInfo;
import cn.ifreedomer.com.softmanager.bean.clean.QQGroupTitle;
import cn.ifreedomer.com.softmanager.util.DataTypeUtil;
import cn.ifreedomer.com.softmanager.util.FileUtil;
import cn.ifreedomer.com.softmanager.util.ToolbarUtil;
import cn.ifreedomer.com.softmanager.widget.NumChangeHeadView;

public class QQCleanActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = QQCleanActivity.class.getSimpleName();
    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.expand_listview)
    ExpandableListView mExpandListview;
    @InjectView(R.id.btn_clean)
    Button mBtnClean;
    @InjectView(R.id.pb)
    ProgressBar pb;
    private List<QQGroupTitle> mTitleList = new ArrayList<>();
    private List<FileInfo> fileInfoList = new ArrayList<>();
    private List<List<FileInfo>> fileInfoGroupList = new ArrayList<>();
    private QQCleanAdapter qqCleanAdapter;
    public static final String QQ_PATH = Environment.getExternalStorageDirectory().getPath() + "/tencent/";
    public static final String QQ_FILE_RECV = QQ_PATH + "QQfile_recv";
    public static final String QQ_THUMBNAILS = QQ_FILE_RECV + File.separator + ".thumbnails";
    public static final String QQ_SHORT_VIDEO = QQ_PATH + "MobileQQ/shortvideo/";
    public static final String QQ_USER_HEAD = QQ_PATH + "MobileQQ/head/_hd";
    private static final String QQ_CHAT_DISK_CACHE = QQ_PATH + "MobileQQ/diskcache";
    public static final int MSG_CLEAN_FINISH = 0;
    public static final int MSG_REFRESH = 1;
    List<FileInfo> mReceiveFiles = new ArrayList<>();
    List<FileInfo> mShortVideoFiles = new ArrayList<>();
    List<FileInfo> mHeadIconFiles = new ArrayList<>();
    List<FileInfo> mThumbFiles = new ArrayList<>();
    List<FileInfo> mChatPhotoFiles = new ArrayList<>();

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_CLEAN_FINISH:
                    pb.setVisibility(View.GONE);
                    qqCleanAdapter.notifyDataSetChanged();
                    float calculateTotalSize = calculateTotalSize();
                    mNumChangeHeadView.setScanTotal(DataTypeUtil.getTextBySize(calculateTotalSize));
                    break;
                case MSG_REFRESH:
                    break;
            }
        }
    };
    private NumChangeHeadView mNumChangeHeadView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qqclean);
        ButterKnife.inject(this);
        initData();
        initListener();
        initTitleBar();
        initExpandbleListView();
        startScan();
    }

    private void initListener() {
        mBtnClean.setOnClickListener(this);
    }

    private void initData() {
        QQGroupTitle receiveFileTitle = new QQGroupTitle(getString(R.string.recv_files), QQGroupTitle.TYPE_RECEIVE_FILES);
        mTitleList.add(receiveFileTitle);

        QQGroupTitle shortVideoTitle = new QQGroupTitle(getString(R.string.shortvideo), QQGroupTitle.TYPE_SHORT_VIDEOS);
        mTitleList.add(shortVideoTitle);

        QQGroupTitle userHeadTitle = new QQGroupTitle(getString(R.string.user_head), QQGroupTitle.TYPE_USER_HEAD);
        mTitleList.add(userHeadTitle);


        QQGroupTitle thumbnailsTitle = new QQGroupTitle(getString(R.string.thumbnails), QQGroupTitle.TYPE_THUMBNAILS);
        mTitleList.add(thumbnailsTitle);

        QQGroupTitle chatPhotoTitle = new QQGroupTitle(getString(R.string.chat_title), QQGroupTitle.TYPE_CHAT_PHOTO);
        mTitleList.add(chatPhotoTitle);


//        mTitles = new String[]{getString(R.string.recv_files), getString(R.string.shortvideo)};
    }

    private void initExpandbleListView() {
        mNumChangeHeadView = new NumChangeHeadView(this);
        qqCleanAdapter = new QQCleanAdapter(this, mTitleList, fileInfoGroupList);
        mExpandListview.addHeaderView(mNumChangeHeadView);
        mExpandListview.setAdapter(qqCleanAdapter);
        mExpandListview.setOnGroupClickListener((parent, v, groupPosition, id) -> {
            Log.e(TAG, "onGroupClick: " + "group position=" + groupPosition);
            return false;
        });


    }

    public void startScan() {
        pb.setVisibility(View.VISIBLE);
        asyncTask.execute();
    }

    private void initTitleBar() {
        ToolbarUtil.setTitleBarWhiteBack(this, mToolbar);
        getSupportActionBar().setTitle(getString(R.string.qq_clean));
    }


    FileUtil.ScanListener mShortScanListener = new FileUtil.ScanListener() {
        @Override
        public void onScanStart() {
            mShortVideoFiles.clear();
        }

        @Override
        public void onScanProcess(File file) {
            FileInfo fileInfo = FileInfo.getFileInfo(file);
            mShortVideoFiles.add(fileInfo);
        }

        @Override
        public void onScanFinish() {
            fileInfoGroupList.add(mShortVideoFiles);
        }
    };


    FileUtil.ScanListener mReceiverScanListener = new FileUtil.ScanListener() {
        @Override
        public void onScanStart() {

        }

        @Override
        public void onScanProcess(File file) {
            FileInfo fileInfo = FileInfo.getFileInfo(file);
            mReceiveFiles.add(fileInfo);
        }

        @Override
        public void onScanFinish() {

        }
    };

    FileUtil.ScanListener mQQUserHeadScanListener = new FileUtil.ScanListener() {
        @Override
        public void onScanStart() {

        }

        @Override
        public void onScanProcess(File file) {
            FileInfo fileInfo = FileInfo.getFileInfo(file);
            mHeadIconFiles.add(fileInfo);
            fileInfo.setType("image/jpeg");
        }

        @Override
        public void onScanFinish() {
            fileInfoGroupList.add(mHeadIconFiles);
        }
    };

    FileUtil.ScanListener mQQThumbScanListener = new FileUtil.ScanListener() {
        @Override
        public void onScanStart() {

        }

        @Override
        public void onScanProcess(File file) {
            FileInfo fileInfo = FileInfo.getFileInfo(file);
            mThumbFiles.add(fileInfo);
            fileInfo.setType("image/jpeg");

        }

        @Override
        public void onScanFinish() {
            fileInfoGroupList.add(mThumbFiles);
        }
    };


    FileUtil.ScanListener mChatPhotoScanListener = new FileUtil.ScanListener() {
        @Override
        public void onScanStart() {

        }

        @Override
        public void onScanProcess(File file) {
            FileInfo fileInfo = FileInfo.getFileInfo(file);
            mChatPhotoFiles.add(fileInfo);
            fileInfo.setType("image/jpeg");
        }

        @Override
        public void onScanFinish() {
            fileInfoGroupList.add(mChatPhotoFiles);
        }
    };

    @SuppressLint("StaticFieldLeak")
    private AsyncTask<String, Integer, List<FileInfo>> asyncTask = new AsyncTask<String, Integer, List<FileInfo>>() {
        @Override
        protected List<FileInfo> doInBackground(String... params) {
//
            FileUtil.scanFile(QQ_FILE_RECV, mReceiverScanListener);
            publishProgress(20);

            FileUtil.scanFile(QQ_SHORT_VIDEO, mShortScanListener);
            publishProgress(40);

            FileUtil.scanFile(QQ_USER_HEAD, mQQUserHeadScanListener);
            publishProgress(60);

            FileUtil.scanFile(QQ_THUMBNAILS, mQQThumbScanListener);
            publishProgress(80);

            FileUtil.scanFile(QQ_CHAT_DISK_CACHE, mChatPhotoScanListener);
            publishProgress(100);

//            FileUtil.getFolderFiles(QQ_FILE_RECV, mReceiveFiles);
//            fileInfoGroupList.add(mReceiveFiles);
//            publishProgress(20);
//
//            List<FileInfo> shortVideoFiles = new ArrayList<>();
//            FileUtil.getFolderFiles(QQ_SHORT_VIDEO, shortVideoFiles);
//            fileInfoGroupList.add(shortVideoFiles);
//
//            publishProgress(30);
//
//            List<FileInfo> userHeadFiles = new ArrayList<>();
//            FileUtil.getFolderFiles(QQ_USER_HEAD, userHeadFiles);
//            for (int i = 0; i < userHeadFiles.size(); i++) {
//                FileInfo fileInfo = userHeadFiles.get(i);
//                fileInfo.setType("image/jpeg");
//            }
//            fileInfoGroupList.add(userHeadFiles);
//
//            publishProgress(40);
//
//            List<FileInfo> thumbnails = new ArrayList<>();
//            FileUtil.getFolderFiles(QQ_THUMBNAILS, thumbnails);
//            for (int i = 0; i < thumbnails.size(); i++) {
//                FileInfo fileInfo = thumbnails.get(i);
//                fileInfo.setType("image/jpeg");
//            }
//            fileInfoGroupList.add(thumbnails);
//            publishProgress(90);
//
//
//            List<FileInfo> chatPhotos = new ArrayList<>();
//            FileUtil.getFolderFiles(QQ_CHAT_DISK_CACHE, chatPhotos);
//            for (int i = 0; i < chatPhotos.size(); i++) {
//                FileInfo fileInfo = chatPhotos.get(i);
//                fileInfo.setType("image/jpeg");
//            }
//            fileInfoGroupList.add(chatPhotos);
//
//            publishProgress(100);

            return null;
        }

        @Override
        protected void onPostExecute(List<FileInfo> fileInfoList) {
            runOnUiThread(() -> {
                pb.setVisibility(View.GONE);
                float calculateTotalSize = calculateTotalSize();
                mNumChangeHeadView.setScanTotal(DataTypeUtil.getTextBySize(calculateTotalSize));
                qqCleanAdapter.notifyDataSetChanged();
                for (int i = 0; i < fileInfoGroupList.size(); i++) {
                    mExpandListview.expandGroup(i);
                }
            });

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            float calculateTotalSize = calculateTotalSize();
            mNumChangeHeadView.setScanTotal(DataTypeUtil.getTextBySize(calculateTotalSize));
            mNumChangeHeadView.setScanningText("已经扫描:" + values[0] + "%");
            qqCleanAdapter.notifyDataSetChanged();
        }
    };


    public float calculateTotalSize() {
        float totalScanSize = 0;
        for (List<FileInfo> fileInfoList : fileInfoGroupList) {
            for (FileInfo fileInfo : fileInfoList) {
                totalScanSize = totalScanSize + fileInfo.getSize();
            }
        }
        return totalScanSize;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_clean:
                pb.setVisibility(View.VISIBLE);
                qqCleanAdapter.removeCheckedItems(mHandler);
                break;
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (asyncTask != null && (asyncTask.getStatus() == AsyncTask.Status.RUNNING)) {
            asyncTask.cancel(true);
        }
    }
}
