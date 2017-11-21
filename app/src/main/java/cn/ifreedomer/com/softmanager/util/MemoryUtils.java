package cn.ifreedomer.com.softmanager.util;

import android.content.Context;
import android.content.pm.IPackageDataObserver;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.os.RemoteException;
import android.os.StatFs;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

public class MemoryUtils {

    /**
     * 清除全部的缓存
     *
     * @param mContext
     */
    public static void clearAllCache(Context mContext) {
        PackageManager pm = mContext.getPackageManager();
        Method localMethod;
        try {
            localMethod = pm.getClass().getMethod("freeStorageAndNotify", Long.TYPE, IPackageDataObserver.class);
            localMethod.invoke(pm, getTotalInternalMemorySize(), new IPackageDataObserver.Stub() {
                @Override
                public void onRemoveCompleted(String packageName, boolean succeeded) throws RemoteException {
                }
            });
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }

    }

    /**
     * 获取sdcard1的目录
     *
     * @return
     */
    public static String getSdcard0() {
        return Environment.getExternalStorageDirectory().getAbsolutePath();
    }

    /**
     * 获取sdcard2的目录
     *
     * @return
     */
    public static String getSdcard1() {
        Map<String, String> map = System.getenv();
        if (map != null) {
//			Set<Map.Entry<String, String>> set = map.entrySet();
//			for (Iterator<Map.Entry<String, String>> it = set.iterator(); it.hasNext();) {
//				Map.Entry<String, String> entry = (Map.Entry<String, String>) it.next();
//				Log.i("debug", entry.getKey() + "--->" + entry.getValue());
//			}
            String SECONDARY_STORAGE = map.get("SECONDARY_STORAGE");
            if (SECONDARY_STORAGE == null) {
                return null;
            }
            if (SECONDARY_STORAGE.contains(":")) {
                String[] sdcards = SECONDARY_STORAGE.split(":");
                if (sdcards != null && sdcards.length > 0) {
                    return sdcards[0];
                }
            } else {
                return SECONDARY_STORAGE;
            }
        }
        return null;
    }

