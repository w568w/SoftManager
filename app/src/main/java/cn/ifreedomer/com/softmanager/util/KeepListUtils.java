package cn.ifreedomer.com.softmanager.util;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import cn.ifreedomer.com.softmanager.db.DBKeepListOpenHelper;


/**
 * 白名单数据库操作类
 * 
 * @author 庄宏岩
 * 
 */
public class KeepListUtils  {
	private static final String TAG = "db";

	/**
	 * @param context
	 *            把信息保存到数据库
	 */
	public static void save(Context context, String packageName) {
		DBKeepListOpenHelper tdbOpenHelper = new DBKeepListOpenHelper(context);
		SQLiteDatabase db = tdbOpenHelper.getWritableDatabase();
		try {
			db.execSQL("insert into keeplist (packagename) values('?')", new Object[] { packageName });
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.close();
			tdbOpenHelper.close();
		}
	}

	/**
	 * @param context
	 *            把信息保存到数据库
	 */
	public static void saveAll(Context context, String[] packages) {
		DBKeepListOpenHelper tdbOpenHelper = new DBKeepListOpenHelper(context);
		SQLiteDatabase db = tdbOpenHelper.getWritableDatabase();
		try {
			for (String packageName : packages) {
				db.execSQL("insert into keeplist (packagename) values('?')", new Object[] { packageName });
				Log.i(TAG, "save()");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.close();
			tdbOpenHelper.close();
		}

	}

	/**
	 * @param context
	 *            从数据库中删除某条数据
	 */
	public static void delete(Context context, String packageName) {
		DBKeepListOpenHelper tdbOpenHelper = new DBKeepListOpenHelper(context);
		SQLiteDatabase db = tdbOpenHelper.getWritableDatabase();
		try {
			db.execSQL("delete from keeplist where packagename='?'", new Object[] { packageName });
			Log.i(TAG, "delete()");
		} catch (SQLException e) {
			e.printStackTrace();
		}finally {
			db.close();
			tdbOpenHelper.close();
		}
	}

	/**
	 * @param context
	 * @return 从数据库中获取所有数据并返回一个集合
	 */
	public static ArrayList<String> getList(Context context) {
		ArrayList<String> list = new ArrayList<String>();
		DBKeepListOpenHelper tdbOpenHelper = new DBKeepListOpenHelper(context);
		SQLiteDatabase db = tdbOpenHelper.getWritableDatabase();
		Cursor cursor = db.rawQuery("select * from keeplist ", null);
		try {
			if (cursor != null) {
				while (cursor.moveToNext()) {
					list.add(cursor.getString(0));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
			db.close();
			tdbOpenHelper.close();
		}
		return list;
	}
}
