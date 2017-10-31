package cn.ifreedomer.com.softmanager;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.IPackageStatsObserver;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.PackageStats;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;
import android.os.RemoteException;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import cn.ifreedomer.com.softmanager.model.AppInfo;
import cn.ifreedomer.com.softmanager.util.DataTypeUtil;
import cn.ifreedomer.com.softmanager.util.LogUtil;

/**
 * @author:eavawu
 * @date: 19/02/2017.
 * @todo:包工具
 */

public class PackageInfoManager {
    private static PackageInfoManager packageInfoManager = new PackageInfoManager();

    private PackageInfoManager() {

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


    public void loadData(final Context context, final LoadStateCallback loadStateCallback) {
        AsyncTask task = new AsyncTask<Object, Integer, List<AppInfo>>() {
            private int mAppCount = 0;

            @Override
            protected List<AppInfo> doInBackground(Object... params) {
                LogUtil.d("loadData1");
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        loadStateCallback.loadBegin();
                    }
                });

                Method mGetPackageSizeInfoMethod = null;

                try {
                    mGetPackageSizeInfoMethod = context.getPackageManager().getClass().getMethod(
                            "getPackageSizeInfo", String.class, IPackageStatsObserver.class);


                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
                PackageManager pm = context.getPackageManager();
                List<PackageInfo> packInfos = pm.getInstalledPackages(0);

                List<AppInfo> appinfos = new ArrayList<AppInfo>();
                LogUtil.d("loadData2");
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
                    if (info.packageName.equals(context.getPackageName())) {
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
                    String version = packInfo.versionName;
                    appInfo.setVersion(version);
                    try {

                        mGetPackageSizeInfoMethod.invoke(context.getPackageManager(), new Object[]{
                                packname,
                                new IPackageStatsObserver.Stub() {
                                    @Override
                                    public void onGetStatsCompleted(PackageStats pStats, boolean succeeded) throws RemoteException {
                                        synchronized (appInfo) {
                                            appInfo.setPkgSize(DataTypeUtil.getTwoFloat(pStats.cacheSize + pStats.codeSize + pStats.dataSize));
                                            appInfo.setCacheSize(pStats.cacheSize);

                                        }
                                    }
                                }
                        });
                    } catch (Exception e) {
                    }

                    appinfos.add(appInfo);
                }
                LogUtil.d("loadData3");
                return appinfos;
            }

            @Override
            protected void onProgressUpdate(final Integer... values) {
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        loadStateCallback.loadProgress(values[0], values[1]);
                    }
                });

            }

            @Override
            protected void onPreExecute() {

                super.onPreExecute();
            }

            @Override
            protected void onPostExecute(List<AppInfo> result) {

                super.onPostExecute(result);
                new Handler(Looper.getMainLooper()).post(new Runnable() {
                    @Override
                    public void run() {
                        loadStateCallback.loadFinish();
                    }
                });


            }

        };
        task.execute();


    }

}
