package cn.ifreedomer.com.softmanager.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBAppCacheOpenHelper extends SQLiteOpenHelper {

	private final static String SqLiteName = "app_cache.db";
	public final static int mversion = 505;
	public DBAppCacheOpenHelper(Context context) {
		super(context, SqLiteName, null, mversion);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table if not exists pname (_id INTEGER primary key autoincrement, pname TEXT)");
		db.execSQL("create table if not exists path (_id INTEGER, path TEXT, t_id INTEGER, checked INTEGER)");
		db.execSQL("create table if not exists type_zh (t_id INTEGER primary key autoincrement, t_name TEXT)");
		db.execSQL("create table if not exists type_en (t_id INTEGER primary key autoincrement, t_name TEXT)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		onCreate(db);
	}

}
