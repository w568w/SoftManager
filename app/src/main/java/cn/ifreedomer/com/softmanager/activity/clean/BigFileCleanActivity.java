package cn.ifreedomer.com.softmanager.activity.clean;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.ifreedomer.com.softmanager.R;
import cn.ifreedomer.com.softmanager.activity.BaseActivity;
import cn.ifreedomer.com.softmanager.adapter.BigFileAdapter;
import cn.ifreedomer.com.softmanager.bean.FileInfo;
import cn.ifreedomer.com.softmanager.manager.GlobalDataManager;
import cn.ifreedomer.com.softmanager.util.DataTypeUtil;
import cn.ifreedomer.com.softmanager.util.FileUtil;
import cn.ifreedomer.com.softmanager.util.MemoryUtils;
import cn.ifreedomer.com.softmanager.util.SPUtil;
import cn.ifreedomer.com.softmanager.util.ToolbarUtil;
import cn.ifreedomer.com.softmanager.widget.BigFileDialog;
import cn.ifreedomer.com.softmanager.widget.NumChangeHeadView;

public class BigFileCleanActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = BigFileCleanActivity.class.getSimpleName();
    @InjectView(R.id.toolbar)
    Toolbar mToolbar;

    @InjectView(R.id.rv)
    RecyclerView mRv;
    @InjectView(R.id.btn_clean)
    Button mBtnClean;
    private List<FileInfo> mFileInfoList = new ArrayList<>();
    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;
    private BigFileAdapter mBigFileAdapter;
    private NumChangeHeadView mHeaderView;
    private static final int MSG_START_SCAN = 0;
    private static final int MSG_SCANNING = 1;
    private static final int MSG_FINISH_SCAN = 2;
    private long mBigTotalSize = 0;
    private long mScanTotalSize = 0;
    private int mScanMinSize = 10;
    @SuppressLint("HandlerLeak")
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_START_SCAN:
                    mHeaderView.setScanningText(getString(R.string.begin_scan));
                    mBtnClean.setVisibility(View.GONE);

                    break;
                case MSG_SCANNING:
                    mHeaderView.setScanTotal(DataTypeUtil.getTextBySize(mBigTotalSize));
                    mHeaderView.setScanningText(msg.arg1 + "%   " + msg.obj);
                    break;
                case MSG_FINISH_SCAN:
                    mHeaderView.setScanningText(getString(R.string.scan_finish));
                    mBtnClean.setVisibility(View.VISIBLE);
                    break;
            }
        }
    };
    private long mAvailableExternalMemorySize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_big_file_clean);
        ButterKnife.inject(this);
        initTitleBar();
        initRv();
        initListener();
        initLogic();
        startScan();


    }

    private void initLogic() {
        mScanMinSize = (int) SPUtil.get(this, "scanMinSize", mScanMinSize);

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
        mHeaderView = new NumChangeHeadView(this);
        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(mBigFileAdapter);
        mHeaderAndFooterWrapper.addHeaderView(mHeaderView);
        mRv.setAdapter(mHeaderAndFooterWrapper);

        mHeaderAndFooterWrapper.notifyDataSetChanged();
    }

    private void initTitleBar() {
        ToolbarUtil.setTitleBarWhiteBack(this, mToolbar);
        getSupportActionBar().setTitle(getString(R.string.big_file_clean));
    }


    @Override
    public boolean onPrepareOptionsMenu(final Menu menu) {
        mToolbar.inflateMenu(R.menu.bigfile_menu);
        mToolbar.getMenu().findItem(R.id.m_item_edit).setOnMenuItemClickListener(item -> {
            showEtDialog();
            return false;
        });
        return super.onPrepareOptionsMenu(menu);
    }

    private void startScan() {
        mFileInfoList.clear();
        mBigTotalSize = 0;
        GlobalDataManager.getInstance().getThreadPool().execute(() -> {
            mAvailableExternalMemorySize = MemoryUtils.getTotalExternalMemorySize() - MemoryUtils.getAvailableExternalMemorySize();
            FileUtil.scanFile(Environment.getExternalStorageDirectory().getPath(), mScanListener);
        });

    }


    private FileUtil.ScanListener mScanListener = new FileUtil.ScanListener() {
        @Override
        public void onScanStart() {
            mHandler.sendEmptyMessage(MSG_START_SCAN);
        }

        @Override
        public void onScanProcess(File file) {

            mScanTotalSize = mScanTotalSize + file.length();
            long percent = mScanTotalSize * 100 / (mAvailableExternalMemorySize);
            if (file.length() > mScanMinSize * FileUtil.MB) {
                FileInfo fileInfo = new FileInfo();
                fileInfo.setName(file.getName());
                fileInfo.setChecked(false);
                fileInfo.setPath(file.getPath());
                fileInfo.setSize(file.length());
                fileInfo.setType(FileUtil.getMimeType(file.getPath()));
                mFileInfoList.add(fileInfo);
                mBigTotalSize = mBigTotalSize + file.length();
            }

            Message message = new Message();
            message.what = MSG_SCANNING;
            message.arg1 = (int) percent;
            message.obj = file.getName();
            mHandler.sendMessage(message);
        }

        @Override
        public void onScanFinish() {
            mHandler.sendEmptyMessage(MSG_FINISH_SCAN);

        }
    };



    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_clean:
                mBigFileAdapter.removeCheckedItems();
                mBigTotalSize = 0;
                for (int i = 0; i < mFileInfoList.size(); i++) {
                    mBigTotalSize = mBigTotalSize + (long) mFileInfoList.get(i).getSize();
                }
                mHeaderView.setScanTotal(DataTypeUtil.getTextBySize(mBigTotalSize) );
                mHeaderAndFooterWrapper.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }


    public void showEtDialog() {

        BigFileDialog etDialog = new BigFileDialog(this);
        etDialog.show();
        etDialog.setData(mScanMinSize);
        etDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        etDialog.setOnEditContent(content -> {
            mScanMinSize = Integer.parseInt(content);
            SPUtil.put(BigFileCleanActivity.this, "scanMinSize", mScanMinSize);
        });
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}
