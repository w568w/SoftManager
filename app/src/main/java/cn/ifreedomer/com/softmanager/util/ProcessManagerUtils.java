package cn.ifreedomer.com.softmanager.util;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Debug;
import android.os.Process;
import android.text.TextUtils;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import cn.ifreedomer.com.softmanager.bean.clean.ClearResult;
import cn.ifreedomer.com.softmanager.bean.clean.ProcessItem;

public class ProcessManagerUtils {

    /**
     * 获得手机运行时可用内存
     *
     * @param context
     * @return
     */
    public static long getRuntimeAvailableMemory(Context context) {
        MemoryInfo memoryInfo = new MemoryInfo();
        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        activityManager.getMemoryInfo(memoryInfo);

        return memoryInfo.availMem;
    }

    /**
     * 获得手机运行总内存
     *
     * @return 手机总内存
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

    public static String formatsize(long size) {
        String str = "";
        if (size < 1024) {
            str = "B";
        }
        if (size >= 1024) {
            str = "KB";
            size /= 1024;
            if (size >= 1024) {
                str = "MB";
                size /= 1024;
                if (size >= 1024) {
                    str = "GB";
                    size /= 1024;
                }
            }
        }
        DecimalFormat formatter = new DecimalFormat("#0");
        String newstr = formatter.format(size) + str;
        return newstr;
    }

    /**
     * 结束所有用户程序
     *
     * @param context
     * @return
     */
    @SuppressWarnings("deprecation")
    public static ClearResult killProcessList(final Context context) {
        ArrayList<String> killPackages = new ArrayList<String>();
        ArrayList<Integer> runPids = new ArrayList<Integer>();
        ClearResult clearResult = new ClearResult();
        long memory = 0;
        int count = 0;
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        PackageManager pm = context.getPackageManager();
        List<ApplicationInfo> installedAppList = pm.getInstalledApplications(0);
        List<RunningAppProcessInfo> lists = am.getRunningAppProcesses();
        List<RunningServiceInfo> serviceList = am.getRunningServices(100);
        String packages = "";
        String killCommand = "";
        ArrayList<String> keeps = KeepListUtils.getList(context);
        if (lists != null) {
            for (RunningAppProcessInfo runapp : lists) {
                if (runapp.pkgList == null || runapp.pkgList.length == 0) {
                    continue;
                }
                String packageName = runapp.pkgList[0];
                if (packageName.equals(context.getPackageName())) {
                    continue;
                }
                if (!runPids.contains(runapp.pid)) {
                    runPids.add(runapp.pid);
                }
                for (ApplicationInfo info : installedAppList) {
                    String sourceDir = info.sourceDir;
                    if (sourceDir != null) {
                        if (packageName.equals(info.packageName)) {
                            if (!keeps.contains(packageName)) {
                                if (!killPackages.contains(packageName)) {
                                    count++;
                                    memory += ProcessManagerUtils.getProcessMemUsage(am, runapp.pid) * 1000;
                                    if (Terminal.isRoot(context)) {
                                        if (packages.length() == 0) {
                                            packages += packageName;
                                        } else {
                                            packages += ("," + packageName);
                                        }
                                        if (runapp.pid != 0) {
                                            killCommand += "kill " + runapp.pid + "\n";
                                        }
                                    } else {
                                        am.restartPackage(packageName);
                                        if (runapp.pid != 0) {
                                            Process.killProcess(runapp.pid);
                                        }
                                        if (Build.VERSION.SDK_INT >= 8) {
                                            am.killBackgroundProcesses(packageName);
                                        }
                                    }
                                    killPackages.add(packageName);
                                }

                            }
                        }
                    }
                }
            }
        }
        if (serviceList != null) {
            for (RunningServiceInfo runService : serviceList) {
                if (runService.service == null) {
                    continue;
                }
                String packageName = runService.service.getPackageName();
                if (TextUtils.isEmpty(packageName)) {
                    continue;
                }
                if (packageName.equals(context.getPackageName())) {
                    continue;
                }
                if (!runPids.contains(runService.pid)) {
                    runPids.add(runService.pid);
                }
                for (ApplicationInfo info : installedAppList) {
                    String sourceDir = info.sourceDir;
                    if (sourceDir != null) {
                        if (packageName.equals(info.packageName)) {
                            if (!keeps.contains(packageName)) {
                                if (!killPackages.contains(packageName)) {
                                    count++;
                                    memory += ProcessManagerUtils.getProcessMemUsage(am, runService.pid) * 1000;
                                    if (Terminal.isRoot(context)) {
                                        if (packages.length() == 0) {
                                            packages += packageName;
                                        } else {
                                            packages += ("," + packageName);
                                        }
                                        if (runService.pid != 0) {
                                            killCommand += "kill " + runService.pid + "\n";
                                        }
                                    } else {
                                        am.restartPackage(packageName);
                                        if (Build.VERSION.SDK_INT >= 8) {
                                            am.killBackgroundProcesses(packageName);
                                        }
                                        if (runService.pid != 0) {
                                            Process.killProcess(runService.pid);
                                        }
                                    }
                                    killPackages.add(packageName);
                                }
                            }
                        }
                    }
                }
            }
        }
        final String packs = packages;
        final String command = killCommand;
        if (packs.length() > 0) {
            new Thread(new Runnable() {

                @Override
                public void run() {
//                    AppManagerUtils.forceStopPackage(context, packs);
//					Terminal.RootCommand(command);
                }
            }).start();
        }
        clearResult.setCount(count);
        clearResult.setMemory(memory);
        return clearResult;
    }

