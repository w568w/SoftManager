package cn.ifreedomer.com.softmanager.activity.clean;

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
import cn.ifreedomer.com.softmanager.adapter.GarbageCleanAdapter;
import cn.ifreedomer.com.softmanager.bean.EmptyFolder;
import cn.ifreedomer.com.softmanager.bean.GarbageInfo;
import cn.ifreedomer.com.softmanager.bean.clean.ClearItem;
import cn.ifreedomer.com.softmanager.bean.clean.GarbageGroupTitle;
import cn.ifreedomer.com.softmanager.db.DBAppAdUtils;
import cn.ifreedomer.com.softmanager.manager.GlobalDataManager;
import cn.ifreedomer.com.softmanager.manager.PackageInfoManager;
import cn.ifreedomer.com.softmanager.manager.PermissionManager;
import cn.ifreedomer.com.softmanager.model.AppInfo;
import cn.ifreedomer.com.softmanager.util.DBUtil;
import cn.ifreedomer.com.softmanager.util.FileUtil;
import cn.ifreedomer.com.softmanager.util.LogUtil;
import cn.ifreedomer.com.softmanager.util.ToolbarUtil;
import cn.ifreedomer.com.softmanager.widget.NumChangeHeadView;

public class GarbageActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = GarbageActivity.class.getSimpleName();
    private static final int MSG_UPDATE_TOTAL_SIZE = 1;
    private static final int MSG_UPDATE_UI = 2;
    private static final int MSG_CLEAN_SUCCESS = 3;
    public static final int MSG_CLEAN_PROCESSING = 4;
    public static final int MSG_LOAD_GARBAGE_FINISH = 5;
    @InjectView(R.id.toolbar)
    Toolbar mToolbar;

    @InjectView(R.id.expand_listview)
    ExpandableListView mExpandListview;
    @InjectView(R.id.btn_clean)
    Button mBtnClean;
    @InjectView(R.id.pb)
    ProgressBar mProgressBar;
    private float mTotalSize = 0;
    private List<List<GarbageInfo>> mGarbageInfoGroupList = new ArrayList<>();
    private List<GarbageGroupTitle> mTitleList = new ArrayList<>();
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_UPDATE_TOTAL_SIZE:
                    mTotalSize = mTotalSize + (Float) msg.obj;
                    mGarbageHeadView.setScanTotal(mTotalSize);
                    LogUtil.e(TAG, "mTotalSize:" + mTotalSize + "");
                    break;
                case MSG_UPDATE_UI:
                    break;
                case MSG_LOAD_GARBAGE_FINISH:
                    mGarbageHeadView.setScanningText(getString(R.string.app_scaned));
                    mProgressBar.setVisibility(View.GONE);
                    mGarbageCleanAdapter.notifyDataSetChanged();
                    for (int i = 0; i < mGarbageInfoGroupList.size(); i++) {
                        mExpandListview.expandGroup(i);
                    }
                    break;
                case MSG_CLEAN_SUCCESS:
                    //清理完先设置为0，后面扫描更新
