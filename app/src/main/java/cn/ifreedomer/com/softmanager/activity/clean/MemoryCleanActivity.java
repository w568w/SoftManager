package cn.ifreedomer.com.softmanager.activity.clean;

import android.annotation.SuppressLint;
import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;

import com.zhy.adapter.recyclerview.wrapper.HeaderAndFooterWrapper;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import butterknife.ButterKnife;
import butterknife.InjectView;
import cn.ifreedomer.com.softmanager.R;
import cn.ifreedomer.com.softmanager.activity.BaseActivity;
import cn.ifreedomer.com.softmanager.adapter.MemoryAdapter;
import cn.ifreedomer.com.softmanager.bean.clean.ProcessItem;
import cn.ifreedomer.com.softmanager.util.LogUtil;
import cn.ifreedomer.com.softmanager.util.ProcessManagerUtils;
import cn.ifreedomer.com.softmanager.util.ToolbarUtil;
import cn.ifreedomer.com.softmanager.widget.ArcProgress;
import cn.ifreedomer.com.softmanager.widget.BigFileHeadView;

public class MemoryCleanActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = BigFileCleanActivity.class.getSimpleName();
    @InjectView(R.id.toolbar)
    Toolbar mToolbar;

    @InjectView(R.id.rv)
    RecyclerView mRv;
    @InjectView(R.id.btn_clean)
    Button mBtnClean;
    ArcProgress mArcProgress;
    private List<ProcessItem> mProcessList = new ArrayList<>();
    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;
    private MemoryAdapter mMemoryAdapter;
    private ConcurrentHashMap<String, ProcessItem> mRunedMap = new ConcurrentHashMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_memory_clean);
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
        mMemoryAdapter = new MemoryAdapter(this, R.layout.item_appcache_child, mProcessList);

        //add headview
        BigFileHeadView headerView = new BigFileHeadView(this);
        mArcProgress = (ArcProgress) headerView.findViewById(R.id.arc_progress);
        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(mMemoryAdapter);
        mHeaderAndFooterWrapper.addHeaderView(headerView);
        mRv.setAdapter(mHeaderAndFooterWrapper);
        mHeaderAndFooterWrapper.notifyDataSetChanged();
    }

    private void initTitleBar() {
        ToolbarUtil.setTitleBarWhiteBack(this, mToolbar);
        mToolbar.setNavigationOnClickListener(v -> MemoryCleanActivity.this.finish());
        getSupportActionBar().setTitle(getString(R.string.memory_clean));
    }

    private void startScan() {
        mArcProgress.setProgress(0);
        mArcProgress.setProgress(1);
        asyncTask.execute();
    }


    @SuppressLint("StaticFieldLeak")
    private AsyncTask<String, Integer, List<ProcessItem>> asyncTask = new AsyncTask<String, Integer, List<ProcessItem>>() {
        @Override
        protected List<ProcessItem> doInBackground(String... params) {
            mRunedMap.clear();
            mProcessList.clear();
            ActivityManager am = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
            PackageManager pm = getPackageManager();
            List<ApplicationInfo> installedAppList = pm.getInstalledApplications(0);
            List<ActivityManager.RunningAppProcessInfo> lists = am.getRunningAppProcesses();
            List<ActivityManager.RunningServiceInfo> serviceList = am.getRunningServices(100);
            int serviceSize = serviceList == null ? 0 : serviceList.size();
            for (int i = 0; i < lists.size(); i++) {
                ActivityManager.RunningAppProcessInfo runningAppProcessInfo = lists.get(i);
                if (runningAppProcessInfo.pkgList == null || runningAppProcessInfo.pkgList.length <= 0) {
                    continue;
                }
                publishProgress(i * 100 / (lists.size() + serviceSize));
                try {
                    PackageInfo packageInfo = pm.getPackageInfo(runningAppProcessInfo.pkgList[0], 0);
                    int flags = packageInfo.applicationInfo.flags;
                    if ((flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                        continue;
                    }
                    if (mRunedMap.containsKey(packageInfo.packageName)) {
                        ProcessItem processItem = mRunedMap.get(packageInfo.packageName);
                        mRunedMap.get(packageInfo.packageName).setMemorySize(processItem.getMemorySize() + ProcessManagerUtils.getProcessMemUsage(am, runningAppProcessInfo.pid) * 1000);
                        continue;
                    }
                    LogUtil.e(TAG, "package name=>" + packageInfo.packageName + "   pid=" + runningAppProcessInfo.pid);
                    ProcessItem processItem = new ProcessItem();
                    processItem.setPkgName(packageInfo.packageName);
                    processItem.setMemorySize(ProcessManagerUtils.getProcessMemUsage(am, runningAppProcessInfo.pid) * 1000);
                    processItem.setPid(runningAppProcessInfo.pid + "");
                    processItem.setIcon(packageInfo.applicationInfo.loadIcon(pm));
                    processItem.setDes(packageInfo.applicationInfo.loadDescription(pm));
                    processItem.setLabel(packageInfo.applicationInfo.loadLabel(pm));
                    mRunedMap.put(packageInfo.packageName, processItem);
                    mProcessList.add(processItem);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }

            }

            for (int i = 0; i < serviceSize; i++) {
                ActivityManager.RunningServiceInfo runningServiceInfo = serviceList.get(i);
                publishProgress((lists.size() + i) * 100 / (lists.size() + serviceSize));
                if (runningServiceInfo.service == null) {
                    continue;
                }

                String packageName = runningServiceInfo.service.getPackageName();
                if (TextUtils.isEmpty(packageName)) {
                    continue;
                }
                PackageInfo packageInfo = null;
                try {
                    packageInfo = pm.getPackageInfo(packageName, 0);
                } catch (PackageManager.NameNotFoundException e) {
                    e.printStackTrace();
                }
                if (packageInfo == null) {
                    continue;
                }
                int flags = packageInfo.applicationInfo.flags;
                if ((flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                    continue;
                }

                if (mRunedMap.containsKey(packageName)) {
                    mRunedMap.get(packageName).setMemorySize(ProcessManagerUtils.getProcessMemUsage(am, runningServiceInfo.pid) * 1000);
                    continue;
                }
                ProcessItem processItem = new ProcessItem();
                processItem.setPkgName(packageName);
                processItem.setMemorySize(ProcessManagerUtils.getProcessMemUsage(am, runningServiceInfo.pid) * 1000);
                processItem.setLabel(packageInfo.applicationInfo.loadLabel(pm));
                processItem.setIcon(packageInfo.applicationInfo.loadIcon(pm));
                mProcessList.add(processItem);
                mRunedMap.put(packageName, processItem);
            }

            publishProgress(100);
            return mProcessList;

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            mArcProgress.setProgress(values[0]);
            if (values[0] == 100) {
                mArcProgress.setBottomText(getString(R.string.scan_finish));
                mBtnClean.setVisibility(View.VISIBLE);

            }
        }


        @Override
        protected void onPostExecute(List<ProcessItem> processItemList) {
            mHeaderAndFooterWrapper.notifyDataSetChanged();

        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_clean:
                mMemoryAdapter.removeCheckedItems();
                mHeaderAndFooterWrapper.notifyDataSetChanged();
                break;
            default:
                break;
        }
    }
}
