package cn.ifreedomer.com.softmanager.util;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;

/**
 * @author eavawu
 * @since 06/01/2018.
 */

public class ChannelUtil {

    public static String getMeta(Context context, String key) {
        ApplicationInfo appInfo = null;
        try {
            appInfo = context.getPackageManager()
                    .getApplicationInfo(context.getPackageName(),
                            PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        String msg = appInfo.metaData.getString(key);
        return msg;
    }
}
