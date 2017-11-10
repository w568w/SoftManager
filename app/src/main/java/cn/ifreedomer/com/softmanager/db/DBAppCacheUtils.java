package cn.ifreedomer.com.softmanager.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

import cn.ifreedomer.com.softmanager.bean.clean.ClearItem;
import cn.ifreedomer.com.softmanager.util.DesUtils;
import cn.ifreedomer.com.softmanager.util.HASH;


/**
 * 缓存目录数据库操作类
 * 
 * @author 庄宏岩
 * 
 */
public class DBAppCacheUtils {
	private static DBAppCacheUtils dbUtils = null;
	private static DBAppCacheOpenHelper tdbOpenHelper = null;
	private static SQLiteDatabase db = null;

	public static DBAppCacheUtils getInstance(Context context) {
		if (dbUtils == null) {
			dbUtils = new DBAppCacheUtils();
		}
		if (tdbOpenHelper == null) {
			tdbOpenHelper = new DBAppCacheOpenHelper(context);
		}
		if (db == null) {
			db = tdbOpenHelper.getWritableDatabase();
		}
		return dbUtils;
	}

	/**
	 * @param context
	 * @return 从数据库中获取所有数据并返回一个集合
	 */
	public ArrayList<ClearItem> get(Context context, String packageName, String language) {
		String key = DesUtils.getKey1();
		key = DesUtils.getKey2();
		key = DesUtils.getKey3();
		ArrayList<ClearItem> list = new ArrayList<ClearItem>();
		Cursor cursor = null;
		try {
			String type_table = "type_" + language;
			String sql = "SELECT " + type_table + ".*,path.*,pname.* from " + type_table + ",path,pname WHERE pname.pname=? AND " + type_table
					+ ".t_id=path.t_id AND path._id=pname._id";
			cursor = db.rawQuery(sql, new String[] { HASH.md5sum(packageName+"_zz")});
			if (cursor != null) {
				while (cursor.moveToNext()) {
					ClearItem info = new ClearItem();
					info.setName(cursor.getString(cursor.getColumnIndex("t_name")));
					info.setPackageName(packageName);
					String filePath = cursor.getString(cursor.getColumnIndex("path"));
					info.setFilePath(DesUtils.decryptDES(filePath, key));
					info.setChecked(cursor.getInt(cursor.getColumnIndex("checked")) == 1);
					list.add(info);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return list;
	}

	/**
	 * @param context
	 * @return 从数据库中获取所有数据并返回一个集合
	 */
	public ArrayList<ClearItem> getAll(Context context, String language) {
		String key = DesUtils.getKey1();
		key = DesUtils.getKey2();
		key = DesUtils.getKey3();
		ArrayList<ClearItem> list = new ArrayList<ClearItem>();
		Cursor cursor = null;
		try {
			String type_table = "type_" + language;
			String sql = "SELECT " + type_table + ".*,path.*,pname.* from " + type_table + ",path,pname WHERE " + type_table
					+ ".t_id=path.t_id AND path._id=pname._id";
			cursor = db.rawQuery(sql, null);
			if (cursor != null) {
				while (cursor.moveToNext()) {
					ClearItem info = new ClearItem();
					info.setName(cursor.getString(cursor.getColumnIndex("t_name")));
					String packageName = cursor.getString(cursor.getColumnIndex("pname"));
					info.setPackageName(DesUtils.decryptDES(packageName, key));
					String filePath = cursor.getString(cursor.getColumnIndex("path"));
					info.setFilePath(DesUtils.decryptDES(filePath, key));
					info.setChecked(cursor.getInt(cursor.getColumnIndex("checked")) == 1);
					list.add(info);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return list;
	}

	/**
	 * @param context
	 * @return 从数据库中获取所有数据并返回一个集合
	 */
	public ArrayList<ClearItem> old_get(Context context, String language) {
		ArrayList<ClearItem> list = new ArrayList<ClearItem>();
		Cursor cursor = null;
		try {
			String type_table = "type_" + language;
			String sql = "SELECT " + type_table + ".*,path.*,pname.* from " + type_table + ",path,pname where " + type_table
					+ ".t_id=path.t_id AND path._id=pname._id";
			cursor = db.rawQuery(sql, null);
			if (cursor != null) {
				while (cursor.moveToNext()) {
					ClearItem info = new ClearItem();
					info.setName(cursor.getString(cursor.getColumnIndex("t_name")));
					info.setPackageName(cursor.getString(cursor.getColumnIndex("pname")));
					info.setFilePath(cursor.getString(cursor.getColumnIndex("path")));
					info.setChecked(cursor.getInt(cursor.getColumnIndex("checked")) == 1);
					list.add(info);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (cursor != null) {
				cursor.close();
			}
		}
		return list;
	}

	/**
	 * @param infos
	 * @param context
	 *            把信息保存到数据库
	 */
	public void update(Context context, ArrayList<ClearItem> infos) {
		try {
			for (ClearItem info : infos) {
				db.execSQL("update path set path=? where path=?", new Object[] { info.getMd5Path(), info.getFilePath() });
				db.execSQL("update pname set pname=? where pname=?", new Object[] { info.getMd5PackageName(), info.getPackageName() });
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void close() {
		try {
			if (db != null) {
				db.close();
				db = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		try {
			if (tdbOpenHelper != null) {
				tdbOpenHelper.close();
				tdbOpenHelper = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
