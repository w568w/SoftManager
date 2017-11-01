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
import cn.ifreedomer.com.softmanager.GlobalDataManager;
import cn.ifreedomer.com.softmanager.PackageInfoManager;
import cn.ifreedomer.com.softmanager.R;
import cn.ifreedomer.com.softmanager.adapter.GarbageCleanAdapter;
import cn.ifreedomer.com.softmanager.bean.GarbageInfo;
import cn.ifreedomer.com.softmanager.model.AppInfo;
import cn.ifreedomer.com.softmanager.util.LogUtil;
import cn.ifreedomer.com.softmanager.util.ToolbarUtil;
import cn.ifreedomer.com.softmanager.widget.GarbageHeadView;

public class GarbageActivity extends AppCompatActivity {
    private static final String TAG = GarbageActivity.class.getSimpleName();
    private static final int MSG_UPDATE_TOTAL_SIZE = 1;
    private static final int MSG_APP_SCAN_FINISH = 2;
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
                    GarbageInfo garbageInfo = (GarbageInfo) msg.obj;
                    mTotalSize = mTotalSize + garbageInfo.getSize();
                    mGarbageHeadView.setScanTotal(mTotalSize);
                    mGarbageHeadView.setScanningText(garbageInfo.getPath());
                    LogUtil.e(TAG, "mTotalSize:" + mTotalSize + "");
                    break;
                case MSG_APP_SCAN_FINISH:
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
        scanGarbage();
    }


    private void initExpandbleListView() {
        mTitles = new String[]{getString(R.string.app_cache), getString(R.string.ad_garbage)};
        mGarbageCleanAdapter = new GarbageCleanAdapter(this, mTitles, mGarbageInfoGroupList);
        mGarbageHeadView = new GarbageHeadView(this);
        mExpandListview.addHeaderView(mGarbageHeadView);
        mExpandListview.setAdapter(mGarbageCleanAdapter);
        mExpandListview.setOnGroupClickListener(new ExpandableListView.OnGroupClickListener() {
            @Override
            public boolean onGroupClick(ExpandableListView parent, View v, int groupPosition, long id) {
                Log.e(TAG, "onGroupClick: " + "group position=" + groupPosition);
                return false;
            }
        });


    }


    private void scanGarbage() {

        getTotalAppCacheSize();
        getUninstallCacheSize();
        getUselessApkSize();
        getSystemGabargeSize();
        getADGarbageSize();
        getUselessFileSize();
    }

    private float getUselessFileSize() {
        return 0;
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
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                List<AppInfo> totalApps = new ArrayList<>();
                List<AppInfo> systemApps = PackageInfoManager.getInstance().getSystemApps();
                List<AppInfo> userApps = PackageInfoManager.getInstance().getUserApps();
                totalApps.addAll(systemApps);
                totalApps.addAll(userApps);
                List<GarbageInfo> mGarbageInfoList = new ArrayList<>();
                for (int i = 0; i < totalApps.size(); i++) {
                    AppInfo appInfo = totalApps.get(i);
                    float cacheSize = appInfo.getCacheSize();
                    if (cacheSize > 0) {
                        GarbageInfo garbageInfo = new GarbageInfo(appInfo.getAppName(), appInfo.getCodePath(), appInfo.getCacheSize(), appInfo.getAppIcon());
                        mGarbageInfoList.add(garbageInfo);
                        garbageInfo.setPackageName(appInfo.getPackname());
                        sendGarbageMsg(garbageInfo);

                    }

                }
                mGarbageInfoGroupList.add(mGarbageInfoList);
                handler.sendEmptyMessage(MSG_APP_SCAN_FINISH);
            }
        };
        GlobalDataManager.getInstance().getThreadPool().execute(runnable);
    }


    public void sendGarbageMsg(GarbageInfo garbageInfo) {
        Message message = new Message();
        message.obj = garbageInfo;
        message.what = MSG_UPDATE_TOTAL_SIZE;
        handler.sendMessage(message);
    }


    private void initTitleBar() {
        ToolbarUtil.setTitleBarWhiteBack(this, mToolbar);
        getSupportActionBar().setTitle(getString(R.string.garbage_clean));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GarbageActivity.this.finish();
            }
        });
    }


}
