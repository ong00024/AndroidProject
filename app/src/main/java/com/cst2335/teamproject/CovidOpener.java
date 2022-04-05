package com.cst2335.teamproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * CovidOpener opens a database to store and retrieve previous queries
 *
 * @author Vincent Zheng, Kevin Ong
 */

public class CovidOpener extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "CovidQueries";
    public static final String COL_ID = "_id";
    public static final String COL_COUNTRY = "country";
    public static final String COL_FROM_DATE = "FROM_DATE";
    public static final String COL_TO_DATE = "TO_DATE";
    public static final String COL_DAYS = "dates";
    public static final String COL_MESSAGE = "Message";
    public static final String COL_RESULTS = "Results";

    protected static final String DATABASE_NAME = "CovidDatabase";
    protected static final int VERSION_NUM = 1;


    public CovidOpener(Context context) {
        super(context, DATABASE_NAME, null, VERSION_NUM);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
       String create = String.format("Create Table %s ( %s INTEGER PRIMARY KEY AUTOINCREMENT, %s TEXT, %s TEXT, %s TEXT, %s INTEGER, %s TEXT, %s TEXT );",
                TABLE_NAME, COL_ID, COL_COUNTRY, COL_FROM_DATE, COL_TO_DATE, COL_DAYS, COL_MESSAGE, COL_RESULTS);

        db.execSQL(create);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        this.onCreate(db);
    }
}
