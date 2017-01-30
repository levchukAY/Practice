package com.artioml.practice.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.artioml.practice.data.DatabaseDescription.*;

/**
 * Created by Artiom L on 28.01.2017.
 */

public class PracticeDatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "PunchSpeedDB";
    private static final int DATABASE_VERSION = 1;

    private static PracticeDatabaseHelper instance;

    private PracticeDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static PracticeDatabaseHelper getInstance(Context context) {
        if (instance == null)
            instance = new PracticeDatabaseHelper(context);
        return instance;
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String COMMUNITY_TABLE_SQL =
                "CREATE TABLE " + Community.TABLE_NAME + "(" +
                Community._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                Community.COLUMN_LOGIN + " TEXT NOT NULL, " +
                Community.COLUMN_PASSWORD + " TEXT NOT NULL);";
        sqLiteDatabase.execSQL(COMMUNITY_TABLE_SQL);

        final String HISTORY_TABLE_SQL = "CREATE TABLE " + History.TABLE_NAME +"(" +
                History._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                History.COLUMN_PUNCH_TYPE + " INTEGER NOT NULL, " +
                History.COLUMN_HAND + " TEXT NOT NULL, " +
                History.COLUMN_GLOVES + " TEXT NOT NULL, " +
                History.COLUMN_GLOVES_WEIGHT + " TEXT, " +
                History.COLUMN_POSITION + " TEXT NOT NULL, " +
                History.COLUMN_SPEED + " REAL, " +
                History.COLUMN_REACTION + " REAL, " +
                History.COLUMN_ACCELERATION + " REAL, " +
                History.COLUMN_DATE + " DATETIME NOT NULL);";
        sqLiteDatabase.execSQL(HISTORY_TABLE_SQL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

}