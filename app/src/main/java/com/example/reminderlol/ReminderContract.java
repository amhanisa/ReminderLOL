package com.example.reminderlol;

import android.provider.BaseColumns;

public class ReminderContract {

    private ReminderContract(){}

    public static final class NoteEntry implements BaseColumns{
        public static final String TABLE_NAME = "reminderNotes";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_NOTE = "note";
        public static final String COLUMN_TIMESTAMP = "timestamp";
        public static final String COLUMN_NOTIFICATION = "notification";
    }
}
