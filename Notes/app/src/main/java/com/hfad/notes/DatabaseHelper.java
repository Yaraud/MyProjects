package com.hfad.notes;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "notes";
    private static final int DB_VERSION = 4;

    public DatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE NOTE ( _id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "TITLE TEXT," +
                "DESCRIPTION TEXT);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        updateMyDatabase(db, oldVersion, newVersion);
    }

    private static void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 4) {
            db.execSQL("CREATE TABLE NOTE ( _id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "TITLE TEXT," +
                    "DESCRIPTION TEXT);");
        }
    }

    private static void insertNote(SQLiteDatabase db, String title, String description) {
        ContentValues noteValues = new ContentValues();
        noteValues.put("TITLE", title);
        noteValues.put("DESCRIPTION", description);
        db.insert("NOTE", null, noteValues);
    }
}
