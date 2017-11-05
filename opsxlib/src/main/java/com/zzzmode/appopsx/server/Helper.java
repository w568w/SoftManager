package com.zzzmode.appopsx.server;

import android.app.ActivityThread;
import android.app.AppOpsManager;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.UserHandle;
import com.zzzmode.appopsx.common.ReflectUtils;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zl on 2017/1/15.
 */
class Helper {

  static int getPackageUid(String packageName, int userId) {
    int uid = -1;
    try {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        uid = ActivityThread.getPackageManager().getPackageUid(packageName,PackageManager.MATCH_UNINSTALLED_PACKAGES,userId);
      } else {
        uid = ActivityThread.getPackageManager().getPackageUid(packageName, userId);
      }
    } catch (Throwable e) {
      e.printStackTrace();
    }

    if (uid == -1) {
      try {
        ApplicationInfo applicationInfo = ActivityThread.getPackageManager()
            .getApplicationInfo(packageName, 0, userId);
        List<Class> paramsType = new ArrayList<>(2);
        paramsType.add(int.class);
        paramsType.add(int.class);
        List<Object> params = new ArrayList<>(2);
        params.add(userId);
        params.add(applicationInfo.uid);
        uid = (int) ReflectUtils.invokMethod(UserHandle.class, "getUid", paramsType, params);
      } catch (Throwable e) {
        e.printStackTrace();
      }
    }

    return uid;
  }

  private static Map<String, Integer> sRuntimePermToOp = null;

  static int permissionToCode(String permission) {
    if (sRuntimePermToOp == null) {
      sRuntimePermToOp = new HashMap<>();
      Object sOpPerms = ReflectUtils.getFieldValue(AppOpsManager.class, "sOpPerms");
      Object sOpToSwitch = ReflectUtils.getFieldValue(AppOpsManager.class, "sOpToSwitch");

      if (sOpPerms instanceof String[] && sOpToSwitch instanceof int[]) {
        String[] opPerms = (String[]) sOpPerms;
        int[] opToSwitch = (int[]) sOpToSwitch;

        if (opPerms.length == opToSwitch.length) {
          for (int i = 0; i < opToSwitch.length; i++) {
            if (opPerms[i] != null) {
              sRuntimePermToOp.put(opPerms[i], opToSwitch[i]);
            }
          }
        }
      }
    }
    Integer code = sRuntimePermToOp.get(permission);
    if (code != null) {
      return code;
    }
    return -1;
  }





  static String getProcessName(int pid){
    FileInputStream fis = null;
    try {
      byte[] buff = new byte[512];
      fis = new FileInputStream("/proc/"+pid+"/cmdline");
      int len = fis.read(buff);
      if (len > 0) {
        int i;
        for (i=0; i<len; i++) {
          if (buff[i] == '\0') {
            break;
          }
        }
        return new String(buff,0,i);
      }
    }catch (Exception e){
      e.printStackTrace();
    }finally {
      try {
        if (fis != null) {
          fis.close();
        }
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    return null;
  }
}
