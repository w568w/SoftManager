package cn.ifreedomer.com.softmanager.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBAppFolderOpenHelper extends SQLiteOpenHelper {

	private final static String SqLiteName = "app_folder.db";
	public final static int mversion = 505;
	public static String table_path = "path";
	public static String table_pname = "pname";
	public static String table_name_zh = "name_zh";
	public static String table_name_en = "name_en";
	public static String field_path = "path";
	public static String field_pname = "pname";
	public static String field_name = "name";
	public static String field_id = "_id";
	public DBAppFolderOpenHelper(Context context) {
		super(context, SqLiteName, null, mversion);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table if not exists path (_id INTEGER primary key autoincrement, path TEXT)");
		db.execSQL("create table if not exists pname (_id INTEGER, pname TEXT)");
		db.execSQL("create table if not exists name_zh (_id INTEGER, name TEXT)");
		db.execSQL("create table if not exists name_en (_id INTEGER, name TEXT)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		onCreate(db);
	}

}
