package cn.ifreedomer.com.softmanager.util;

import android.content.Context;

import com.zzzmode.appopsx.AssetsUtils;

/**
 * @author:eavawu
 * @since: 10/11/2017.
 * TODO:
 */

public class DBUtil {
    private static final String APP_AD_DB = "app_ad.db";
    private static final String APP_CACHE_DB = "app_cache.db";
    private static final String APP_FOLDER_DB = "app_folder.db";

    public static void copyDB(Context context) {
        if (!context.getApplicationContext().getDatabasePath(APP_AD_DB).exists()) {
            AssetsUtils.copyFile(context, APP_AD_DB, context.getApplicationContext().getDatabasePath(APP_AD_DB), true);
            AssetsUtils.copyFile(context, APP_CACHE_DB, context.getApplicationContext().getDatabasePath(APP_CACHE_DB), true);
            AssetsUtils.copyFile(context, APP_FOLDER_DB, context.getApplicationContext().getDatabasePath(APP_FOLDER_DB), true);
        }
    }
}
