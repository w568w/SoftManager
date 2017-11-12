package cn.ifreedomer.com.softmanager.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import cn.ifreedomer.com.softmanager.bean.clean.ClearItem;


public class DBAppAdUtils {
    private static final String TAG = "newdb";


    /**
     * @param context
     * @return 从数据库中获取所有数据并返回一个集合
     */
    public static ArrayList<ClearItem> get(Context context, String language) {
        ArrayList<ClearItem> list = new ArrayList<ClearItem>();
        DBAppAdOpenHelper tdbOpenHelper = new DBAppAdOpenHelper(context);
        SQLiteDatabase db = tdbOpenHelper.getWritableDatabase();
        String name_table = "name_" + language;
        String sql = "SELECT " + name_table + ".*,path.* from " + name_table + ",path WHERE " + name_table + "._id=path._id";
        Cursor cursor = db.rawQuery(sql, null);
        try {
            if (cursor != null) {
                while (cursor.moveToNext()) {
                    ClearItem info = new ClearItem();
                    info.setName(cursor.getString(cursor.getColumnIndex("name")));
                    String filePath = cursor.getString(cursor.getColumnIndex("path"));
                    info.setFilePath(filePath);
                    list.add(info);
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

        return list;
    }

}
