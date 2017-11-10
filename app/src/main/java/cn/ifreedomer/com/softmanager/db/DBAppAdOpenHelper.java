package cn.ifreedomer.com.softmanager.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBAppAdOpenHelper extends SQLiteOpenHelper {

	private final static String SqLiteName = "app_ad.db";
	public final static int mversion = 505;
	public DBAppAdOpenHelper(Context context) {
		super(context, SqLiteName, null, mversion);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table if not exists path (_id INTEGER primary key autoincrement, path TEXT)");
		db.execSQL("create table if not exists name_zh (_id INTEGER, name TEXT)");
		db.execSQL("create table if not exists name_en (_id INTEGER, name TEXT)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		onCreate(db);
	}

}
