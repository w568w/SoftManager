package cn.ifreedomer.com.softmanager.util;

import android.content.Context;

import com.zzzmode.appopsx.AssetsUtils;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
    private static final String APP_COMPONENT_DB = "action_component.db";

    public static void copyDB(Context context) {
        List<String> dbList = new ArrayList<>();
        dbList.add(APP_AD_DB);
        dbList.add(APP_CACHE_DB);
        dbList.add(APP_FOLDER_DB);
        dbList.add(APP_COMPONENT_DB);
        for (int i = 0; i < dbList.size(); i++) {
            if (context.getDatabasePath(dbList.get(i)).exists()) {
                continue;
            }
            File databasePath = context.getDatabasePath(dbList.get(i));
            if (!databasePath.getParentFile().exists()) {
                databasePath.getParentFile().mkdirs();
            }
            AssetsUtils.copyFile(context, dbList.get(i), context.getDatabasePath(dbList.get(i)), true);
        }


//        if (!context.getDatabasePath(APP_CACHE_DB).exists()) {
//            File databasePath = context.getDatabasePath(APP_CACHE_DB);
//            if (!databasePath.getParentFile().exists()) {
//                databasePath.getParentFile().mkdirs();
//            }
//            Log.e(TAG, "copyDB path=" + databasePath.getPath() + "   exist = " + databasePath.exists());
//        }
//
//
//        if (!context.getDatabasePath(APP_AD_DB).exists()) {
//            AssetsUtils.copyFile(context, APP_AD_DB, context.getDatabasePath(APP_AD_DB), true);
//        }
//
//        if (!context.getDatabasePath(APP_AD_DB).exists()) {
//            AssetsUtils.copyFile(context, APP_CACHE_DB, context.getDatabasePath(APP_CACHE_DB), true);
//        }
//        AssetsUtils.copyFile(context, APP_FOLDER_DB, context.getDatabasePath(APP_FOLDER_DB), true);
//        AssetsUtils.copyFile(context, APP_COMPONENT_DB, context.getDatabasePath(APP_COMPONENT_DB), true);


    }
}
