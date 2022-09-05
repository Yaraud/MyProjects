package com.hfad.notes;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class EditActivity extends AppCompatActivity {

    public static final String EXTRA_NOTE = "one_note";
    int noteId = 0;
    public Menu main_menu;
    SQLiteDatabase db;
    SQLiteOpenHelper databaseHelper;
    EditText title, description;
    Cursor cursor;
    Intent intent;

    public void setDelVisibility(Boolean b) {
        if (main_menu != null)
            main_menu.findItem(R.id.delete_note).setVisible(b);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
        setDelVisibility(true);
        noteId = (Integer) getIntent().getExtras().get(EXTRA_NOTE);
        title = findViewById(R.id.titleText);
        description = findViewById(R.id.mainText);
        try {
            databaseHelper = new DatabaseHelper(this);
            db = databaseHelper.getWritableDatabase();
            cursor = db.query("NOTE",
                    new String[]{"TITLE", "DESCRIPTION"},
                    "_id = ?",
                    new String[]{Integer.toString(noteId)},
                    null, null, null);
            if (cursor.moveToFirst()) {
                String titleText = cursor.getString(0);
                String descriptionText = cursor.getString(1);
                title.setText(titleText);
                description.setText(descriptionText);
            }
        } catch (SQLiteException e) {
            Toast toast = Toast.makeText(this, "Database unavailable",
                    Toast.LENGTH_SHORT);
            toast.show();
        }

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.add_note) {

            ContentValues noteValues = new ContentValues();
            noteValues.put("TITLE", String.valueOf(title.getText()));
            noteValues.put("DESCRIPTION", String.valueOf(description.getText()));
            if (cursor.moveToFirst())
                db.update("NOTE", noteValues, "_id = ?",
                        new String[]{Integer.toString(noteId)});
            cursor.close();
            db.close();
            intent = new Intent(EditActivity.this, MainActivity.class);
            startActivity(intent);
        } else
        if (item.getItemId() == R.id.delete_note) {
            db.delete("NOTE", "_id = ?",
                    new String[]{Integer.toString(noteId)});
            cursor.close();
            db.close();
            intent = new Intent(EditActivity.this, MainActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        main_menu = menu;
        setDelVisibility(true);
        return super.onCreateOptionsMenu(menu);
    }


}