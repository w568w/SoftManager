package cn.ifreedomer.com.softmanager.manager;

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
import android.os.Build;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.RemoteException;
import android.os.StatFs;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.util.Log;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;

import cn.ifreedomer.com.softmanager.LoadStateCallback;
import cn.ifreedomer.com.softmanager.bean.ComponentEntity;
import cn.ifreedomer.com.softmanager.db.DBSoftUtil;
import cn.ifreedomer.com.softmanager.listener.LoadAllComponentListener;
import cn.ifreedomer.com.softmanager.model.AppInfo;
import cn.ifreedomer.com.softmanager.util.DataTypeUtil;
import cn.ifreedomer.com.softmanager.util.LogUtil;
import cn.ifreedomer.com.softmanager.util.ShellUtils;
import cn.ifreedomer.com.softmanager.util.Terminal;
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
    private List<AppInfo> backupList = new ArrayList<>();
    private boolean isLoaded = false;
    private boolean isLoadFinish = false;
    private boolean isComponentLoaded = false;
    private Method mGetPackageSizeInfoMethod;
    public static final int LOAD_APP_MESSAGE = 1;
    public static final int LOAD_COMPONENT_MESSAGE = 2;


    private int loadAppPartCount = 0;
    private int loadComponentPartCount = 0;
    private static final int LOAD_APP_MAX_PART = 3;
    private static final int LOAD_COMPONENT_MAX_PART = 4;
    private Handler mHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case LOAD_COMPONENT_MESSAGE:
                    loadComponentPartCount = loadComponentPartCount + 1;
                    LogUtil.d(TAG, "loadComponentPartCount =" + loadComponentPartCount);
                    if (loadComponentPartCount == LOAD_COMPONENT_MAX_PART) {
                        if (mLoadAllComponentListener != null) {
                            isComponentLoaded = true;
                            mLoadAllComponentListener.loadFinish();
                        }
                    }
                    break;
                case LOAD_APP_MESSAGE:
                    List<AppInfo> appInfos = (List<AppInfo>) msg.obj;
                    for (AppInfo appInfo : appInfos) {
                        if (appInfo.isUserApp()) {
                            userAppInfos.add(appInfo);
                        } else {
                            systemAppInfos.add(appInfo);
                        }
                    }
                    loadAppPartCount = loadAppPartCount + 1;
                    LogUtil.d(TAG, "loadAppPartCount =" + loadAppPartCount);
                    if (loadAppPartCount == LOAD_APP_MAX_PART) {
                        isLoadFinish = true;
                        LogUtil.d(TAG, "loadAppPartCount loadAppPartCount=" + isLoadFinish);

                        for (LoadStateCallback loadStateCallbackItem : loadStateCallbackList) {
                            if (loadStateCallbackItem != null) {
                                loadStateCallbackItem.loadFinish();
                            }
                        }
                    }

                    break;
            }
        }
    };
    private LoadAllComponentListener mLoadAllComponentListener;


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
        LogUtil.d(TAG, "getUserApps size = " + userAppInfos.size());
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

        mGetPackageSizeInfoMethod = null;
        try {
            mGetPackageSizeInfoMethod = mContext.getPackageManager().getClass().getMethod(
                    "getPackageSizeInfo", String.class, IPackageStatsObserver.class);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }


        PackageManager pm = mContext.getPackageManager();
        List<PackageInfo> packInfos = pm.getInstalledPackages(0);
        if (packInfos == null || packInfos.size() == 0) {
            return;
        }
        int taotalSize = packInfos.size();
        int firstSize = packInfos.size() / 3;
        int secondSize = packInfos.size() / 3 * 2;
        int thirdSize = packInfos.size();

        LogUtil.d(TAG, String.format("totalSize = %d firstSize = %d secondsize = %d thirdsize = %d", taotalSize, firstSize, secondSize, thirdSize));
        String curPkgName = mContext.getPackageName();

        List<PackageInfo> firstPart = packInfos.subList(0, firstSize);
        List<PackageInfo> secondPart = packInfos.subList(firstSize, secondSize);
        List<PackageInfo> thirdPart = packInfos.subList(secondSize, packInfos.size());
        GlobalDataManager.getInstance().getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                List<AppInfo> appInfoList = getAppInfoList(firstPart, curPkgName);
                Message message = new Message();
                message.obj = appInfoList;
                message.what = LOAD_APP_MESSAGE;
                mHandler.sendMessage(message);
            }
        });

        GlobalDataManager.getInstance().getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                List<AppInfo> appInfoList = getAppInfoList(secondPart, curPkgName);
                Message message = new Message();
                message.obj = appInfoList;
                message.what = LOAD_APP_MESSAGE;
                mHandler.sendMessage(message);

            }
        });

        GlobalDataManager.getInstance().getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                List<AppInfo> appInfoList = getAppInfoList(thirdPart, curPkgName);
                Message message = new Message();
                message.obj = appInfoList;
                message.what = LOAD_APP_MESSAGE;
                mHandler.sendMessage(message);

            }
        });