    public static int getProcessMemUsage(ActivityManager am, int pid) {
        Debug.MemoryInfo[] memInfo = am.getProcessMemoryInfo(new int[]{pid});
        return memInfo[0].getTotalPss();
    }

    /**
     * 结束某个进程
     *
     * @param mContext
     * @param appItem
     */
    @SuppressWarnings("deprecation")
    public static void killProcess(Context mContext, ProcessItem appItem) {
        if (Terminal.isRoot(mContext)) {
			Terminal.RootCommand("kill " + appItem.getPid());
//            AppManagerUtils.forceStopPackage(mContext, appItem.getPkgName());
        }
        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        am.restartPackage(appItem.getPkgName());
        if (Build.VERSION.SDK_INT >= 8) {
            am.killBackgroundProcesses(appItem.getPkgName());
        }
//		if (appItem.getPid() != 0) {
//			Process.killProcess(appItem.getPid());
//		}
    }

    /**
     * 获取已用的运行时内存
     *
     * @param mContext
     * @return
     */
    public static long getUsedMemory(Context mContext) {
        long size = 0;
        ActivityManager am = (ActivityManager) mContext.getSystemService(Context.ACTIVITY_SERVICE);
        PackageManager pm = mContext.getPackageManager();
        List<ApplicationInfo> installedAppList = pm.getInstalledApplications(0);
        List<RunningAppProcessInfo> lists = am.getRunningAppProcesses();
        ArrayList<String> keeps = KeepListUtils.getList(mContext);
        for (int i = 0; i < lists.size(); i++) {
            RunningAppProcessInfo runapp = lists.get(i);
            if (runapp.pkgList == null || runapp.pkgList.length == 0) {
                continue;
            }
            String packageName = runapp.pkgList[0];
            if (packageName.equals(mContext.getPackageName())) {
                continue;
            }
            for (ApplicationInfo info : installedAppList) {
                String sourceDir = info.sourceDir;
                if (sourceDir != null) {
                    if (packageName.equals(info.packageName)) {
                        if (!keeps.contains(packageName)) {
                            ProcessItem appItem = new ProcessItem();
                            appItem.setPkgName(packageName);
                            appItem.setMemorySize(ProcessManagerUtils.getProcessMemUsage(am, runapp.pid) * 1000);
                            size += appItem.getMemorySize();
                        }
                    }
                }
            }
        }
        return size;
    }

}
