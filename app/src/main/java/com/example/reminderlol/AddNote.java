package com.example.reminderlol;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;

import java.sql.Time;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddNote extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    private SQLiteDatabase mDatabase;
    private TextView txtDate, txtTime;
    private EditText inputTitle, inputNote;
    private Button btnAdd;
    private Calendar calendar, inputCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_note);

        ReminderDBHelper dbHelper = new ReminderDBHelper(this);
        mDatabase = dbHelper.getWritableDatabase();

        txtDate = findViewById(R.id.txtDate);
        txtTime = findViewById(R.id.txtTime);
        inputTitle = findViewById(R.id.inputTitle);
        inputNote = findViewById(R.id.inputNote);
        btnAdd = findViewById(R.id.btnAdd);

        inputCalendar = Calendar.getInstance();

        setReminderTime();

        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "DATE PICKER");
            }
        });

        txtTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DialogFragment timePicker = new TimePickerFragment();
                timePicker.show(getSupportFragmentManager(), "TIME PICKER");
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(inputTitle.getText().toString().trim().length() == 0 && inputNote.getText().toString().trim().length() == 0){
                    return;
                }

                addItem();

                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int hourOfDay, int minute) {
        inputCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        inputCalendar.set(Calendar.MINUTE, minute);

        txtTime.setText(DateFormat.format("hh:mm", inputCalendar).toString());
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        inputCalendar.set(year, month, day);
        txtDate.setText(DateFormat.format("yyyy-MM-dd", inputCalendar).toString());
    }

    public void setReminderTime() {
        inputCalendar.getTime();
        txtTime.setText(DateFormat.format("hh:mm", inputCalendar).toString());
        txtDate.setText(DateFormat.format("yyyy-MM-dd", inputCalendar).toString());
    }

    public void addItem() {
        if (inputTitle.getText().toString().trim().length() == 00 && inputNote.getText().toString().trim().length() == 0) {
            return;
        }

        String title = inputTitle.getText().toString();
        String note = inputNote.getText().toString();
        String date = DateFormat.format("yyyy-MM-dd hh:mm", inputCalendar).toString();

        ContentValues cv = new ContentValues();
        cv.put(ReminderContract.NoteEntry.COLUMN_TITLE, title);
        cv.put(ReminderContract.NoteEntry.COLUMN_NOTE, note);
        cv.put(ReminderContract.NoteEntry.COLUMN_NOTIFICATION, date);

        mDatabase.insert(ReminderContract.NoteEntry.TABLE_NAME, null, cv);
    }
}
