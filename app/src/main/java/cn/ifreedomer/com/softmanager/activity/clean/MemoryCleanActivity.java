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
import android.widget.Toast;

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
import cn.ifreedomer.com.softmanager.util.DataTypeUtil;
import cn.ifreedomer.com.softmanager.util.LogUtil;
import cn.ifreedomer.com.softmanager.util.ProcessManagerUtils;
import cn.ifreedomer.com.softmanager.util.ToolbarUtil;
import cn.ifreedomer.com.softmanager.widget.NumChangeHeadView;

public class MemoryCleanActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = BigFileCleanActivity.class.getSimpleName();
    @InjectView(R.id.toolbar)
    Toolbar mToolbar;

    @InjectView(R.id.rv)
    RecyclerView mRv;
    @InjectView(R.id.btn_clean)
    Button mBtnClean;
    private List<ProcessItem> mProcessList = new ArrayList<>();
    private HeaderAndFooterWrapper mHeaderAndFooterWrapper;
    private MemoryAdapter mMemoryAdapter;
    private ConcurrentHashMap<String, ProcessItem> mRunedMap = new ConcurrentHashMap<>();
    private float mTotalMemoryGarbage = 0;
    private NumChangeHeadView mHeaderView;

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
        mHeaderView = new NumChangeHeadView(this);

        mHeaderAndFooterWrapper = new HeaderAndFooterWrapper(mMemoryAdapter);
        mHeaderAndFooterWrapper.addHeaderView(mHeaderView);
        mRv.setAdapter(mHeaderAndFooterWrapper);
        mHeaderAndFooterWrapper.notifyDataSetChanged();
    }

    private void initTitleBar() {
        ToolbarUtil.setTitleBarWhiteBack(this, mToolbar);
        getSupportActionBar().setTitle(getString(R.string.memory_clean));
    }

    private void startScan() {
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
                    if (packageInfo.packageName.equals(getPackageName())) {
                        continue;
                    }
                    if (mRunedMap.containsKey(packageInfo.packageName)) {
                        ProcessItem processItem = mRunedMap.get(packageInfo.packageName);
                        int appendPackMemory = ProcessManagerUtils.getProcessMemUsage(am, runningAppProcessInfo.pid) * 1000;
                        mTotalMemoryGarbage = appendPackMemory + mTotalMemoryGarbage;
                        mRunedMap.get(packageInfo.packageName).getPids().add(runningAppProcessInfo.pid);
                        mRunedMap.get(packageInfo.packageName).setMemorySize(processItem.getMemorySize() + appendPackMemory);
                        continue;
                    }
                    LogUtil.e(TAG, "package name=>" + packageInfo.packageName + "   pid=" + runningAppProcessInfo.pid);
                    ProcessItem processItem = new ProcessItem();
                    processItem.setPkgName(packageInfo.packageName);
                    processItem.setMemorySize(ProcessManagerUtils.getProcessMemUsage(am, runningAppProcessInfo.pid) * 1000);
                    processItem.getPids().add(runningAppProcessInfo.pid);
                    processItem.setIcon(packageInfo.applicationInfo.loadIcon(pm));
                    processItem.setDes(packageInfo.applicationInfo.loadDescription(pm));
                    processItem.setLabel(packageInfo.applicationInfo.loadLabel(pm));
                    processItem.setChecked(true);
                    mRunedMap.put(packageInfo.packageName, processItem);
                    mTotalMemoryGarbage = mTotalMemoryGarbage + processItem.getMemorySize();
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
                if (packageName.equals(getPackageName())) {
                    continue;
                }
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
                    int appendPackMemory = ProcessManagerUtils.getProcessMemUsage(am, runningServiceInfo.pid) * 1000;
                    mTotalMemoryGarbage = mTotalMemoryGarbage + appendPackMemory;
                    mRunedMap.get(packageName).getPids().add(runningServiceInfo.pid);
                    mRunedMap.get(packageName).setMemorySize(appendPackMemory + mRunedMap.get(packageName).getMemorySize());
                    continue;
                }
                ProcessItem processItem = new ProcessItem();
                processItem.setPkgName(packageName);
                processItem.setMemorySize(ProcessManagerUtils.getProcessMemUsage(am, runningServiceInfo.pid) * 1000);
                processItem.setLabel(packageInfo.applicationInfo.loadLabel(pm));
                processItem.setIcon(packageInfo.applicationInfo.loadIcon(pm));
                processItem.setChecked(true);
                processItem.getPids().add(runningServiceInfo.pid);
                mProcessList.add(processItem);
                mRunedMap.put(packageName, processItem);
                mTotalMemoryGarbage = mTotalMemoryGarbage + processItem.getMemorySize();

            }

            publishProgress(100);
            return mProcessList;

        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            if (values[0] == 100) {
                mBtnClean.setVisibility(View.VISIBLE);
            }
            mHeaderView.setScanningText("");
            mHeaderView.setScanTotal(DataTypeUtil.getTextBySize(mTotalMemoryGarbage));
        }


        @Override
        protected void onPostExecute(List<ProcessItem> processItemList) {
            mHeaderAndFooterWrapper.notifyDataSetChanged();

        }
    };

    public void refreshHeadNum() {
        int mCurrentMemoryGarbage = 0;
        for (int i = 0; i < mProcessList.size(); i++) {
            ProcessItem processItem = mProcessList.get(i);
            mCurrentMemoryGarbage = mCurrentMemoryGarbage + processItem.getMemorySize();
        }
        mHeaderView.setScanTotal(DataTypeUtil.getTextBySize(mCurrentMemoryGarbage));
        Toast.makeText(this, getString(R.string.clear_text) + DataTypeUtil.getTextBySize(mTotalMemoryGarbage - mCurrentMemoryGarbage), Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_clean:
                mMemoryAdapter.removeCheckedItems();
                mHeaderAndFooterWrapper.notifyDataSetChanged();
                refreshHeadNum();
                break;
            default:
                break;
        }
    }
}
