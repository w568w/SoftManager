package cn.ifreedomer.com.softmanager.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.util.DisplayMetrics;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;

import cn.ifreedomer.com.softmanager.CleanApplication;
import cn.ifreedomer.com.softmanager.R;
import cn.ifreedomer.com.softmanager.model.AppInfo;
import cn.ifreedomer.com.softmanager.model.AppItem;
import cn.ifreedomer.com.softmanager.model.ReceiverInfo;


public class Terminal {

    private static final String TAG = "Terminal";
    private static String[] SU_OPTIONS = {"/system/bin/su", "/system/xbin/su", "/data/bin/su"};

    public static boolean hasRootPermission() {
        try {
            for (String p : SU_OPTIONS) {
                File su = new File(p);
                if (su.exists()) {
                    LogUtil.i(TAG, "su found at: " + p);
                    return true;
                } else {
                    LogUtil.e(TAG, "No su in: " + p);
                }
            }
            LogUtil.i(TAG, "No su found in a well-known location, " + "will just use \"su\".");
        } catch (Exception e) {
            LogUtil.e(TAG, "Can't obtain root - Here is what I know: " + e.getMessage());
        }
        return false;
    }

    public static boolean haveRoot(Context context) {
        // 通过执行测试命令来检测
        int i = RootCommand("chmod 777 " + context.getPackageCodePath());
        LogUtil.i("debug", "root result " + i);
        if (i == 0 || i == 10)
            return true;
        return false;
    }

    /**
     * 请求root权限
     *
     * @param mContext
     * @return
     */
    public static boolean grantRoot(Context mContext) {
        long currenttime = System.currentTimeMillis();
        RootCommand("mount -o remount,rw /system; echo 'a' > /system/xbin/" + currenttime + ";");
        RootCommand("PATH='/system/bin';'mount' '-o' 'remount,rw' '' '/system'; echo 'a' > /system/xbin/" + currenttime + ";");
        if (new File("/system/xbin/" + currenttime) != null && new File("/system/xbin/" + currenttime).exists()
                && new File("/system/xbin/" + currenttime).length() > 0) {
            RootCommand("PATH='/system/bin';'mount' '-o' 'remount,rw' '' '/system';rm /system/xbin/" + currenttime);
            RootCommand("mount -o remount,rw /system;rm /system/xbin/" + currenttime);
            saveRoot(mContext, true);
            LogUtil.i("debug", "grantRoot " + true);
            DashiSmartStore_RootUtils.isroot = true;
            return true;
        } else {
            LogUtil.i("debug", "grantRoot " + false);
            DashiSmartStore_RootUtils.isroot = false;
            saveRoot(mContext, false);
            return false;
        }
    }

    /**
     * 判断是否有su
     *
     * @return
     */
    public static boolean haveSu() {
        File file = new File("/system/bin/su");
        if (file.exists()) {
            LogUtil.i("debug", "haveSu  bin " + true);
            return true;
        }
        file = new File("/system/xbin/su");
        if (file.exists()) {
            LogUtil.i("debug", "haveSu xbin " + true);
            return true;
        }
        LogUtil.i("debug", "haveSu " + false);
        return false;
    }

