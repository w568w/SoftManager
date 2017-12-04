package cn.ifreedomer.com.softmanager.manager;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.ComponentName;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageDataObserver;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.RemoteException;
import android.os.StatFs;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import cn.ifreedomer.com.softmanager.LoadStateCallback;
import cn.ifreedomer.com.softmanager.model.AppInfo;
import cn.ifreedomer.com.softmanager.util.DataTypeUtil;
import cn.ifreedomer.com.softmanager.util.LogUtil;
import cn.ifreedomer.com.softmanager.util.ShellUtils;
import cn.ifreedomer.com.softmanager.util.XmlUtil;

/**
 * @author:eavawu
 * @date: 19/02/2017.
 * @todo:包工具
 */

public class PackageInfoManager {
    private String TAG = PackageInfoManager.class.getSimpleName();
    private static PackageInfoManager packageInfoManager = new PackageInfoManager();
    private Context mContext;
    private Method mDeleteApplicationCacheFiles;
    private StatFs mStat;
    private boolean isLoaded = false;
    private boolean isLoadFinish = false;
    private boolean isComponentLoaded = false;

    private PackageInfoManager() {

    }

    public void init(Context context) {
        this.mContext = context;
        try {
            mDeleteApplicationCacheFiles = mContext.getPackageManager().getClass().getMethod(
                    "deleteApplicationCacheFiles", String.class, IPackageDataObserver.class);
            mDeleteApplicationCacheFiles.setAccessible(true);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
    }

    public static PackageInfoManager getInstance() {
        return packageInfoManager;
    }

    private List<AppInfo> systemAppInfos = new ArrayList<>();
    private List<AppInfo> userAppInfos = new ArrayList<>();

    public List<AppInfo> getSystemApps() {
        return systemAppInfos;
    }

    public List<AppInfo> getUserApps() {
        return userAppInfos;
    }


    private List<LoadStateCallback> loadStateCallbackList = new ArrayList<>();

    public void addLoadStateCallback(LoadStateCallback loadStateCallback) {
        if (loadStateCallback != null) {
            loadStateCallbackList.add(loadStateCallback);
        }
    }


    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    public void loadData(final LoadStateCallback loadStateCallback) {
        //防止重复加载
        if (isLoaded) {
            return;
        }
        isLoaded = true;
        @SuppressLint("StaticFieldLeak") AsyncTask task = new AsyncTask<Object, Integer, List<AppInfo>>() {
            private int mAppCount = 0;

            @Override
            protected List<AppInfo> doInBackground(Object... params) {
                Method mGetPackageSizeInfoMethod = null;

                try {
                    mGetPackageSizeInfoMethod = mContext.getPackageManager().getClass().getMethod(
                            "getPackageSizeInfo", String.class, IPackageStatsObserver.class);


                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
                PackageManager pm = mContext.getPackageManager();
                List<PackageInfo> packInfos = pm.getInstalledPackages(0);




                List<AppInfo> appinfos = new ArrayList<AppInfo>();
                for (PackageInfo packInfo : packInfos) {
                    publishProgress(++mAppCount, packInfos.size());
                    final AppInfo appInfo = new AppInfo();
                    Drawable appIcon = packInfo.applicationInfo.loadIcon(pm);
                    ApplicationInfo info = packInfo.applicationInfo;
                    appInfo.setAppIcon(appIcon);
                    if (info.sourceDir == null) {
                        continue;
                    }
                    appInfo.setCodePath(info.sourceDir);
                    if (info.packageName.equals(mContext.getPackageName())) {
                        continue;
                    }
                    int flags = packInfo.applicationInfo.flags;

                    int uid = packInfo.applicationInfo.uid;

                    appInfo.setUid(uid);

                    if ((flags & ApplicationInfo.FLAG_SYSTEM) != 0) {
                        appInfo.setUserApp(false);//系统应用

                    } else {
                        appInfo.setUserApp(true);//用户应用
                    }

                    boolean flag = false;
                    if ((info.flags & ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
                        flag = true;
                    } else if ((info.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                        flag = true;
                    }
                    if (flag) {
                        userAppInfos.add(appInfo);
                    } else {
                        systemAppInfos.add(appInfo);
                    }
                    String appName = packInfo.applicationInfo.loadLabel(pm).toString();
                    appInfo.setAppName(appName);
                    String packname = packInfo.packageName;
                    appInfo.setPackname(packname);
                    appInfo.setEnable(PackageInfoManager.getInstance().isAppEnable(packname));
                    String version = packInfo.versionName;
                    appInfo.setVersion(version);
                    try {

                        mGetPackageSizeInfoMethod.invoke(mContext.getPackageManager(), new Object[]{
                                packname,
                                new IPackageStatsObserver.Stub() {
                                    @Override
                                    public void onGetStatsCompleted(PackageStats pStats, boolean succeeded) throws RemoteException {
                                        synchronized (appInfo) {
                                            appInfo.setPkgSize(DataTypeUtil.getTwoFloat((pStats.cacheSize + pStats.codeSize + pStats.dataSize)));
                                            appInfo.setCacheSize(DataTypeUtil.getTwoFloat(pStats.cacheSize));

                                        }
                                    }
                                }
                        });
                    } catch (Exception e) {


                        e.printStackTrace();
                    }
                    appInfo.setPermissionDetailList(PermissionManager.getInstance().getAppPermission(appInfo.getPackname()));
                    appinfos.add(appInfo);
                }


                return appinfos;
            }

            @Override
            protected void onProgressUpdate(final Integer... values) {
                new Handler(Looper.getMainLooper()).post(() -> loadStateCallback.loadProgress(values[0], values[1]));

            }

            @Override
            protected void onPreExecute() {

                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(List<AppInfo> result) {
                LogUtil.e(TAG, "onPostExecute");
                isLoadFinish = true;
                super.onPostExecute(result);
                new Handler(Looper.getMainLooper()).post(() -> {
                    for (int i = 0; i < loadStateCallbackList.size(); i++) {
                        loadStateCallbackList.get(i).loadFinish();
                    }
                });


            }

        };
        task.execute();

    }


    public void clearCache(String packageName) {
        try {
            mDeleteApplicationCacheFiles.invoke(mContext.getPackageManager(),
                    packageName,
                    new IPackageDataObserver() {
                        @Override
                        public void onRemoveCompleted(String packageName, boolean succeeded) throws RemoteException {
                            LogUtil.e(TAG, "当前清理的包名:" + packageName);
                            if (succeeded) {
                                LogUtil.e(TAG, "清理成功！");
                            } else {
                                LogUtil.e(TAG, "清理失败！");
                            }
                        }

                        @Override
                        public IBinder asBinder() {
                            return null;
                        }
                    });
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }


    public String getVersionCode(Context context) {
        PackageManager manager = context.getPackageManager();
        PackageInfo info = null;
        try {
            info = manager.getPackageInfo(
                    context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String version = info.versionName;
        return version;
    }


    public boolean isAppEnable(String pkgName) {
        int applicationEnabledSetting = mContext.getPackageManager().getApplicationEnabledSetting(pkgName);
        return applicationEnabledSetting != PackageManager.COMPONENT_ENABLED_STATE_DISABLED;
    }


    public boolean isComponentEnable(String component) {
        ComponentName componentName = new ComponentName(mContext, component);
        return mContext.getPackageManager().getComponentEnabledSetting(componentName) != PackageManager.COMPONENT_ENABLED_STATE_DISABLED;

    }

    public boolean isLoadFinish() {
        return isLoadFinish;
    }

    public List<AppInfo> getAllApp() {
        List<AppInfo> allApps = new ArrayList<>();
        allApps.addAll(userAppInfos);
        allApps.addAll(systemAppInfos);
        return allApps;
    }


    //加载所有组件
    public void loadAllComponent() {
        List<AppInfo> allApp = getAllApp();
        LogUtil.d(TAG, "loadAllComponent size=>" + allApp.size());

        for (int i = 0; i < allApp.size(); i++) {
            AppInfo appInfo = allApp.get(i);
            LogUtil.d(TAG, "loadAllComponent=>" + appInfo.getPackname());
            XmlUtil.parseAppInfo(mContext, appInfo.getPackname(), appInfo);
        }
        isComponentLoaded = true;
    }


    public void disableComponent(String component) {
        GlobalDataManager.getInstance().getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                ShellUtils.execCommand("pm disable " + component, true);
            }
        });
    }

    public void enableComponent(String component) {
        GlobalDataManager.getInstance().getThreadPool().execute(() -> {
            ShellUtils.execCommand("pm disable " + component, true);
        });
    }


    public boolean isComponentLoaded() {
        return isComponentLoaded;
    }

}
