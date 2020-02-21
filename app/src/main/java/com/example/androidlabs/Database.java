package com.example.androidlabs;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
public class Database extends SQLiteOpenHelper
{
    public static final String DATABASE_LAB5 = "Lab5";
    public static final int VERSION = 2;
    public static final String TABLE_NAME = "Messages";
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_MESAGE = "Sent";
    public static final String COLUMN_MESSAGE_TYPE = "message_type";
    public Database(Context context) {


        super(context, DATABASE_LAB5, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
    db.execSQL("DROP TABLE IF EXISTS "+ TABLE_NAME);
    db.execSQL("CREATE TABLE " + TABLE_NAME + "(" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT," + COLUMN_MESAGE + " TEXT, " + COLUMN_MESSAGE_TYPE + " TEXT )");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
    onCreate(db);
    }
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }


}
