package cn.ifreedomer.com.softmanager.util;

import android.app.AppOpsManager;
import android.content.Context;
import android.text.TextUtils;

import com.zzzmode.appopsx.common.OpsResult;
import com.zzzmode.appopsx.common.ReflectUtils;



/**
 * @author:eavawu
 * @since: 05/11/2017.
 * TODO:
 */

public class AppOpsHelper {

    public OpsResult setMode(Context context, String pkgName, int op, int mode) {
        try {
            return AppOpsx.getInstance(context)
                    .setOpsMode(pkgName, op, mode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static OpsResult setMode(Context context, String pkgName, int op, boolean isAllowed) {
        int mode;
        if (isAllowed) {
            mode = AppOpsManager.MODE_ALLOWED;
        } else {
            mode = AppOpsManager.MODE_IGNORED;
        }
        try {
            return AppOpsx.getInstance(context)
                    .setOpsMode(pkgName, op, mode);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    public static int getOpByPermission(String permission) {
        Object[] sOpPerms = ReflectUtils.getArrayField(AppOpsManager.class, "sOpPerms");
        for (int i = 0; i < sOpPerms.length; i++) {
            if (!TextUtils.isEmpty(permission) && sOpPerms[i] != null) {
                if (sOpPerms[i].equals(permission)) {
                    return i;
                }
            }
        }
        return -1;
    }


    public static OpsResult setPermission(Context context, String pkgName, String permission, boolean isAllowed) {
        int op = getOpByPermission(permission);
        return setMode(context, pkgName, op, isAllowed);
    }


}
