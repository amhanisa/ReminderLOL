package com.example.reminderlol;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ReminderViewHolder> {
    private Context mContext;
    private Cursor mCursor;

    public ReminderAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
    }

    public class ReminderViewHolder extends RecyclerView.ViewHolder {
        public TextView txtTitle, txtNote, txtDate, txtTimestamp;

        public ReminderViewHolder(@NonNull View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtNote = itemView.findViewById(R.id.txtNote);
            txtDate = itemView.findViewById(R.id.txtDate);
            txtTimestamp = itemView.findViewById(R.id.txtTimeStamp);
        }
    }

    @NonNull
    @Override
    public ReminderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.note_card, parent, false);
        return new ReminderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReminderViewHolder holder, int position) {
        if (!mCursor.moveToPosition(position)) {
            return;
        }

        long id = mCursor.getLong(mCursor.getColumnIndex(ReminderContract.NoteEntry._ID));
        String title = mCursor.getString(mCursor.getColumnIndex(ReminderContract.NoteEntry.COLUMN_TITLE));
        String note = mCursor.getString(mCursor.getColumnIndex(ReminderContract.NoteEntry.COLUMN_NOTE));
        String date = mCursor.getString(mCursor.getColumnIndex(ReminderContract.NoteEntry.COLUMN_NOTIFICATION));
        String timestamp = mCursor.getString(mCursor.getColumnIndex(ReminderContract.NoteEntry.COLUMN_TIMESTAMP));

        holder.itemView.setTag(id);
        holder.txtTitle.setText(title);
        holder.txtNote.setText(note);
        holder.txtDate.setText(date);
        holder.txtTimestamp.setText(timestamp);
    }

    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    public void swapCursor(Cursor newCursor) {
        if (mCursor != null) {
            mCursor.close();
        }

        mCursor = newCursor;

        if (newCursor != null) {
            notifyDataSetChanged();
        }
    }
}
