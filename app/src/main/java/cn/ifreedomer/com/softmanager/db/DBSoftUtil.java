package cn.ifreedomer.com.softmanager.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import cn.ifreedomer.com.softmanager.CleanApplication;
import cn.ifreedomer.com.softmanager.bean.ComponentEntity;
import cn.ifreedomer.com.softmanager.util.LogUtil;

/**
 * @author eavawu
 * @since 28/12/2017.
 */

public class DBSoftUtil {
    private static final String TAG = DBSoftUtil.class.getSimpleName();
    private static SQLiteDatabase writableDatabase;
    private static SQLiteDatabase readableDatabase;

    public static boolean isExist(ComponentEntity componentEntity) {
        isOpen();
//        String sql = String.format("select * from %s where name = %s", DBSoftHelper.SOFT_DISABLE_TABLE, componentEntity.getName());
//        LogUtil.d(TAG, "isExist sql = " + sql);
        Cursor cursor = readableDatabase.rawQuery("select * from  " + DBSoftHelper.SOFT_DISABLE_TABLE + " where name = ?", new String[]{componentEntity.getName()});
        if (cursor.moveToNext()) {
            cursor.close();
            return true;
        }
        cursor.close();
        return false;
    }

    public static void isOpen() {
        if (writableDatabase != null && readableDatabase != null) {
            return;
        }
        DBSoftHelper dbSoftHelper = new DBSoftHelper(CleanApplication.INSTANCE);
        writableDatabase = dbSoftHelper.getWritableDatabase();
        readableDatabase = dbSoftHelper.getReadableDatabase();
    }

    public static boolean save(ComponentEntity componentEntity) {
        isOpen();
        if (isExist(componentEntity)) {
            return false;
        }
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", componentEntity.getName());
        contentValues.put("exported", componentEntity.getExported());
        contentValues.put("belongPkg", componentEntity.getBelongPkg());
        contentValues.put("fullPathName", componentEntity.getFullPathName());
        contentValues.put("enable", componentEntity.isEnable());
        long offset = writableDatabase.insert(DBSoftHelper.SOFT_DISABLE_TABLE, null, contentValues);
        LogUtil.d(TAG, "save offset = " + offset);
        return offset > 0;
    }


    public static void remove(ComponentEntity componentEntity) {
        isOpen();
        if (!isExist(componentEntity)) {
            return;
        }
        DBSoftHelper dbSoftHelper = new DBSoftHelper(CleanApplication.INSTANCE);
        SQLiteDatabase writableDatabase = dbSoftHelper.getWritableDatabase();
        String[] params = new String[]{componentEntity.getName()};
        int offset = writableDatabase.delete(DBSoftHelper.SOFT_DISABLE_TABLE, "name = ?", params);
        LogUtil.d(TAG, "remove offset = " + offset);
        writableDatabase.close();
    }

    public static List<ComponentEntity> getAll() {

        isOpen();
        Cursor cursor = readableDatabase.rawQuery(String.format("select * from %s", DBSoftHelper.SOFT_DISABLE_TABLE), null);
        List<ComponentEntity> componentEntitieList = new ArrayList<>();
        while (cursor.moveToNext()) {
            ComponentEntity componentEntity = new ComponentEntity();
            String name = cursor.getString(cursor.getColumnIndex("name")); //获取第一列的值,第一列的索引从0开始
            String belongPkg = cursor.getString(cursor.getColumnIndex("belongPkg"));//获取第二列的值
            String fullPathName = cursor.getString(cursor.getColumnIndex("fullPathName"));//获取第二列的值
            String exported = cursor.getString(cursor.getColumnIndex("exported"));//获取第二列的值
            boolean enable = cursor.getInt(cursor.getColumnIndex("enable")) == 1;//获取第二列的值
            componentEntity.setName(name);
            componentEntity.setBelongPkg(belongPkg);
            componentEntity.setFullPathName(fullPathName);
            componentEntity.setExported(exported);
            componentEntity.setEnable(enable);
            componentEntitieList.add(componentEntity);
        }
        cursor.close();
        return componentEntitieList;
    }


    public static void close() {
        if (readableDatabase != null) {
            readableDatabase.close();
        }
        if (writableDatabase != null) {
            writableDatabase.close();
        }
    }


}
