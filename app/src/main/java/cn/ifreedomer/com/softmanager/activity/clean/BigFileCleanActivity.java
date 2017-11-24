package cn.ifreedomer.com.softmanager.activity.clean;

import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.ifreedomer.com.softmanager.R;
import cn.ifreedomer.com.softmanager.activity.BaseActivity;
import cn.ifreedomer.com.softmanager.adapter.BigFileAdapter;
import cn.ifreedomer.com.softmanager.bean.FileInfo;
import cn.ifreedomer.com.softmanager.service.FileScanService;
import cn.ifreedomer.com.softmanager.util.FileUtil;
import cn.ifreedomer.com.softmanager.util.ToolbarUtil;
import cn.ifreedomer.com.softmanager.widget.ArcProgress;
import cn.ifreedomer.com.softmanager.widget.BigFileHeadView;

public class BigFileCleanActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = BigFileCleanActivity.class.getSimpleName();
    @InjectView(R.id.toolbar)
    Toolbar mToolbar;

    @InjectView(R.id.rv)
    RecyclerView mRv;
    @InjectView(R.id.btn_clean)
    Button mBtnClean;
    ArcProgress mArcProgress;
    private List<FileInfo> mFileInfoList = new ArrayList<>();
    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;
    private BigFileAdapter mBigFileAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_file_clean);
        ButterKnife.inject(this);
        initTitleBar();
        initRv();
        initListener();
        startScan();

    }

    private void initListener() {
        mBtnClean.setOnClickListener(this);
    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    private void initRv() {


        mRv.setLayoutManager(new LinearLayoutManager(this));
        mBigFileAdapter = new BigFileAdapter(this, R.layout.item_big_file, mFileInfoList);

        //add headview
        BigFileHeadView headerView = new BigFileHeadView(this);
        mArcProgress = (ArcProgress) headerView.findViewById(R.id.arc_progress);
        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(mBigFileAdapter);
        mHeaderAndFooterWrapper.addHeaderView(headerView);
        mRv.setAdapter(mHeaderAndFooterWrapper);

        mHeaderAndFooterWrapper.notifyDataSetChanged();
    }

    private void initTitleBar() {
        ToolbarUtil.setTitleBarWhiteBack(this, mToolbar);

        getSupportActionBar().setTitle(getString(R.string.big_file_clean));
    }

    private void startScan() {
        mArcProgress.setProgress(0);
        mArcProgress.setProgress(1);
        asyncTask.execute();
    }


    private AsyncTask<String, Integer, List<FileInfo>> asyncTask = new AsyncTask<String, Integer, List<FileInfo>>() {
        @Override
        protected List<FileInfo> doInBackground(String... params) {


            return FileUtil.scanSDCard4BigFile(Environment.getExternalStorageDirectory().getPath(), new FileScanService.ScanListener() {
                @Override
                public void onScanStart() {
                    Log.e(TAG, "onScanStart: ");

                }

                @Override
                public void onScanProcess(float process, FileInfo fileInfo) {
                    publishProgress((int) process);
                    mFileInfoList.add(fileInfo);

                }



                @Override
                public void onScanFinish(float garbageSize, List<FileInfo> garbageList) {
                    publishProgress(100);
                }
            });

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            mArcProgress.setProgress(values[0]);

            if (values[0] == 100) {
                mArcProgress.setBottomText(getString(R.string.scan_finish));
                mBtnClean.setVisibility(View.VISIBLE);
            }
            mHeaderAndFooterWrapper.notifyDataSetChanged();

        }

        @Override
        protected void onPostExecute(List<FileInfo> fileInfos) {
//            mFileInfoList.clear();
//            mFileInfoList.addAll(fileInfos);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_clean:
                mBigFileAdapter.removeCheckedItems();
                mHeaderAndFooterWrapper.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }
}
