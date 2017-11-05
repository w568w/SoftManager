package cn.ifreedomer.com.softmanager.util;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;

public class Terminal2 {
	private Process process = null;
	private DataOutputStream os = null;
	private Context context;

	public Terminal2(Context context) throws IOException {
		this.context = context;
		process = Runtime.getRuntime().exec("su");
		os = new DataOutputStream(process.getOutputStream());
	}

	public void RootCommand(String command) {
		if (command != null) {
			Log.i("debug", "RootCommand :" + command);
		}
		try {
			os.write((command + "\n").getBytes("ASCII"));
			os.flush();
			os.write(("exit\n").getBytes("ASCII"));
			os.flush();
			process.waitFor();
			process.exitValue();
		} catch (Exception e) {
			Log.d("*** DEBUG ***", "ROOT REE" + e.getMessage());
		}
	}

	/**
	 * 通过app_process去运行jar包
	 * @param method
	 * @param value
	 */
	public void runCommand(final String method, final String value) {
		StringBuilder sb = new StringBuilder();
		sb.append("export CLASSPATH=" + context.getFilesDir() + "/RemoteTools.jar" + "\n");
		if (Build.VERSION.SDK_INT >= 14)
			sb.append("LD_LIBRARY_PATH=/vendor/lib:/system/lib ");
		sb.append("exec app_process ").append(context.getFilesDir().getAbsolutePath()).append(" cn.opda.remote.tools.RemoteTools");
		sb.append(" " + method + " " + value + "\n");
		RootCommand(sb.toString());
	}

	/**
	 * 通过app_process去运行jar包,2个参数的
	 * @param method
	 * @param value
	 * @param value2
	 */
	public void runCommand(final String method, final String value, final String value2) {
		StringBuilder sb = new StringBuilder();
		sb.append("export CLASSPATH=" + context.getFilesDir() + "/RemoteTools.jar" + "\n");
		if (Build.VERSION.SDK_INT >= 14)
			sb.append("LD_LIBRARY_PATH=/vendor/lib:/system/lib ");
		sb.append("exec app_process ").append(context.getFilesDir().getAbsolutePath()).append(" cn.opda.remote.tools.RemoteTools");
		sb.append(" " + method + " " + value + " " + value2 + "\n");
		RootCommand(sb.toString());
	}
}
