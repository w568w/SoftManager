package cn.ifreedomer.com.softmanager.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import cn.ifreedomer.com.softmanager.util.LogUtil;

/**
 * @author:eavawu
 * @since: 03/12/2017.
 * TODO:
 */

public class DBActionUtils {
    private static final String TAG = DBActionUtils.class.getSimpleName();

    public static Map<String, String> loadActionMap(Context context) {
        Map<String, String> actionMap = new ConcurrentHashMap<>();
        DBActionHelper tdbOpenHelper = new DBActionHelper(context);
        SQLiteDatabase db = tdbOpenHelper.getWritableDatabase();
        String sql = "select * from actions";
        Cursor cursor = db.rawQuery(sql, null);
        try {
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    String action = (cursor.getString(cursor.getColumnIndex("action_name")));
                    String cnDesc = cursor.getString(cursor.getColumnIndex("cn_desc"));
                    actionMap.put(action, cnDesc);
                    LogUtil.d(TAG, "action =" + action + "  cn_desc=" + cnDesc);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "get: " + e.getCause());
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            db.close();
            tdbOpenHelper.close();
        }
        Log.e(TAG, "sql 2");

        LogUtil.d(TAG, "action map =" + actionMap.toString());

        return actionMap;
    }


}