    /**
     * 获取某个目录的可用存储空间大小
     *
     * @param path
     * @return
     */
    public static long getFileAvailableSize(String path) {
        try {
            StatFs stat = new StatFs(path);
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            return availableBlocks * blockSize;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 获取某个目录的最大存储空间
     *
     * @param path
     * @return
     */
    public static long getFileTotalSize(String path) {
        try {
            StatFs stat = new StatFs(path);
            long blockSize = stat.getBlockSize();
            long totalBlocks = stat.getBlockCount();
            return totalBlocks * blockSize;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * 判断SD卡是否挂载
     *
     * @return
     */
    public static boolean externalMounted() {
        return Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState());
    }


    /**
     * 获取ROM可用内存
     *
     * @return
     */
    public static long getAvailableInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        return availableBlocks * blockSize;
    }

    /**
     * 获取ROM全部
     *
     * @return
     */
    public static long getTotalInternalMemorySize() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return totalBlocks * blockSize;
    }

    /**
     * 获取SD卡可用内存
     *
     * @return
     */
    public static long getAvailableExternalMemorySize() {
        if (externalMounted()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long availableBlocks = stat.getAvailableBlocks();
            return availableBlocks * blockSize;
        } else {
            return -1;
        }
    }

    /**
     * 获取SD卡全部内存
     *
     * @return
     */
    public static long getTotalExternalMemorySize() {
        if (externalMounted()) {
            File path = Environment.getExternalStorageDirectory();
            StatFs stat = new StatFs(path.getPath());
            long blockSize = stat.getBlockSize();
            long totalBlocks = stat.getBlockCount();
            return totalBlocks * blockSize;
        } else {
            return -1;
        }
    }

    /**
     * 获取system全部内存
     *
     * @return
     */
    public static long getTotalSystemMemorySize() {
        File path = new File("/system");
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return totalBlocks * blockSize;
    }

//    /**
//     * 获取全部的缓存大小
//     *
//     * @param context
//     * @return
//     */
//    public static final long getCacheTotalSize(final Context context) {
//        long totalSize = 0;
//        PackageManager pm = context.getPackageManager();
//        List<PackageInfo> list = pm.getInstalledPackages((PackageManager.GET_UNINSTALLED_PACKAGES));
//        for (PackageInfo packageInfo : list) {
//            ApplicationInfo applicationInfo = packageInfo.applicationInfo;
//            if (applicationInfo.packageName.equals(context.getPackageName())) {
//                continue;
//            }
//            final String sourceDir = applicationInfo.sourceDir;
//            if (sourceDir != null) {
//                final AppItem appItem = new AppItem();
//                appItem.setAppPackage(applicationInfo.packageName);
//                final CountDownLatch countDownLatch = new CountDownLatch(1);
//                try {
//                    Method getPackageSizeInfo = pm.getClass().getMethod("getPackageSizeInfo", String.class, IPackageStatsObserver.class);
//                    getPackageSizeInfo.invoke(pm, appItem.getAppPackage(), new IPackageStatsObserver.Stub() {
//                        public void onGetStatsCompleted(PackageStats pStats, boolean succeeded) throws RemoteException {
//                            if (Build.VERSION.SDK_INT >= 11) {
//                                appItem.setCacheSize(pStats.cacheSize + pStats.externalCacheSize);
//                            } else {
//                                appItem.setCacheSize(pStats.cacheSize);
//                            }
//                            countDownLatch.countDown();
//                        }
//                    });
//                    countDownLatch.await();
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                if (Build.VERSION.SDK_INT > 16) {
//                    if (appItem.getCacheSize() > 0 && appItem.getCacheSize() != 12288) {
//                        totalSize += appItem.getCacheSize();
//                    }
//                } else {
//                    if (appItem.getCacheSize() > 0) {
//                        totalSize += appItem.getCacheSize();
//                    }
//                }
//            }
//        }
//        return totalSize;
//    }

    /**
     * 获得手机运行总内存
     *
     * @return
     */
    public static long getRuntimeTotalMemory() {
        String memInfoFile = "/proc/meminfo";
        long totalMemory = 0;
        String[] arrayInfo;
        String tempString;
        try {
            FileReader memoryInfoReader = new FileReader(memInfoFile);
            BufferedReader bufferedReader = new BufferedReader(memoryInfoReader, 4 * 1024);

            tempString = bufferedReader.readLine();
            arrayInfo = tempString.split("\\s+");

            totalMemory = Long.parseLong(arrayInfo[1]) * 1024;
            bufferedReader.close();
            memoryInfoReader.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return totalMemory;
    }
//
//    /**
//     * 获取残留文件的垃圾大小
//     *
//     * @param mContext
//     * @return
//     */
//    public static long getTotalDepthSize(Context mContext) {
//        long totalDepthSize = 0;
//        new CopyDBUtils(mContext).copyDB();
//        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//            List<ApplicationInfo> applicationList = mContext.getPackageManager().getInstalledApplications(0);
//            ArrayList<ClearItem> clearItems = DBAppFolderUtils.get(mContext, "zh");
//            int tAppCount = applicationList.size();
//            int tSoftCount = clearItems.size();
//
//            for (int i = 0; i < tSoftCount; i++) {
//                if (!new File(Constants.SDCARD_PATH, clearItems.get(i).getFilePath()).exists()) {
//                    continue;
//                }
//                for (int j = 0; j < tAppCount; j++) {
//                    if (!clearItems.get(i).getPackageName().contains(",")) {
//                        if (clearItems.get(i).getPackageName().equals(applicationList.get(j).packageName))
//                            break;
//                    } else {
//                        String[] packages = clearItems.get(i).getPackageName().split(",");
//                        boolean flag = false;
//                        for (String packageName : packages) {
//                            if (packageName.equals(applicationList.get(j).packageName)) {
//                                flag = true;
//                                break;
//                            }
//                        }
//                        if (flag) {
//                            break;
//                        }
//                    }
//                    if (j == tAppCount - 1 && isExistsFile(clearItems.get(i).getFilePath())) {
//                        AppItem appItem = new AppItem();
//                        appItem.setName(clearItems.get(i).getName());
//                        appItem.setFilePath(Constants.SDCARD_PATH + clearItems.get(i).getFilePath());
//                        appItem.setCodeSize(GetFileSizeLongByPath(Constants.SDCARD_PATH + clearItems.get(i).getFilePath()));
//                        appItem.setAppPackage(clearItems.get(i).getPackageName());
//                        totalDepthSize += appItem.getCodeSize();
//                    }
//                }
//            }
//        }
//        return totalDepthSize;
//    }
//

    /**
     * 计算某个目录下所有文件的大小
     *
     * @param path
     * @return
     */
    public static long GetFileSizeLongByPath(final String path) {
        File file = new File(path);
        return getFileSize(file);
    }

    public static long getFileSize(File f) {
        long size = 0;
        if (f != null && f.isDirectory() && f.exists()) {
            File flist[] = f.listFiles();
            if (flist != null) {
                for (int j = 0; j < flist.length; j++) {

                    if (flist[j].isDirectory()) {
                        size = size + getFileSize(flist[j]);
                    } else {
                        size = size + flist[j].length();
                    }
                }

            }
        }
        return size;
    }
}
