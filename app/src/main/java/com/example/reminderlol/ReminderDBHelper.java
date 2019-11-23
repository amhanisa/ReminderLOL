package com.example.reminderlol;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.reminderlol.ReminderContract.*;

public class ReminderDBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "reminderNote.db";
    public static final int DATABASE_VERSION = 1;


    public ReminderDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_REMINDERNOTE_TABLE = "CREATE TABLE " +
                NoteEntry.TABLE_NAME + " (" +
                NoteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                NoteEntry.COLUMN_TITLE + " TEXT NOT NULL, " +
                NoteEntry.COLUMN_NOTE + " TEXT, " +
                NoteEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP," +
                NoteEntry.COLUMN_NOTIFICATION + " TIMESTAMP" +
                ");";

        sqLiteDatabase.execSQL(SQL_CREATE_REMINDERNOTE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + NoteEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }
}
