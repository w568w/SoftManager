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
import cn.ifreedomer.com.softmanager.bean.clean.AppCacheItem;
import cn.ifreedomer.com.softmanager.bean.clean.ClearItem;
import cn.ifreedomer.com.softmanager.bean.clean.EmptyFileItem;
import cn.ifreedomer.com.softmanager.bean.clean.GarbageGroupTitle;
import cn.ifreedomer.com.softmanager.db.DBAppAdUtils;
import cn.ifreedomer.com.softmanager.manager.GlobalDataManager;
import cn.ifreedomer.com.softmanager.manager.PackageInfoManager;
import cn.ifreedomer.com.softmanager.manager.PermissionManager;
import cn.ifreedomer.com.softmanager.model.AppInfo;
import cn.ifreedomer.com.softmanager.util.DataTypeUtil;
import cn.ifreedomer.com.softmanager.util.FileUtil;
import cn.ifreedomer.com.softmanager.util.LogUtil;
import cn.ifreedomer.com.softmanager.util.ToolbarUtil;
import cn.ifreedomer.com.softmanager.widget.NumChangeHeadView;

public class GarbageActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = GarbageActivity.class.getSimpleName();
    private static final int MSG_UPDATE_TOTAL_SIZE = 1;
    public static final int MSG_UPDATE_UI = 2;
    private static final int MSG_CLEAN_SUCCESS = 3;
    public static final int MSG_CLEAN_PROCESSING = 4;
    public static final int MSG_LOAD_GARBAGE_FINISH = 5;
    private static final int MSG_MODULE_FINISH = 6;
    @InjectView(R.id.toolbar)
    Toolbar mToolbar;

    @InjectView(R.id.expand_listview)
    ExpandableListView mExpandListview;
    @InjectView(R.id.btn_clean)
    Button mBtnClean;
    @InjectView(R.id.pb)
    ProgressBar mProgressBar;
    private float mTotalSize = 0;
    private List<List<GarbageInfo>> groupList = new ArrayList<>();
    private List<GarbageInfo> appCacheGarbageList = new ArrayList<>();
    private List<GarbageInfo> emptyFileList = new ArrayList<>();
    private List<GarbageInfo> adGarbageList = new ArrayList<>();
    private int curModuleCount = 0;
    private static final int MAX_MODULE_COUNT = 3;
    private List<GarbageGroupTitle> mTitleList = new ArrayList<>();

    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_MODULE_FINISH:
                    curModuleCount++;
                    if (curModuleCount >= MAX_MODULE_COUNT) {
                        mProgressBar.setVisibility(View.GONE);
                        getTotalGarbageSize();
                        sendEmptyMessage(MSG_UPDATE_TOTAL_SIZE);
                    }
                    break;
                case MSG_UPDATE_TOTAL_SIZE:
                    mGarbageHeadView.setScanTotal(DataTypeUtil.getTextBySize(mTotalSize));
                    break;
                case MSG_UPDATE_UI:
                    mGarbageCleanAdapter.notifyDataSetChanged();
                    break;
                case MSG_LOAD_GARBAGE_FINISH:
                    mGarbageHeadView.setScanningText(getString(R.string.app_scaned));
                    mProgressBar.setVisibility(View.GONE);
                    mGarbageCleanAdapter.notifyDataSetChanged();
                    for (int i = 0; i < groupList.size(); i++) {
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
        initGroupInfo();
        //copy db file
        mProgressBar.setVisibility(View.VISIBLE);

        LogUtil.e(TAG, "copy db");
        GlobalDataManager.getInstance().getThreadPool().execute(new Runnable() {
            @Override
            public void run() {

                runOnUiThread(() -> scanGarbage());

            }
        });
        LogUtil.e(TAG, "scanGarbage");

    }

    private void initGroupInfo() {
        GarbageGroupTitle garbageGroupTitle = new GarbageGroupTitle(getString(R.string.app_cache), GarbageInfo.TYPE_APP_CACHE);
        mTitleList.add(garbageGroupTitle);
        //填充group信息
        garbageGroupTitle = new GarbageGroupTitle(getString(R.string.empty_file), GarbageInfo.TYPE_EMPTY_FILE);
        mTitleList.add(garbageGroupTitle);
        mTitleList.add(new GarbageGroupTitle(getString(R.string.ad_garbage), GarbageInfo.TYPE_AD_GARBAGE));
        //填充List信息
        groupList.add(appCacheGarbageList);
        groupList.add(emptyFileList);
        groupList.add(adGarbageList);
    }

    private void initListener() {
        mBtnClean.setOnClickListener(this);
    }


    private void initExpandbleListView() {

//        mTitleList = new String[]{getString(R.string.app_cache), getString(R.string.empty_file), getString(R.string.ad_garbage)};
        mGarbageCleanAdapter = new GarbageCleanAdapter(this, mTitleList, groupList);
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
    }


    private void getEmptyFileSize() {

        GlobalDataManager.getInstance().getThreadPool().execute(() -> {
            EmptyFolder emptyFolders = FileUtil.getEmptyFile();
            if (emptyFolders == null || emptyFolders.getPathList() == null) {
                return;
            }
            for (int i = 0; i < emptyFolders.getPathList().size(); i++) {
                EmptyFileItem emptyFileItem = new EmptyFileItem();
                emptyFileItem.setPath(emptyFolders.getPathList().get(i));
                emptyFileItem.setSize(new File(emptyFolders.getPathList().get(i)).length());
                GarbageInfo<EmptyFileItem> emptyFileItemGarbageInfo = new GarbageInfo<>();
                emptyFileItemGarbageInfo.setData(emptyFileItem);
                emptyFileItemGarbageInfo.setType(GarbageInfo.TYPE_EMPTY_FILE);
                emptyFileList.add(emptyFileItemGarbageInfo);
            }
            mHandler.sendEmptyMessage(MSG_MODULE_FINISH);

        });
    }

    private void getADGarbageSize() {
        adGarbageList.clear();
        GlobalDataManager.getInstance().getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                ArrayList<ClearItem> clearItems = DBAppAdUtils.get(GarbageActivity.this, getString(R.string.zh));
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
                    sendGarbageMsg(garbageInfo.getData().getFileSize());
                    mHandler.sendEmptyMessage(MSG_MODULE_FINISH);
                    runOnUiThread(() -> {
                        adGarbageList.add(garbageInfo);
                        mHandler.sendEmptyMessage(MSG_UPDATE_UI);
                    });
                }

            }
        });


    }


    private void getTotalAppCacheSize() {
        appCacheGarbageList.clear();
        List<AppInfo> totalApps = new ArrayList<>();
        List<AppInfo> userApps = PackageInfoManager.getInstance().getUserApps();

        totalApps.addAll(userApps);
        if (PermissionManager.getInstance().checkOrRequestedRootPermission()) {
            totalApps.addAll(PackageInfoManager.getInstance().getSystemApps());
        }

        for (int i = 0; i < totalApps.size(); i++) {
            AppInfo appInfo = totalApps.get(i);
            Log.d(TAG, totalApps.get(i) + "   cache size = " + appInfo.getCacheSize());
            float cacheSize = appInfo.getCacheSize();
            if (this.getPackageName().equals(appInfo.getPackname())) {
                continue;
            }
            if (cacheSize > 0) {
                AppCacheItem appCacheItem = new AppCacheItem();
                GarbageInfo<AppCacheItem> garbageInfo = new GarbageInfo<>();
                garbageInfo.setData(appCacheItem);
                appCacheItem.setPkgName(appInfo.getPackname());
                appCacheItem.setPath(appInfo.getCodePath());
                appCacheItem.setAppName(appInfo.getAppName());
                appCacheItem.setSize((long) appInfo.getCacheSize());
                appCacheItem.setAppIcon(appInfo.getAppIcon());
                appCacheGarbageList.add(garbageInfo);
            }
            mHandler.sendEmptyMessage(MSG_MODULE_FINISH);
        }
        runOnUiThread(() -> {
            LogUtil.e(TAG, "getTotalAppCacheSize groupList SIZE = " + groupList.size());
            mHandler.sendEmptyMessage(MSG_UPDATE_UI);
        });
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
        mGarbageHeadView.setScanTotal(DataTypeUtil.getTextBySize(mTotalSize));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_clean:
                getTotalGarbageSize();
                mProgressBar.setVisibility(View.VISIBLE);

                mGarbageCleanAdapter.removeCheckFiles(new RemoveFinishCallback() {
                    @Override
                    public void finish() {
                        mProgressBar.setVisibility(View.GONE);
                        mHandler.sendEmptyMessage(MSG_CLEAN_SUCCESS);
                        mHandler.sendEmptyMessage(MSG_UPDATE_TOTAL_SIZE);

                    }

                    @Override
                    public void delete(float fileSize) {
                        mTotalSize = mTotalSize - fileSize;

                    }
                }, mHandler);


                break;
            default:
                break;
        }
    }

    private void getTotalGarbageSize() {
        mTotalSize = 0;
        for (int i = 0; i < groupList.size(); i++) {
            for (int j = 0; j < groupList.get(i).size(); j++) {
                mTotalSize = mTotalSize + groupList.get(i).get(j).getData().getSize();
            }
        }
    }


    public interface RemoveFinishCallback {
        void finish();

        void delete(float fileSize);
    }
}
