package cn.ifreedomer.com.softmanager.util;

import android.Manifest.permission;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.provider.Settings;


import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import cn.ifreedomer.com.softmanager.model.AppInfo;
import cn.ifreedomer.com.softmanager.model.AppItem;
import cn.ifreedomer.com.softmanager.model.ReceiverInfo;


public class Terminal {

	private static final String TAG = "Terminal";
	private static String[] SU_OPTIONS = { "/system/bin/su", "/system/xbin/su", "/data/bin/su" };

	public static boolean hasRootPermission() {
		try {
			for (String p : SU_OPTIONS) {
				File su = new File(p);
				if (su.exists()) {
					L.i(TAG, "su found at: " + p);
					return true;
				} else
					L.e(TAG, "No su in: " + p);
			}
			L.i(TAG, "No su found in a well-known location, " + "will just use \"su\".");
		} catch (Exception e) {
			L.e(TAG, "Can't obtain root - Here is what I know: " + e.getMessage());
		}
		return false;
	}

	public static boolean haveRoot(Context context) {
		int i = RootCommand("chmod 777 " + context.getPackageCodePath()); // 通过执行测试命令来检测
		L.i("debug", "root result " + i);
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
			L.i("debug", "grantRoot " + true);
			DashiSmartStore_RootUtils.isroot = true;
			// VerboseUtils.setRootState(mContext, true);
			return true;
		} else {
			L.i("debug", "grantRoot " + false);
			DashiSmartStore_RootUtils.isroot = false;
			// VerboseUtils.setRootState(mContext, false);
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
			L.i("debug", "haveSu  bin " + true);
			return true;
		}
		file = new File("/system/xbin/su");
		if (file.exists()) {
			L.i("debug", "haveSu xbin " + true);
			return true;
		}
		L.i("debug", "haveSu " + false);
		return false;
	}

	public static boolean isRoot(Context mContext) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
		return sp.getBoolean("isRoot", false);
	}

	public static void saveRoot(Context mContext, boolean b) {
		SharedPreferences sp = PreferenceManager.getDefaultSharedPreferences(mContext);
		sp.edit().putBoolean("isRoot", b).commit();
	}

	/**
	 * 执行脚本
	 * 
	 * @param command
	 * @return
	 */
	public static int RootCommand(String command) {
		if (command != null) {
			L.i("debug", "RootCommand :" + command);
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
			L.d("*** DEBUG ***", "ROOT REE" + e.getMessage());
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
		L.d("*** DEBUG ***", "Root SUC  code :" + process.exitValue());
		return process.exitValue();
	}

	/**
	 * 把字符串写到文件里
	 * 
	 * @param filename
	 *            文件路径
	 * @param str
	 *            字符串
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

			L.i(TAG, "------写文件成功------" + filename);
		} catch (FileNotFoundException e1) {
			e1.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static boolean uninstallSystemApp(AppInfo appItem) {
		Terminal.RootCommand("mount -o remount rw /system");
		Terminal.RootCommand("PATH='/system/bin';'mount' '-o' 'remount,rw' '' '/system'");
		String command = "rm " + appItem.getCodePath() + "\n";
		command += "pm uninstall " + appItem.getPackname() + "\n";
		int code = Terminal.RootCommand(command);
		return code == 0 ? true : false;
	}

	public static boolean uninstallUserApp(AppItem appItem) {
		String command = "pm uninstall " + appItem.getAppPackage();
		int code = Terminal.RootCommand(command);
		return code == 0 ? true : false;
	}

	public static boolean removeFile(String filePath) {
		Terminal.RootCommand("mount -o remount rw /system");
		Terminal.RootCommand("PATH='/system/bin';'mount' '-o' 'remount,rw' '' '/system'");
		int code = Terminal.RootCommand("rm " + filePath);
		return code == 0 ? true : false;
	}

	public static boolean copyFile(String filePath, String targeFilePath) {
		Terminal.RootCommand("mount -o remount rw /system");
		Terminal.RootCommand("PATH='/system/bin';'mount' '-o' 'remount,rw' '' '/system'");
		int code = Terminal.RootCommand("cat " + filePath + " > " + targeFilePath);
		return code == 0 ? true : false;
	}

	public static boolean installApp(String apkPath) {
		String command = "pm install " + apkPath;
		int code = Terminal.RootCommand(command);
		return code == 0 ? true : false;

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
			for(ReceiverInfo receiverInfo : receiverInfos){
				command += "pm disable \'" + packageName+ "/" + receiverInfo.getClassName() + "\'\n";
			}
		} else {
			for(ReceiverInfo receiverInfo : receiverInfos){
				command += "pm enable \'" + packageName+ "/" + receiverInfo.getClassName() + "\'\n";
			}
		}
		code = RootCommand(command);
		return code == 0 ? true : false;
	}

	@SuppressWarnings("deprecation")
	public static boolean setADBEnabledState(ContentResolver cr, boolean enable, Context context) {
		if (context.checkCallingOrSelfPermission(permission.WRITE_SECURE_SETTINGS) == PackageManager.PERMISSION_GRANTED) {
			try {
				return Settings.Secure.putInt(cr, Settings.Secure.ADB_ENABLED, enable ? 1 : 0);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return false;
		} else {
			return RootCommand(String.format("setprop persist.service.adb.enable %s", enable ? 1 : 0)) == 0;
		}

	}

}
