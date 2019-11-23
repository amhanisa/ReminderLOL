package com.example.reminderlol;

import android.app.Activity;
import android.app.Notification;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import static com.example.reminderlol.App.CHANNEL_1_ID;
import static com.example.reminderlol.App.CHANNEL_2_ID;

public class MainActivity extends AppCompatActivity {
    private SQLiteDatabase mDatabase;
    private ReminderAdapter reminderAdapter;

    private NotificationManagerCompat notificationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        ReminderDBHelper dbHelper = new ReminderDBHelper(this);
        mDatabase = dbHelper.getWritableDatabase();

        RecyclerView recyclerView = findViewById(R.id.recylerview_remindernote);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        reminderAdapter = new ReminderAdapter(this, getAllItems());
        recyclerView.setAdapter(reminderAdapter);
        notificationManager = NotificationManagerCompat.from(this);

        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,
                ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                removeItem((long) viewHolder.itemView.getTag());
            }
        }).attachToRecyclerView(recyclerView);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Notification notification = new NotificationCompat.Builder(MainActivity.this, CHANNEL_1_ID)
                        .setSmallIcon(R.drawable.ic_launcher_background)
                        .setContentTitle("ads")
                        .setContentText("asd")
                        .build();

                notificationManager.notify(1, notification);
                Intent addNote = new Intent(MainActivity.this, AddNote.class);
                startActivityForResult(addNote, 0);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 0 && resultCode == Activity.RESULT_OK) {
            reminderAdapter.swapCursor(getAllItems());
            reminderAdapter.notifyDataSetChanged();
            Toast.makeText(this, "Added", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private Cursor getAllItems() {
        return mDatabase.query(
                ReminderContract.NoteEntry.TABLE_NAME,
                null, null, null, null, null,
                ReminderContract.NoteEntry.COLUMN_TIMESTAMP + " DESC"
        );
    }

    private void removeItem(long id){
        mDatabase.delete(ReminderContract.NoteEntry.TABLE_NAME,
                ReminderContract.NoteEntry._ID + "=" + id, null);
        reminderAdapter.swapCursor(getAllItems());
    }

}
