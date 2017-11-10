package cn.ifreedomer.com.softmanager.activity.clean;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.ifreedomer.com.softmanager.R;
import cn.ifreedomer.com.softmanager.adapter.GarbageCleanAdapter;
import cn.ifreedomer.com.softmanager.bean.EmptyFolder;
import cn.ifreedomer.com.softmanager.bean.GarbageInfo;
import cn.ifreedomer.com.softmanager.bean.clean.AppCacheInfo;
import cn.ifreedomer.com.softmanager.bean.clean.EmptyFolderInfo;
import cn.ifreedomer.com.softmanager.manager.GlobalDataManager;
import cn.ifreedomer.com.softmanager.manager.PackageInfoManager;
import cn.ifreedomer.com.softmanager.model.AppInfo;
import cn.ifreedomer.com.softmanager.util.DBUtil;
import cn.ifreedomer.com.softmanager.util.FileUtil;
import cn.ifreedomer.com.softmanager.util.LogUtil;
import cn.ifreedomer.com.softmanager.util.ToolbarUtil;
import cn.ifreedomer.com.softmanager.widget.GarbageHeadView;

public class GarbageActivity extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = GarbageActivity.class.getSimpleName();
    private static final int MSG_UPDATE_TOTAL_SIZE = 1;
    private static final int MSG_UPDATE_UI = 2;
    @InjectView(R.id.toolbar)
    Toolbar mToolbar;

    @InjectView(R.id.expand_listview)
    ExpandableListView mExpandListview;
    @InjectView(R.id.btn_clean)
    Button mBtnClean;
    private float mTotalSize = 0;
    private List<List<GarbageInfo>> mGarbageInfoGroupList = new ArrayList<>();
    private String[] mTitles;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_UPDATE_TOTAL_SIZE:
                    mTotalSize = mTotalSize + (Float) msg.obj;
                    mGarbageHeadView.setScanTotal(mTotalSize);
                    LogUtil.e(TAG, "mTotalSize:" + mTotalSize + "");
                    break;
                case MSG_UPDATE_UI:
                    mGarbageCleanAdapter.notifyDataSetChanged();
                    for (int i = 0; i < mGarbageInfoGroupList.size(); i++) {
                        mExpandListview.expandGroup(i);
                    }
                    mGarbageHeadView.setScanningText(getString(R.string.app_scaned));
                    break;
                default:
                    break;
            }
        }
    };
    private GarbageCleanAdapter mGarbageCleanAdapter;
    private GarbageHeadView mGarbageHeadView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_garbage);
        ButterKnife.inject(this);
        initTitleBar();
        initExpandbleListView();
        initListener();


        //copy db file
        GlobalDataManager.getInstance().getThreadPool().execute(() -> {
            DBUtil.copyDB(GarbageActivity.this);
            scanGarbage();
            getADGarbageSize();
            //init ui
        });


    }

    private void initListener() {
        mBtnClean.setOnClickListener(this);
    }


    private void initExpandbleListView() {
        mTitles = new String[]{getString(R.string.app_cache), getString(R.string.empty_file), getString(R.string.ad_garbage)};
        mGarbageCleanAdapter = new GarbageCleanAdapter(this, mTitles, mGarbageInfoGroupList);
        mGarbageHeadView = new GarbageHeadView(this);
        mExpandListview.addHeaderView(mGarbageHeadView);
        mExpandListview.setAdapter(mGarbageCleanAdapter);
        mExpandListview.setOnGroupClickListener((parent, v, groupPosition, id) -> {
            Log.e(TAG, "onGroupClick: " + "group position=" + groupPosition);
            return false;
        });


    }


    private void scanGarbage() {

        getTotalAppCacheSize();
        getUninstallCacheSize();
        getSystemGabargeSize();
        getADGarbageSize();
        getEmptyFileSize();
    }


    private void getEmptyFileSize() {
        Runnable runnable = () -> {
            EmptyFolder emptyFile = FileUtil.getEmptyFile();
            List<GarbageInfo> emptyList = new ArrayList<>();
            GarbageInfo<EmptyFolderInfo> emptyGarbageInfo = EmptyFolderInfo.create(emptyFile.getTotalSize(), emptyFile.getPathList().size(), getString(R.string.not_use_empty));
            emptyList.add(emptyGarbageInfo);
            mGarbageInfoGroupList.add(emptyList);
            sendGarbageMsg(emptyGarbageInfo.getData().getEmptySize());
            handler.sendEmptyMessage(MSG_UPDATE_UI);
        };
        GlobalDataManager.getInstance().getThreadPool().execute(runnable);
    }

    private float getADGarbageSize() {


        return 0;
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
        List<AppInfo> totalApps = new ArrayList<>();
        List<AppInfo> userApps = PackageInfoManager.getInstance().getUserApps();
        totalApps.addAll(userApps);
        List<GarbageInfo> appCahceList = new ArrayList<>();
        for (int i = 0; i < totalApps.size(); i++) {
            AppInfo appInfo = totalApps.get(i);
            float cacheSize = appInfo.getCacheSize();
            if (cacheSize > 0) {
                AppCacheInfo appCacheInfo = new AppCacheInfo(appInfo.getAppName(), appInfo.getCodePath(), appInfo.getCacheSize(), appInfo.getAppIcon(), appInfo.getPackname());
                GarbageInfo<AppCacheInfo> garbageInfo = AppCacheInfo.create(appCacheInfo);
                GarbageActivity.this.sendGarbageMsg(garbageInfo.getData().getSize());
                appCahceList.add(garbageInfo);
            }

        }
        mGarbageInfoGroupList.add(appCahceList);
        handler.sendEmptyMessage(MSG_UPDATE_UI);
    }


    public void sendGarbageMsg(float size) {
        Message message = new Message();
        message.obj = size;
        message.what = MSG_UPDATE_TOTAL_SIZE;
        handler.sendMessage(message);
    }


    private void initTitleBar() {
        ToolbarUtil.setTitleBarWhiteBack(this, mToolbar);
        getSupportActionBar().setTitle(getString(R.string.garbage_clean));
        mToolbar.setNavigationOnClickListener(v -> GarbageActivity.this.finish());
    }


    public void refreshTotalCache() {
        mTotalSize = 0;
//        for (int i = 0; i < mGarbageInfoGroupList.size(); i++) {
//            List<GarbageInfo> garbageInfos = mGarbageInfoGroupList.get(i);
//            for (int j = 0; j < garbageInfos.size(); j++) {
//                mTotalSize = mTotalSize + garbageInfos.get(j).getSize();
//            }
//        }
        mGarbageHeadView.setScanTotal(mTotalSize);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_clean:
                mGarbageCleanAdapter.removeCheckedItems();
                refreshTotalCache();
                break;
            default:
                break;
        }
    }
}