//        Observable.just(firstPart, secondPart, thirdPart).subscribeOn(Schedulers.newThread()).
//                flatMap(packageInfoList -> {
//                    List<AppInfo> appInfoList = getAppInfoList(packageInfoList, curPkgName);
//                    return Observable.just(appInfoList);
//                }).observeOn(AndroidSchedulers.mainThread()).subscribe(appInfos -> {
//            for (AppInfo appInfo : appInfos) {
//                if (appInfo.isUserApp()) {
//                    userAppInfos.add(appInfo);
//                } else {
//                    systemAppInfos.add(appInfo);
//                }
//
//                LogUtil.d(TAG, "App name = " + appInfo.getAppName());
//
//            }
//
//
//            LogUtil.e(TAG, "load part ");
//
//        }, throwable -> {
//            LogUtil.e(TAG, "load all app failed");
//        }, () -> {
//            LogUtil.e(TAG, "load all app complete");
//
//            for (LoadStateCallback loadStateCallbackItem : loadStateCallbackList) {
//                if (loadStateCallbackItem != null) {
//                    loadStateCallbackItem.loadFinish();
//                }
//            }
//        });


    }

    @NonNull
    private List<AppInfo> getAppInfoList(List<PackageInfo> packageInfoList, String curPkgName) {
        List<AppInfo> appInfoList = new ArrayList<>();
        for (PackageInfo packInfo : packageInfoList) {
            if (packInfo.applicationInfo.sourceDir == null) {
                continue;
            }
            appInfoList.add(getAppInfo(packInfo));
        }
        return appInfoList;
    }


    private AppInfo getAppInfo(PackageInfo packInfo) {
        PackageManager pm = mContext.getPackageManager();
        final AppInfo appInfo = new AppInfo();
        Drawable appIcon = packInfo.applicationInfo.loadIcon(mContext.getPackageManager());
        ApplicationInfo info = packInfo.applicationInfo;
        appInfo.setAppIcon(appIcon);
        String sourceDir = info.sourceDir;
        appInfo.setCodePath(sourceDir);
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
        return appInfo;

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


    public synchronized boolean isComponentEnable(String pkgName, String component) {
        if (TextUtils.isEmpty(pkgName) || TextUtils.isEmpty(component)) {
            return false;
        }
        ComponentName componentName = new ComponentName(pkgName, component);
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
    public void loadAllComponent(LoadAllComponentListener loadAllComponentListener) {
        if (isComponentLoaded) {
            return;
        }
        this.mLoadAllComponentListener = loadAllComponentListener;
        List<AppInfo> allApp = getAllApp();
        int taotalSize = allApp.size();
        int firstSize = allApp.size() / 4;
        int secondSize = allApp.size() / 4 * 2;
        int thirdSize = allApp.size() / 4 * 2;
        int fourSize = allApp.size();


        LogUtil.d(TAG, String.format("totalSize = %d firstSize = %d secondsize = %d thirdsize = %d", taotalSize, firstSize, secondSize, thirdSize));

        List<AppInfo> firstPart = allApp.subList(0, firstSize);
        List<AppInfo> secondPart = allApp.subList(firstSize, secondSize);
        List<AppInfo> thirdPart = allApp.subList(secondSize, allApp.size());
        List<AppInfo> fourPart = allApp.subList(thirdSize, fourSize);
        ScheduledExecutorService threadPool = GlobalDataManager.getInstance().getThreadPool();
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                List<AppInfo> appInfoListResult = parseComponent(firstPart);
                mHandler.sendEmptyMessage(LOAD_COMPONENT_MESSAGE);
            }
        });
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                List<AppInfo> appInfoListResult = parseComponent(secondPart);
                mHandler.sendEmptyMessage(LOAD_COMPONENT_MESSAGE);


            }
        });


        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                List<AppInfo> appInfoListResult = parseComponent(thirdPart);
                mHandler.sendEmptyMessage(LOAD_COMPONENT_MESSAGE);
            }
        });


        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                List<AppInfo> appInfoListResult = parseComponent(fourPart);
                mHandler.sendEmptyMessage(LOAD_COMPONENT_MESSAGE);
            }
        });
    }


    private List<AppInfo> parseComponent(List<AppInfo> appInfoList) {
        for (int i = 0; i < appInfoList.size(); i++) {
            AppInfo appInfo = appInfoList.get(i);
            LogUtil.d(TAG, "loadAllComponent=>" + appInfo.getPackname());
            XmlUtil xmlUtil = new XmlUtil();
            xmlUtil.parseAppInfo(mContext, appInfo.getPackname(), appInfo);
        }
        return appInfoList;

    }


    public void disableComponent(String component) {

        GlobalDataManager.getInstance().getThreadPool().execute(() -> {
            String replaceComponent = component.replace("$", "\"" + "$" + "\"");
            ShellUtils.CommandResult commandResult = ShellUtils.execCommand("pm disable " + replaceComponent, true);
            LogUtil.d(TAG, "enableComponent = " + commandResult.toString());

        });
    }

    public void enableComponent(String component) {
        GlobalDataManager.getInstance().getThreadPool().execute(() -> {
            String replaceComponent = component.replace("$", "\"" + "$" + "\"");
            ShellUtils.CommandResult commandResult = ShellUtils.execCommand("pm enable " + replaceComponent, true);
            LogUtil.d(TAG, "enableComponent = " + commandResult.toString());
        });
    }


    public void enableAndRemoveComponent(ComponentEntity componentEntity) {
        componentEntity.setEnable(true);
        enableComponent(componentEntity.getFullPathName());
        GlobalDataManager.getInstance().getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                DBSoftUtil.remove(componentEntity);
            }
        });
    }

    public void disableAndSaveComponent(ComponentEntity componentEntity) {
        componentEntity.setEnable(false);
        disableComponent(componentEntity.getFullPathName());
        GlobalDataManager.getInstance().getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                DBSoftUtil.save(componentEntity);
            }
        });
    }


    public String getMetadata(String channel) {
        ApplicationInfo appInfo = null;
        try {
            appInfo = mContext.getPackageManager()
                    .getApplicationInfo(mContext.getPackageName(),
                            PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String msg = appInfo.metaData.getString(channel);
        Log.d(TAG, " msg == " + msg);
        return msg;
    }


    public boolean isComponentLoaded() {
        return isComponentLoaded;
    }


    public void loadBackup() {
        File file = new File(Terminal.SYSTEM_BACKUP_PATH);
        if (file.exists()) {
            File[] files = file.listFiles();
            for (int i = 0; i < files.length; i++) {
                AppInfo appInfo = new AppInfo();
                appInfo.setBackupPath(files[i].getPath());
                Terminal.fillApkModel(appInfo);
                backupList.add(appInfo);
            }

        }

    }

    public AppInfo getAppInfo(String pkgName) {
        for (int i = 0; i < getAllApp().size(); i++) {
            AppInfo appInfo = getAllApp().get(i);
            if (appInfo.getPackname().equals(pkgName)) {
                return appInfo;
            }

        }
        return null;
    }

    public List<AppInfo> getBackupList() {
        return backupList;
    }

    public ComponentEntity getAppComponent(String belongPkg, String componentName) {
        AppInfo appInfo = getAppInfo(belongPkg);
        if (appInfo == null) {
            return null;
        }
        return appInfo.getComponent(componentName);
    }
}
