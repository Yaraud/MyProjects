package com.hfad.notes;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class CreateActivity extends AppCompatActivity {

    int noteId = 0;
    public Menu main_menu;
    SQLiteDatabase db;
    SQLiteOpenHelper databaseHelper;
    EditText title, description;

    public void setDelVisibility(Boolean b) {
        if (main_menu != null)
            main_menu.findItem(R.id.delete_note).setVisible(b);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {

        if (item.getItemId() == R.id.add_note) {
            try {

                title = findViewById(R.id.titleText);
                description = findViewById(R.id.mainText);

                databaseHelper = new DatabaseHelper(this);
                db = databaseHelper.getWritableDatabase();

                ContentValues noteValues = new ContentValues();
                noteValues.put("TITLE", String.valueOf(title.getText()));
                noteValues.put("DESCRIPTION", String.valueOf(description.getText()));
                db.insert("NOTE", null, noteValues);
                db.close();
                Intent intent = new Intent(CreateActivity.this, MainActivity.class);
                startActivity(intent);
            } catch (SQLiteException exception) {
                Toast toast = Toast.makeText(this, "Database unavailable", Toast.LENGTH_SHORT);
                toast.show();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        main_menu = menu;
        setDelVisibility(false);
        return super.onCreateOptionsMenu(menu);
    }
}