//                    mGarbageHeadView.setScanTotal(0);
                    mGarbageCleanAdapter.notifyDataSetChanged();
                    mGarbageHeadView.setScanningText(getString(R.string.clean_finish));
                    mProgressBar.setVisibility(View.GONE);
                    break;
                case MSG_CLEAN_PROCESSING:
                    mGarbageHeadView.setScanningText((String) msg.obj);
                    break;
                default:
                    break;
            }
        }
    };
    private GarbageCleanAdapter mGarbageCleanAdapter;
    private NumChangeHeadView mGarbageHeadView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garbage);
        ButterKnife.inject(this);
        initTitleBar();
        initExpandbleListView();
        initListener();
        initGroupTitle();

        //copy db file
        mProgressBar.setVisibility(View.VISIBLE);
        GlobalDataManager.getInstance().getThreadPool().execute(() -> {
            LogUtil.e(TAG, "copy db");
            DBUtil.copyDB(GarbageActivity.this);
            LogUtil.e(TAG, "scanGarbage");
            scanGarbage();
            mHandler.sendEmptyMessage(MSG_LOAD_GARBAGE_FINISH);
        });


    }

    private void initGroupTitle() {
        GarbageGroupTitle garbageGroupTitle = new GarbageGroupTitle(getString(R.string.app_cache), GarbageInfo.TYPE_APP_CACHE);
        mTitleList.add(garbageGroupTitle);
        //填充group信息
        garbageGroupTitle = new GarbageGroupTitle(getString(R.string.empty_file), GarbageInfo.TYPE_EMPTY_FILE);
        mTitleList.add(garbageGroupTitle);

        mTitleList.add(new GarbageGroupTitle(getString(R.string.ad_garbage), GarbageInfo.TYPE_AD_GARBAGE));


    }

    private void initListener() {
        mBtnClean.setOnClickListener(this);
    }


    private void initExpandbleListView() {

//        mTitleList = new String[]{getString(R.string.app_cache), getString(R.string.empty_file), getString(R.string.ad_garbage)};
        mGarbageCleanAdapter = new GarbageCleanAdapter(this, mTitleList, mGarbageInfoGroupList);
        mGarbageHeadView = new NumChangeHeadView(this);
        mExpandListview.addHeaderView(mGarbageHeadView);
        mExpandListview.setAdapter(mGarbageCleanAdapter);
        mExpandListview.setOnGroupClickListener((parent, v, groupPosition, id) -> {
            Log.e(TAG, "onGroupClick: " + "group position=" + groupPosition);
            return false;
        });


    }


    private void scanGarbage() {

        getTotalAppCacheSize();
        getEmptyFileSize();
        getADGarbageSize();

        getUninstallCacheSize();
        getSystemGabargeSize();

    }


    private void getEmptyFileSize() {
//        LogUtil.e(TAG, "getEmptyFileSize");

        EmptyFolder emptyFile = FileUtil.getEmptyFile();

        //填充子信息
        List<GarbageInfo> emptyList = new ArrayList<>();
        GarbageInfo<EmptyFolder> garbageInfo = new GarbageInfo<>();
        if (emptyFile.getPathList() != null && emptyFile.getPathList().size() != 0) {
            garbageInfo.setData(emptyFile);
            garbageInfo.setType(GarbageInfo.TYPE_EMPTY_FILE);
            emptyList.add(garbageInfo);
            sendGarbageMsg(garbageInfo.getData().getTotalSize());
        }
        mGarbageInfoGroupList.add(emptyList);
        LogUtil.e(TAG, "getEmptyFileSize mGarbageInfoGroupList SIZE = " + mGarbageInfoGroupList.size());

        mHandler.sendEmptyMessage(MSG_UPDATE_UI);
    }

    private void getADGarbageSize() {
        LogUtil.e(TAG, "getADGarbageSize 0 ");
        List<GarbageInfo> adGarbageList = new ArrayList<>();
        ArrayList<ClearItem> clearItems = DBAppAdUtils.get(this, getString(R.string.zh));
//        LogUtil.e(TAG, "getADGarbageSize 1===> " + clearItems.size());

        for (int i = 0; i < clearItems.size(); i++) {
            ClearItem clearItem = clearItems.get(i);
            File file = new File(Environment.getExternalStorageDirectory().getAbsolutePath(), clearItems.get(i).getFilePath());
            if (!file.exists()) {
                continue;
            }
            clearItem.setFilePath(file.getPath());
            GarbageInfo<ClearItem> garbageInfo = new GarbageInfo<>();
            garbageInfo.setData(clearItem);
            garbageInfo.setType(GarbageInfo.TYPE_AD_GARBAGE);
            adGarbageList.add(garbageInfo);
            sendGarbageMsg(garbageInfo.getData().getFileSize());
        }

        mGarbageInfoGroupList.add(adGarbageList);
        LogUtil.e(TAG, "getADGarbageSize SIZE = " + mGarbageInfoGroupList.size());

        mHandler.sendEmptyMessage(MSG_UPDATE_UI);
    }

    private float getSystemGabargeSize() {
        return 0;
    }

    private float getUselessApkSize() {
        return 0;
    }

    private Runnable getUninstallCacheSize() {
        return new Runnable() {
            @Override
            public void run() {
//                mTotalApps = new ArrayList<>();
//                List<AppInfo> systemApps = PackageInfoManager.getInstance().getSystemApps();
//                List<AppInfo> userApps = PackageInfoManager.getInstance().getUserApps();
//                mTotalApps.addAll(systemApps);
//                mTotalApps.addAll(userApps);
//                for (int i = 0; i < mTotalApps.size(); i++) {
//
//                }
            }
        };
    }

    private void getTotalAppCacheSize() {
//        LogUtil.e(TAG, "getTotalAppCacheSize1");
        List<AppInfo> totalApps = new ArrayList<>();
        List<AppInfo> userApps = PackageInfoManager.getInstance().getUserApps();

        totalApps.addAll(userApps);
        if (PermissionManager.getInstance().checkOrRequestedRootPermission()) {
            totalApps.addAll(PackageInfoManager.getInstance().getSystemApps());
        }
//        LogUtil.e(TAG, "getTotalAppCacheSize1-1");

        List<GarbageInfo> appCahceList = new ArrayList<>();
        for (int i = 0; i < totalApps.size(); i++) {
            AppInfo appInfo = totalApps.get(i);
            float cacheSize = appInfo.getCacheSize();
            if (cacheSize > 0) {
                GarbageInfo<AppInfo> garbageInfo = new GarbageInfo<>();
                garbageInfo.setData(appInfo);
                GarbageActivity.this.sendGarbageMsg(garbageInfo.getData().getCacheSize());
                appCahceList.add(garbageInfo);

            }
//            LogUtil.e(TAG, "getTotalAppCacheSize1-2  pos=" + i + "   appinfo=>" + appInfo.toString());


        }
//        LogUtil.e(TAG, "getTotalAppCacheSize2");

//        if (appCahceList.size() <= 0) {
//            return;
//        }

        mGarbageInfoGroupList.add(appCahceList);
        LogUtil.e(TAG, "getTotalAppCacheSize mGarbageInfoGroupList SIZE = " + mGarbageInfoGroupList.size());
        mHandler.sendEmptyMessage(MSG_UPDATE_UI);

    }


    public void sendGarbageMsg(float size) {
        Message message = new Message();
        message.obj = size;
        message.what = MSG_UPDATE_TOTAL_SIZE;
        mHandler.sendMessage(message);
    }


    private void initTitleBar() {
        ToolbarUtil.setTitleBarWhiteBack(this, mToolbar);
        getSupportActionBar().setTitle(getString(R.string.garbage_clean));
    }


    public void refreshTotalCache() {
        mGarbageHeadView.setScanTotal(mTotalSize);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_clean:
                mProgressBar.setVisibility(View.VISIBLE);
                GlobalDataManager.getInstance().getThreadPool().execute(() -> {
                    mGarbageCleanAdapter.removeCheckedItems(mHandler);
                    LogUtil.e("delete", "removeCheckedItems");
                    resetData();
                    LogUtil.e("delete", "resetData");

                    scanGarbage();
                    LogUtil.e("delete", "scanGarbage");

                    mHandler.sendEmptyMessage(MSG_CLEAN_SUCCESS);
                    LogUtil.e("delete", "MSG_CLEAN_SUCCESS");

                });
                break;
            default:
                break;
        }
    }

    private void resetData() {
        mGarbageInfoGroupList.clear();
        mTotalSize = 0;
    }
}
