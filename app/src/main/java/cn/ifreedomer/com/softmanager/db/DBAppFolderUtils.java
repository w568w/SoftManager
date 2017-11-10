package cn.ifreedomer.com.softmanager.db;

import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;

import cn.com.opda.android.clearmaster.model.ClearItem;
import cn.com.opda.android.clearmaster.utils.BaseJsonUtil;
import cn.com.opda.android.clearmaster.utils.DesUtils;

public class DBAppFolderUtils extends BaseJsonUtil {
	private static final String TAG = "db";

	public DBAppFolderUtils(Context context) {
		super(context);
	}

	/**
	 * @param info
	 * @param context
	 *            把信息保存到数据库
	 */
	public static void save(Context context, ClearItem info) {
		// DBAppFolderOpenHelper tdbOpenHelper = new
		// DBAppFolderOpenHelper(context);
		// SQLiteDatabase db = tdbOpenHelper.getWritableDatabase();
		// Cursor cursor = db.rawQuery("select * from path where path = ?", new
		// String[] { info.getFilePath() });
		// try {
		// if (cursor == null || cursor.getCount() == 0) {
		// db.execSQL("insert into path (path) values(?)", new Object[] {
		// info.getFilePath() });
		// db.execSQL("insert into name_zh (name) values(?)", new Object[] {
		// info.getCname() });
		// db.execSQL("insert into name_en (name) values(?)", new Object[] {
		// info.getEname() });
		// db.execSQL("insert into pname (pname) values(?)", new Object[] {
		// info.getPackageName() });
		// Log.i(TAG, "save()");
		// } else {
		// Log.i(TAG, "data is exists");
		// }
		// } catch (SQLException e) {
		// e.printStackTrace();
		// } finally {
		// if (cursor != null) {
		// cursor.close();
		// }
		// db.close();
		// tdbOpenHelper.close();
		// }
	}

	/**
	 * @param info
	 * @param context
	 *            把信息保存到数据库
	 */
	public static void save(Context context, ArrayList<ClearItem> infos) {
		// DBAppFolderOpenHelper tdbOpenHelper = new
		// DBAppFolderOpenHelper(context);
		// SQLiteDatabase db = tdbOpenHelper.getWritableDatabase();
		// try {
		// for (ClearItem info : infos) {
		// Cursor cursor = db.rawQuery("select * from path where path = ?", new
		// String[] { info.getFilePath() });
		// if (cursor == null || cursor.getCount() == 0) {
		// if (cursor != null) {
		// cursor.close();
		// }
		// db.execSQL("insert into path (path) values(?)", new Object[] {
		// info.getFilePath() });
		// cursor = db.rawQuery("select _id from path where path = ?", new
		// String[] { info.getFilePath() });
		// cursor.moveToFirst();
		// int id = cursor.getInt(0);
		// db.execSQL("insert into name_zh (_id,name) values(?,?)", new Object[]
		// { id,info.getCname() });
		// db.execSQL("insert into name_en (_id,name) values(?,?)", new Object[]
		// { id,info.getEname() });
		// db.execSQL("insert into pname (_id,pname) values(?,?)", new Object[]
		// { id,info.getPackageName() });
		// Log.i(TAG, "save()");
		// } else {
		// Log.i(TAG, "data is exists");
		// }
		// if (cursor != null) {
		// cursor.close();
		// }
		// }
		// } catch (SQLException e) {
		// e.printStackTrace();
		// } finally {
		// db.close();
		// tdbOpenHelper.close();
		// }
	}
	
