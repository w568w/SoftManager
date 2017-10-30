package cn.ifreedomer.com.softmanager.activity.clean;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.ifreedomer.com.softmanager.R;
import cn.ifreedomer.com.softmanager.activity.BaseActivity;
import cn.ifreedomer.com.softmanager.adapter.QQCleanAdapter;
import cn.ifreedomer.com.softmanager.bean.FileInfo;
import cn.ifreedomer.com.softmanager.util.DataTypeUtil;
import cn.ifreedomer.com.softmanager.util.FileUtil;
import cn.ifreedomer.com.softmanager.util.ToolbarUtil;

public class QQCleanActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = QQCleanActivity.class.getSimpleName();
    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.rel_top)
    RelativeLayout mRelTop;
    @InjectView(R.id.expand_listview)
    ExpandableListView mExpandListview;
    @InjectView(R.id.tv_scan_total)
    TextView mTvScanTotal;
    @InjectView(R.id.btn_clean)
    Button mBtnClean;
    private String[] mTitles = null;
    private List<FileInfo> fileInfoList = new ArrayList<>();
    private List<List<FileInfo>> fileInfoGroupList = new ArrayList<>();
    private QQCleanAdapter qqCleanAdapter;
    public static final String QQ_PATH = Environment.getExternalStorageDirectory().getPath() + "/tencent/";
    public static final String QQ_FILE_RECV = QQ_PATH + "QQfile_recv";
    public static final String QQ_SHORT_VIDEO = QQ_PATH + "MobileQQ/shortvideo/";

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
        mTitles = new String[]{getString(R.string.recv_files), getString(R.string.shortvideo)};
    }

    private void initExpandbleListView() {
        qqCleanAdapter = new QQCleanAdapter(this, mTitles, fileInfoGroupList);
        mExpandListview.setAdapter(qqCleanAdapter);
        mExpandListview.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                Log.e(TAG, "onGroupClick: " + "group position=" + groupPosition);
                return false;
            }
        });


    }

    public void startScan() {
        asyncTask.execute();
    }

    private void initTitleBar() {
        ToolbarUtil.setTitleBarWhiteBack(this, mToolbar);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                QQCleanActivity.this.finish();
            }
        });
        getSupportActionBar().setTitle(getString(R.string.qq_clean));
    }

    private AsyncTask<String, Integer, List<FileInfo>> asyncTask = new AsyncTask<String, Integer, List<FileInfo>>() {
        @Override
        protected List<FileInfo> doInBackground(String... params) {


            List<FileInfo> receiveFiles = new ArrayList<>();
            FileUtil.getFolderFiles(QQ_FILE_RECV, receiveFiles);
            fileInfoGroupList.add(receiveFiles);


            List<FileInfo> shortVideoFiles = new ArrayList<>();
            FileUtil.getFolderFiles(QQ_SHORT_VIDEO, shortVideoFiles);
            fileInfoGroupList.add(shortVideoFiles);


            return null;
        }

        @Override
        protected void onPostExecute(List<FileInfo> fileInfoList) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    float calculateTotalSize = calculateTotalSize();
                    mTvScanTotal.setText(DataTypeUtil.getTwoFloat(calculateTotalSize / FileUtil.MB) + "");
                    qqCleanAdapter.notifyDataSetChanged();

                }
            });

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
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
                qqCleanAdapter.removeCheckedItems();
                qqCleanAdapter.notifyDataSetChanged();
                float calculateTotalSize = calculateTotalSize();
                mTvScanTotal.setText(DataTypeUtil.getTwoFloat(calculateTotalSize / FileUtil.MB) + "");
                break;
        }
    }
}
