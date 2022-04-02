package com.cst2335.teamproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * CovidOpener opens a database to store and retrieve previous queries
 * @author Vincent, Kevin
 */

public class CovidOpener extends SQLiteOpenHelper {

    protected static final String DATABASE_NAME = "CovidDatabase";
    protected static final int VERSION_NUM = 1;
    public static final String TABLE_NAME = "CovidCases";
    public static final String COL_ID = "_id";
    public static final String COL_COUNTRY = "country";
    public static final String COL_DATE = "date";
    public static final String COL_PROVINCE = "province";
    public static final String COL_CASE = "cases";

    public CovidOpener(Context context) {
        super(context,DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME
                + "(_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_DATE + " DATE,"
                + COL_COUNTRY + " TEXT,"
                + COL_PROVINCE + " TEXT,"
                + COL_CASE + " INTEGER);");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        onCreate(db);
    }
}
