package com.example.reminderlol;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.DialogFragment;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class AddNote extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    private SQLiteDatabase mDatabase;
    private TextView txtDate, txtTime;
    private EditText inputTitle, inputNote;
    private Button btnAdd;
    private Calendar inputCalendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_detail);


        Intent viewNote = getIntent();
        final long id = viewNote.getLongExtra("id", 0);


        ReminderDBHelper dbHelper = new ReminderDBHelper(this);
        mDatabase = dbHelper.getWritableDatabase();

        txtDate = findViewById(R.id.txtDate);
        txtTime = findViewById(R.id.txtTime);
        inputTitle = findViewById(R.id.inputTitle);
        inputNote = findViewById(R.id.inputNote);
        btnAdd = findViewById(R.id.btnAdd);

        inputCalendar = Calendar.getInstance();

        if (id >= 0) {
            getNoteDetail(id);
        } else {
            setReminderTime();
        }


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
                if (inputTitle.getText().toString().trim().length() == 0 && inputNote.getText().toString().trim().length() == 0) {
                    return;
                }
                if (id > 0) {
                    updateItem(id);
                } else {
                    addItem();
                }

                addNotification();
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
        inputCalendar.set(Calendar.SECOND, 0);

        txtTime.setText(DateFormat.format("HH:mm", inputCalendar).toString());
    }

    @Override
    public void onDateSet(DatePicker datePicker, int year, int month, int day) {
        inputCalendar.set(year, month, day);
        txtDate.setText(DateFormat.format("yyyy-MM-dd", inputCalendar).toString());
    }

    public void setReminderTime() {
        inputCalendar.getTime();
        txtTime.setText(DateFormat.format("HH:mm", inputCalendar).toString());
        txtDate.setText(DateFormat.format("yyyy-MM-dd", inputCalendar).toString());
    }

    public void addItem() {
        if (inputTitle.getText().toString().trim().length() == 00 && inputNote.getText().toString().trim().length() == 0) {
            return;
        }

        String title = inputTitle.getText().toString();
        String note = inputNote.getText().toString();
        String date = DateFormat.format("yyyy-MM-dd HH:mm", inputCalendar).toString();

        ContentValues cv = new ContentValues();
        cv.put(ReminderContract.NoteEntry.COLUMN_TITLE, title);
        cv.put(ReminderContract.NoteEntry.COLUMN_NOTE, note);
        cv.put(ReminderContract.NoteEntry.COLUMN_NOTIFICATION, date);

        mDatabase.insert(ReminderContract.NoteEntry.TABLE_NAME, null, cv);
    }

    public void updateItem(long id) {
        if (inputTitle.getText().toString().trim().length() == 00 && inputNote.getText().toString().trim().length() == 0) {
            return;
        }

        String title = inputTitle.getText().toString();
        String note = inputNote.getText().toString();
        String date = DateFormat.format("yyyy-MM-dd HH:mm", inputCalendar).toString();

        ContentValues cv = new ContentValues();
        cv.put(ReminderContract.NoteEntry.COLUMN_TITLE, title);
        cv.put(ReminderContract.NoteEntry.COLUMN_NOTE, note);
        cv.put(ReminderContract.NoteEntry.COLUMN_NOTIFICATION, date);

        mDatabase.update(ReminderContract.NoteEntry.TABLE_NAME,
                cv, ReminderContract.NoteEntry._ID + " = " + id, null);

    }

    public void addNotification() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        intent.putExtra("title", inputTitle.getText().toString());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, inputCalendar.getTimeInMillis(), pendingIntent);
    }

    public void deleteNotification() {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        intent.putExtra("title", inputTitle.getText().toString());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
        alarmManager.cancel(pendingIntent);
    }

    public void getNoteDetail(long id) {
        Cursor cursor = mDatabase.query(
                ReminderContract.NoteEntry.TABLE_NAME,
                null,
                ReminderContract.NoteEntry._ID + " = " + id,
                null,
                null,
                null,
                null
        );
        String title = null;
        String note = null;
        String notification = null;
        try {
            while (cursor.moveToNext()) {
                title = cursor.getString(cursor.getColumnIndex(ReminderContract.NoteEntry.COLUMN_TITLE));
                note = cursor.getString(cursor.getColumnIndex(ReminderContract.NoteEntry.COLUMN_NOTE));
                notification = cursor.getString(cursor.getColumnIndex(ReminderContract.NoteEntry.COLUMN_NOTIFICATION));
            }
        } finally {
            cursor.close();
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            inputCalendar.setTime(sdf.parse(notification));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        inputTitle.setText(title);
        inputNote.setText(note);
        txtDate.setText(DateFormat.format("yyyy-MM-dd", inputCalendar).toString());
        txtTime.setText(DateFormat.format("HH:mm", inputCalendar).toString());
    }
}
