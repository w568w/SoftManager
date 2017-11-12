package cn.ifreedomer.com.softmanager.util;

import android.content.Context;
import android.util.Log;

import com.zzzmode.appopsx.AssetsUtils;

import java.io.File;

/**
 * @author:eavawu
 * @since: 10/11/2017.
 * TODO:
 */

public class DBUtil {
    private static final String TAG = DBUtil.class.getSimpleName();
    private static final String APP_AD_DB = "app_ad.db";
    private static final String APP_CACHE_DB = "app_cache.db";
    private static final String APP_FOLDER_DB = "app_folder.db";

    public static void copyDB(Context context) {
        if (!context.getDatabasePath(APP_CACHE_DB).exists()) {
            File databasePath = context.getDatabasePath(APP_CACHE_DB);
            if (!databasePath.getParentFile().exists()) {
                databasePath.getParentFile().mkdirs();
            }
            Log.e(TAG, "copyDB path=" + databasePath.getPath() + "   exist = " + databasePath.exists());
            AssetsUtils.copyFile(context, APP_AD_DB, context.getDatabasePath(APP_AD_DB), true);
            AssetsUtils.copyFile(context, APP_CACHE_DB, context.getDatabasePath(APP_CACHE_DB), true);
            AssetsUtils.copyFile(context, APP_FOLDER_DB, context.getDatabasePath(APP_FOLDER_DB), true);
        }
    }
}
