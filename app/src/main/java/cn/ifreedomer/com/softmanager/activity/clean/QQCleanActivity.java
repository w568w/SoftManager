package cn.ifreedomer.com.softmanager.activity.clean;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ExpandableListView;
import android.widget.RelativeLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.ifreedomer.com.softmanager.R;
import cn.ifreedomer.com.softmanager.activity.BaseActivity;
import cn.ifreedomer.com.softmanager.adapter.QQCleanAdapter;
import cn.ifreedomer.com.softmanager.bean.FileInfo;
import cn.ifreedomer.com.softmanager.util.FileUtil;
import cn.ifreedomer.com.softmanager.util.LogUtil;
import cn.ifreedomer.com.softmanager.util.ToolbarUtil;

public class QQCleanActivity extends BaseActivity {
    private static final String TAG = QQCleanActivity.class.getSimpleName();
    @InjectView(R.id.toolbar)
    Toolbar mToolbar;
    @InjectView(R.id.rel_top)
    RelativeLayout mRelTop;
    @InjectView(R.id.expand_listview)
    ExpandableListView mExpandListview;
    private String[] titles = null;
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
        initTitleBar();
        initExpandbleListView();

        startScan();
    }

    private void initData() {
        titles = new String[]{getString(R.string.garbage_file), getString(R.string.shortvideo)};
    }

    private void initExpandbleListView() {
        qqCleanAdapter = new QQCleanAdapter(this, titles, fileInfoGroupList);
        mExpandListview.setAdapter(qqCleanAdapter);
        mExpandListview.setGroupIndicator(this.getResources().getDrawable(R.drawable.expandablelistviewselector));

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
            List<FileInfo> shortVideoFiles = new ArrayList<>();
            FileUtil.getFolderFiles(Environment.getExternalStorageDirectory().getPath() + "/tencent/MobileQQ/shortvideo", shortVideoFiles);
            fileInfoGroupList.add(shortVideoFiles);


            List<FileInfo> receiveFiles = new ArrayList<>();
            FileUtil.getFolderFiles(QQ_FILE_RECV, receiveFiles);
            LogUtil.e(TAG, shortVideoFiles.toString());
            fileInfoGroupList.add(receiveFiles);


            return null;
        }

        @Override
        protected void onPostExecute(List<FileInfo> fileInfoList) {
            qqCleanAdapter.notifyDataSetChanged();

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }
    };


}
