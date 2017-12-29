package cn.ifreedomer.com.softmanager.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import cn.ifreedomer.com.softmanager.CleanApplication;
import cn.ifreedomer.com.softmanager.bean.ComponentEntity;

/**
 * @author eavawu
 * @since 28/12/2017.
 */

public class DBSoftUtil {
    public static void save(ComponentEntity componentEntity) {
        DBSoftHelper dbSoftHelper = new DBSoftHelper(CleanApplication.INSTANCE);
        SQLiteDatabase writableDatabase = dbSoftHelper.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("name", componentEntity.getName());
        contentValues.put("exported", componentEntity.getExported());
        contentValues.put("belongPkg", componentEntity.getBelongPkg());
        contentValues.put("fullPathName", componentEntity.getFullPathName());
        contentValues.put("enable", componentEntity.isEnable());
        writableDatabase.insert(DBSoftHelper.SOFT_DISABLE_TABLE, null, contentValues);
        writableDatabase.close();
    }


    public static void remove(ComponentEntity componentEntity) {
        DBSoftHelper dbSoftHelper = new DBSoftHelper(CleanApplication.INSTANCE);
        SQLiteDatabase writableDatabase = dbSoftHelper.getWritableDatabase();
        String[] params = new String[]{componentEntity.getName()};
        writableDatabase.delete(DBSoftHelper.SOFT_DISABLE_TABLE, "name = ?", params);
        writableDatabase.close();
    }

    public static List<ComponentEntity> getAll() {
        DBSoftHelper dbSoftHelper = new DBSoftHelper(CleanApplication.INSTANCE);
        SQLiteDatabase readableDatabase = dbSoftHelper.getReadableDatabase();
        readableDatabase.beginTransaction();
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
        readableDatabase.close();
        return componentEntitieList;
    }

}
