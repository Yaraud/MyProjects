package com.hfad.notes;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.Toast;

import androidx.cursoradapter.widget.CursorAdapter;
import androidx.cursoradapter.widget.SimpleCursorAdapter;


public class MainActivity extends AppCompatActivity {

    Cursor cursor;
    CursorAdapter adapter;
    SQLiteDatabase db;
    SQLiteOpenHelper databaseHelper;

    @SuppressLint("Range")

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ListView listNotes = findViewById(R.id.list_notes);
        try {
            databaseHelper = new DatabaseHelper(this);
            db = databaseHelper.getWritableDatabase();
            cursor = db.query("NOTE",
                    new String[]{"_id", "TITLE"},
                    null,
                    null, null, null, null);
            adapter = new SimpleCursorAdapter(MainActivity.this,
                    android.R.layout.simple_list_item_1,
                    cursor,
                    new String[]{"TITLE"},
                    new int[]{android.R.id.text1}, 0);
            listNotes.setAdapter(adapter);

            listNotes.setOnItemClickListener((listView, v, position, id) -> {
                Intent intent = new Intent(MainActivity.this, EditActivity.class);
                intent.putExtra(EditActivity.EXTRA_NOTE, (int) id);
                startActivity(intent);
            });

            listNotes.setOnItemLongClickListener((parent, view, position, id) -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Удалить запись?")
                        .setPositiveButton("Ок", (dialogInterface, i) -> {
                            db.delete("NOTE", "_id = ?", new String[]
                                    {String.valueOf(cursor.getInt(cursor.getColumnIndex("_id")))});
                            recreate();
                        })
                        .setNegativeButton("Отмена", (dialog, i) -> dialog.cancel())
                        .show();
                return true;
            });

        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database unavailable",
                    Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    public void openCreate(View view) {
        Intent intent = new Intent(MainActivity.this, CreateActivity.class);
        startActivity(intent);
    }

    public void onRestart() {
        super.onRestart();
        try {
            databaseHelper = new DatabaseHelper(this);
            db = databaseHelper.getWritableDatabase();
            cursor = db.query("DRINK",
                    new String[]{"_id", "NAME"},
                    null,
                    null, null, null, null);
        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database unavailable",
                    Toast.LENGTH_SHORT);
            toast.show();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (cursor != null)
            cursor.close();
        if (db != null)
            db.close();
    }

}