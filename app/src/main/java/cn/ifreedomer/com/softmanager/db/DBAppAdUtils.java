package cn.ifreedomer.com.softmanager.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import cn.ifreedomer.com.softmanager.bean.clean.ClearItem;


public class DBAppAdUtils  {
	private static final String TAG = "newdb";


	/**
	 * @param context
	 * @return 从数据库中获取所有数据并返回一个集合
	 */
	public static ArrayList<ClearItem> get(Context context, String language) {
//		String key = DesUtils.getKey1();
//		key = DesUtils.getKey2();
//		key = DesUtils.getKey3();
		ArrayList<ClearItem> list = new ArrayList<ClearItem>();
		DBAppAdOpenHelper tdbOpenHelper = new DBAppAdOpenHelper(context);
		SQLiteDatabase db = tdbOpenHelper.getWritableDatabase();
		String name_table = "name_" + language;
		String sql = "SELECT " + name_table + ".*,path.* from " + name_table + ",path WHERE " + name_table +"._id=path._id";
		Cursor cursor = db.rawQuery(sql, null);
		try {
			if (cursor != null) {
				while (cursor.moveToNext()) {
					ClearItem info = new ClearItem();
					info.setName(cursor.getString(cursor.getColumnIndex("name")));
					String filePath = cursor.getString(cursor.getColumnIndex("path"));
//					info.setFilePath(DesUtils.decryptDES(filePath, key));
					info.setFilePath(filePath);
					list.add(info);
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
