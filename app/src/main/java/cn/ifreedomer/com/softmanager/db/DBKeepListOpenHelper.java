package cn.ifreedomer.com.softmanager.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBKeepListOpenHelper extends SQLiteOpenHelper {

	private final static String SqLiteName = "keeplist.db";
	private final static int mversion = 1;

	public DBKeepListOpenHelper(Context context) {
		super(context, SqLiteName, null, mversion);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("create table if not exists keeplist (packagename TEXT, keep INTEGER)");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
		onCreate(db);
	}

}
