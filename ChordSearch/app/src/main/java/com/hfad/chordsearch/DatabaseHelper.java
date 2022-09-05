package com.hfad.chordsearch;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper{
    private static final String DB_NAME = "chords";
    private static final int DB_VERSION = 3;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE Chord (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "NAME TEXT," +
                "FIRSTSTRING TEXT," +
                "SECONDSTRING TEXT," +
                "THIRDSTRING TEXT," +
                "FOURTHSTRING TEXT," +
                "FIFTHSTRING TEXT," +
                "SIXTHSTRING TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateMyDatabase(db, oldVersion, newVersion);
    }

    private static void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 3) {
            db.execSQL("CREATE TABLE CHORD (_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "NAME TEXT," +
                    "FIRSTSTRING TEXT," +
                    "SECONDSTRING TEXT," +
                    "THIRDSTRING TEXT," +
                    "FOURTHSTRING TEXT," +
                    "FIFTHSTRING TEXT," +
                    "SIXTHSTRING TEXT);");
        }
    }
}
