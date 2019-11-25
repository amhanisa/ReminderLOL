package com.example.reminderlol;

import androidx.appcompat.app.AppCompatActivity;

import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

public class ViewNote extends AppCompatActivity {
    private SQLiteDatabase mDatabase;
    private TextView txtDate, txtTime;
    private EditText inputTitle, inputNote;
    private Button btnAdd;
    private Calendar inputCalendar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);

    }
}