    public static boolean isRoot(Context mContext) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
        return sp.getBoolean("isRoot", false);
    }

    public static void saveRoot(Context mContext, boolean b) {
        SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
        sp.edit().putBoolean("isRoot", b);
    }

    /**
     * 执行脚本
     *
     * @param command
     * @return
     */
    public static int RootCommand(String command) {
        if (command != null) {
            LogUtil.i("debug", "RootCommand :" + command);
        }
        Process process = null;
        DataOutputStream os = null;
        try {
            process = Runtime.getRuntime().exec("su");
            os = new DataOutputStream(process.getOutputStream());
            os.writeBytes(command + "\n");
            os.writeBytes("exit\n");
            os.flush();
            process.waitFor();
        } catch (Exception e) {
            LogUtil.d("*** DEBUG ***", "ROOT REE" + e.getMessage());
            return -1;
        } finally {
            try {
                if (os != null) {
                    os.close();
                }
                process.destroy();
            } catch (Exception e) {
            }
        }
        LogUtil.d("*** DEBUG ***", "Root SUC  code :" + process.exitValue());
        return process.exitValue();
    }

    /**
     * 把字符串写到文件里
     *
     * @param filename 文件路径
     * @param str      字符串
     */
    public static final void writeToFile(final String filename, final String str) {
        File outFile = new File(filename);
        File pathFile = outFile.getParentFile();
        if (!pathFile.exists()) {
            pathFile.mkdirs();
        }
        FileOutputStream os;
        try {
            os = new FileOutputStream(outFile);
            os.write(str.getBytes());
            os.flush();
            os.close();

            LogUtil.i(TAG, "------写文件成功------" + filename);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean uninstallSystemApp(AppInfo appItem) {
        Terminal.RootCommand("mount -o remount rw /system");
        Terminal.RootCommand("PATH='" + getSuCommand() + "';'mount' '-o' 'remount,rw' '' '/system'");
        String command = "rm " + appItem.getCodePath() + "\n";
        command += "pm uninstall " + appItem.getPackname() + "\n";
        int code = Terminal.RootCommand(command);
        return code == 0;
    }


    public static String getSuCommand() {
        String suCommand = "";
        if (new File("/system/bin/su").exists()) {
            suCommand = "/system/bin/su";
        } else if (new File("/system/xbin/su").exists()) {
            suCommand = "/system/xbin/su";
        } else if (new File("/data/bin/su").exists()) {
            suCommand = "/data/bin/su";
        } else if (new File("/sbin/su").exists()) {
            suCommand = "/sbin/su";
        }
        return suCommand;

    }

    public static boolean uninstallUserApp(AppItem appItem) {
        String command = "pm uninstall " + appItem.getAppPackage();
        int code = Terminal.RootCommand(command);
        return code == 0;
    }

    public static boolean removeFile(String filePath) {
        Terminal.RootCommand("mount -o remount rw /system");
        Terminal.RootCommand("PATH='/system/bin';'mount' '-o' 'remount,rw' '' '/system'");
        int code = Terminal.RootCommand("rm " + filePath);
        return code == 0;
    }

    public static boolean copyFile(String filePath, String targeFilePath) {
        Terminal.RootCommand("mount -o remount rw /system");
        Terminal.RootCommand("PATH='/system/bin';'mount' '-o' 'remount,rw' '' '/system'");
        int code = Terminal.RootCommand("cat " + filePath + " > " + targeFilePath);
        return code == 0;
    }

    public static boolean installApp(String apkPath) {
        String command = "pm install " + apkPath;
        int code = Terminal.RootCommand(command);
        return code == 0;

    }

    public static boolean uninstallApp(String packageName) {
        String command = "pm uninstall " + packageName;
        int code = Terminal.RootCommand(command);
        return code == 0 ? true : false;

    }

    public static boolean enableApp(String packageName) {
        int code = Terminal.RootCommand("pm enable " + packageName + "\n");
        return code == 0 ? true : false;
    }

    public static boolean disableApp(String packageName) {
        int code = 0;
        if (Build.VERSION.SDK_INT >= 14) {
            code = Terminal.RootCommand("pm disable-user " + packageName + "\n");
        } else {
            code = Terminal.RootCommand("pm disable " + packageName + "\n");
        }
        return code == 0 ? true : false;
    }

    public static boolean doChangeComponent(boolean enable, String packageName, String className) {
        String command = null;
        int code = 0;
        if (enable) {
            command = "pm disable \'" + packageName + "/" + className + "\'";
        } else {
            command = "pm enable \'" + packageName + "/" + className + "\'";
        }
        code = RootCommand(command);
        return code == 0 ? true : false;
    }

    public static boolean doChangeComponent(boolean enable, String packageName, ArrayList<ReceiverInfo> receiverInfos) {
        String command = "";
        int code = 0;
        if (enable) {
            for (ReceiverInfo receiverInfo : receiverInfos) {
                command += "pm disable \'" + packageName + "/" + receiverInfo.getClassName() + "\'\n";
            }
        } else {
            for (ReceiverInfo receiverInfo : receiverInfos) {
                command += "pm enable \'" + packageName + "/" + receiverInfo.getClassName() + "\'\n";
            }
        }
        code = RootCommand(command);
        return code == 0 ? true : false;
    }


    public final static String SYSTEM_BACKUP_PATH = Environment.getExternalStorageDirectory().getPath() + "/systemAppBackup/";


    /**
     * 备份系统应用
     *
     * @param appinfoModel
     * @return true:备份成功
     */
    public static boolean backupApp(AppInfo appinfoModel) {

        String apkName = appinfoModel.getCodePath().substring(appinfoModel.getCodePath().lastIndexOf("/") + 1, appinfoModel.getCodePath().length());
        File fileDir = new File(SYSTEM_BACKUP_PATH);
        if (!fileDir.exists()) {
            fileDir.mkdirs();
        }
        if (!apkName.endsWith(".apk")) {
            apkName += ".apk";
        }
        apkName = SYSTEM_BACKUP_PATH + apkName;
        LogUtil.d(TAG, "apkName = " + apkName + "   getCodePath=" + appinfoModel.getCodePath());
        try {
            FileInputStream fileInputStream = new FileInputStream(new File(appinfoModel.getCodePath()));
            FileUtil.inputStream2File(fileInputStream, new File(apkName));
            appinfoModel.setBackupPath(apkName);
            return true;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return false;
        }

    }


    /**
     * 还原系统应用
     *
     * @param appItem
     * @return true:备份成功
     */
    public static boolean restoreApp(AppInfo appItem) {
        String apkName = appItem.getBackupPath().substring(appItem.getBackupPath().lastIndexOf("/") + 1, appItem.getBackupPath().length());
        Terminal.RootCommand("mount -o remount rw /system");
        Terminal.RootCommand("PATH='" + getSuCommand() + "';'mount' '-o' 'remount,rw' '' '/system'");
        int result = 0;
        String odexName = apkName.replace(".apk", ".odex");
        if (new File(appItem.getBackupPath()).exists()) {
            String cmd = "touch /system/app/" + odexName + "\n";
            cmd += "chmod 0644 /system/app/" + odexName + "\n";
            cmd += "cat " + appItem.getBackupPath() + " > " + "/system/app/" + odexName;
            result = Terminal.RootCommand(cmd);
        }
        String cmd = "touch /system/app/" + apkName + "\n";
        cmd += "chmod 0644 /system/app/" + apkName + "\n";
        LogUtil.d(TAG, "file exist = " + new File(appItem.getBackupPath()).exists());
        cmd += "cat " + "/sdcard/systemAppBackup/" + apkName  + "  > " + "/system/app/" + apkName;
        ShellUtils.CommandResult commandResult = ShellUtils.execCommand(cmd, true);
        LogUtil.d(TAG, "resotre result = " + commandResult.toString());
        return commandResult.result == 0;
    }


    /**
     * 获取未安装的apk信息
     *
     * @return
     */
    public static boolean fillApkModel(AppInfo info) {
        Context ctx = CleanApplication.INSTANCE;
        String filepath = info.getBackupPath();
        File file = new File(filepath);
        if (file == null || !file.exists()) {
            return false;
        }
        info.setCodePath(filepath);
        info.setCacheSize(file.length());
        String PATH_PackageParser = "android.content.pm.PackageParser";
        String PATH_AssetManager = "android.content.res.AssetManager";
        try {
            // 反射得到pkgParserCls对象并实例化,有参数
            Class<?> pkgParserCls = Class.forName(PATH_PackageParser);
            Class<?>[] typeArgs = null;
            Object[] valueArgs = null;
            Object pkgParser = null;
            try {
                typeArgs = new Class<?>[]{String.class};
                Constructor<?> pkgParserCt = pkgParserCls.getConstructor(typeArgs);
                valueArgs = new Object[]{filepath};
                pkgParser = pkgParserCt.newInstance(valueArgs);
            } catch (Exception e1) {
                Constructor<?> pkgParserCt = pkgParserCls.getConstructor();
                pkgParser = pkgParserCt.newInstance();
            }
            if (pkgParser == null) {
                return false;
            }

            // 从pkgParserCls类得到parsePackage方法
            Object pkgParserPkg = null;
            try {
                DisplayMetrics metrics = new DisplayMetrics();
                metrics.setToDefaults();// 这个是与显示有关的, 这边使用默认
                typeArgs = new Class<?>[]{File.class, String.class, DisplayMetrics.class, int.class};
                Method pkgParser_parsePackageMtd = pkgParserCls.getDeclaredMethod("parsePackage", typeArgs);
                valueArgs = new Object[]{new File(filepath), filepath, metrics, 0};
                // 执行pkgParser_parsePackageMtd方法并返回
                pkgParserPkg = pkgParser_parsePackageMtd.invoke(pkgParser, valueArgs);
            } catch (Exception e1) {
                typeArgs = new Class<?>[]{File.class, int.class};
                Method pkgParser_parsePackageMtd = pkgParserCls.getDeclaredMethod("parsePackage", typeArgs);
                valueArgs = new Object[]{new File(filepath), 0};
                // 执行pkgParser_parsePackageMtd方法并返回
                pkgParserPkg = pkgParser_parsePackageMtd.invoke(pkgParser, valueArgs);
            }

            // 从返回的对象得到名为"applicationInfo"的字段对象
            if (pkgParserPkg == null) {
                return false;
            }
            Field appInfoFld = pkgParserPkg.getClass().getDeclaredField("applicationInfo");

            // 从对象"pkgParserPkg"得到字段"appInfoFld"的值
            if (appInfoFld.get(pkgParserPkg) == null) {
                return false;
            }
            ApplicationInfo applicationInfo = (ApplicationInfo) appInfoFld.get(pkgParserPkg);

            // 反射得到assetMagCls对象并实例化,无参
            Class<?> assetMagCls = Class.forName(PATH_AssetManager);
            Object assetMag = assetMagCls.newInstance();
            // 从assetMagCls类得到addAssetPath方法
            typeArgs = new Class[1];
            typeArgs[0] = String.class;
            Method assetMag_addAssetPathMtd = assetMagCls.getDeclaredMethod("addAssetPath", typeArgs);
            valueArgs = new Object[1];
            valueArgs[0] = filepath;
            // 执行assetMag_addAssetPathMtd方法
            assetMag_addAssetPathMtd.invoke(assetMag, valueArgs);

            // 得到Resources对象并实例化,有参数
            Resources res = CleanApplication.INSTANCE.getResources();
            typeArgs = new Class[3];
            typeArgs[0] = assetMag.getClass();
            typeArgs[1] = res.getDisplayMetrics().getClass();
            typeArgs[2] = res.getConfiguration().getClass();
            Constructor<Resources> resCt = Resources.class.getConstructor(typeArgs);
            valueArgs = new Object[3];
            valueArgs[0] = assetMag;
            valueArgs[1] = res.getDisplayMetrics();
            valueArgs[2] = res.getConfiguration();
            res = (Resources) resCt.newInstance(valueArgs);

            // 读取apk文件的信息
            if (applicationInfo != null) {
                if (applicationInfo.icon != 0) {// 图片存在，则读取相关信息
                    Drawable icon = res.getDrawable(applicationInfo.icon);// 图标
                    info.setAppIcon(icon);
                }
                if (applicationInfo.labelRes != 0) {
                    String neme = (String) res.getText(applicationInfo.labelRes);// 名字
                    info.setAppName(neme);
                } else {
                    String apkName = file.getName();
                    info.setAppName(apkName.substring(0, apkName.lastIndexOf(".")));
                }
                String pkgName = applicationInfo.packageName;// 包名
                info.setPackname(pkgName);
            } else {
                return false;
            }
            PackageManager pm = ctx.getPackageManager();
            PackageInfo packageInfo = pm.getPackageArchiveInfo(filepath, PackageManager.GET_ACTIVITIES);
            if (packageInfo != null) {
                info.setVersion(packageInfo.versionName);// 版本号
                info.setVersionCode(packageInfo.versionCode);// 版本码
            }
            PackageManager packageManager = ctx.getPackageManager();
            PackageInfo mPackageInfo = null;
            try {
                mPackageInfo = packageManager.getPackageInfo(info.getPackname(), 0);
            } catch (PackageManager.NameNotFoundException e) {
            }
            if (mPackageInfo != null) {
                if (info.getVersionCode() > mPackageInfo.versionCode) {
                    info.setVersion(ctx.getString(R.string.app_install_version_hight_tip));
                } else {
                    return false;
                }
            } else {
                info.setVersion(ctx.getString(R.string.app_not_install_tip));
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }


}
