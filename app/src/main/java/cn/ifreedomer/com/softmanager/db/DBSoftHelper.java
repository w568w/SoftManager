package cn.ifreedomer.com.softmanager.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBSoftHelper extends SQLiteOpenHelper {

    private final static String SqLiteName = "soft.db";
    public final static int mversion = 505;
    public static final String SOFT_DISABLE_TABLE = "soft_disable";

    public DBSoftHelper(Context context) {
        super(context, SqLiteName, null, mversion);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

//        CREATE TABLE "actions" ("action_name" TEXT PRIMARY KEY NOT NULL DEFAULT (null), "cn_desc"
//        NTEXT DEFAULT (null), "en_desc" NTEXT DEFAULT (null) )


//        CREATE TABLE "components" ("component_name" TEXT PRIMARY KEY  NOT NULL  DEFAULT (null) ,"cn_desc" NTEXT DEFAULT (null) ,"en_desc" NTEXT DEFAULT (null) )
        db.execSQL("create table if not exists " + SOFT_DISABLE_TABLE + " (name TEXT PRIMARY KEY, exported TEXT ,belongPkg TEXT,fullPathName TEXT,enable BOOLEAN )");

//        db.execSQL("create table if not exists actions (action_name TEXT PRIMARY KEY  NOT NULL  DEFAULT (null) , cn_desc NTEXT DEFAULT (null),en_desc NTEXT DEFAULT (null) )");
//        db.execSQL("create table if not exists components (component_name TEXT PRIMARY KEY  NOT NULL  DEFAULT (null) , cn_desc NTEXT DEFAULT (null) ,en_desc NTEXT DEFAULT (null))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int arg1, int arg2) {
        onCreate(db);
    }

}