	/**
	 * @param context
	 * @return 从数据库中获取所有数据并返回一个集合
	 */
	public static ArrayList<ClearItem> old_get(Context context, String language) {
		ArrayList<ClearItem> list = new ArrayList<ClearItem>();
		DBAppFolderOpenHelper tdbOpenHelper = new DBAppFolderOpenHelper(context);
		SQLiteDatabase db = tdbOpenHelper.getWritableDatabase();
		String name_table = "name_" + language;
		String sql = "SELECT pname.*," + name_table + ".*,path.* from pname," + name_table + ",path WHERE pname._id=path._id AND " + name_table
				+ "._id=path._id";
		Cursor cursor = db.rawQuery(sql, null);
		try {
			if (cursor != null) {
				while (cursor.moveToNext()) {
					ClearItem info = new ClearItem();
					info.setName(cursor.getString(cursor.getColumnIndex("name")));
					String packageName= cursor.getString(cursor.getColumnIndex("pname"));
					info.setPackageName(packageName);
					String filePath = cursor.getString(cursor.getColumnIndex("path"));
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


	/**
	 * @param context
	 * @return 从数据库中获取所有数据并返回一个集合
	 */
	public static ArrayList<ClearItem> get(Context context, String language) {
		String key = DesUtils.getKey1();
		key = DesUtils.getKey2();
		key = DesUtils.getKey3();
		ArrayList<ClearItem> list = new ArrayList<ClearItem>();
		DBAppFolderOpenHelper tdbOpenHelper = new DBAppFolderOpenHelper(context);
		SQLiteDatabase db = tdbOpenHelper.getWritableDatabase();
		String name_table = "name_" + language;
		String sql = "SELECT pname.*," + name_table + ".*,path.* from pname," + name_table + ",path WHERE pname._id=path._id AND " + name_table
				+ "._id=path._id";
		Cursor cursor = db.rawQuery(sql, null);
		try {
			if (cursor != null) {
				while (cursor.moveToNext()) {
					ClearItem info = new ClearItem();
					info.setName(cursor.getString(cursor.getColumnIndex("name")));
					String packageName= cursor.getString(cursor.getColumnIndex("pname"));
					info.setPackageName(DesUtils.decryptDES(packageName, key));
					String filePath = cursor.getString(cursor.getColumnIndex("path"));
					info.setFilePath(DesUtils.decryptDES(filePath, key));
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

	public static ArrayList<ClearItem> GetDataByPackageName(String packageName, Context mContext) {
		String key = DesUtils.getKey1();
		key = DesUtils.getKey2();
		key = DesUtils.getKey3();
		ArrayList<ClearItem> list = new ArrayList<ClearItem>();
		DBAppFolderOpenHelper tdbOpenHelper = new DBAppFolderOpenHelper(mContext);
		SQLiteDatabase db = tdbOpenHelper.getWritableDatabase();
		String sql = "SELECT pname.*,name_zh.*,path.* from pname,name_zh,path WHERE pname.pname=? AND name_zh._id=path._id AND pname._id=path._id";
		Cursor cursor = null;
		try {
			cursor = db.rawQuery(sql, new String[] { DesUtils.encryptDES(packageName, key)});
			if (cursor != null) {
				while (cursor.moveToNext()) {
					ClearItem info = new ClearItem();
					info.setName(cursor.getString(cursor.getColumnIndex("name")));
					info.setPackageName(packageName);
					String filePath = cursor.getString(cursor.getColumnIndex("path"));
					info.setFilePath(DesUtils.decryptDES(filePath, key));
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

	/**
	 * @param info
	 * @param context
	 *            把信息保存到数据库
	 */
	public static void update(Context context, ArrayList<ClearItem> infos) {
		DBAppFolderOpenHelper tdbOpenHelper = new DBAppFolderOpenHelper(context);
		SQLiteDatabase db = tdbOpenHelper.getWritableDatabase();
		try {
			for (ClearItem info : infos) {
				db.execSQL("update path set path=? where path=?", new Object[] { info.getMd5Path(),info.getFilePath() });
				db.execSQL("update pname set pname=? where pname=?", new Object[] { info.getMd5PackageName(),info.getPackageName() });
				Log.i(TAG, "update()");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			db.close();
			tdbOpenHelper.close();
		}
	}
}
