package com.example.reminderlol;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class ReminderAdapter extends RecyclerView.Adapter<ReminderAdapter.ReminderViewHolder> {
    private Context mContext;
    private Cursor mCursor;

    public ReminderAdapter(Context context, Cursor cursor) {
        mContext = context;
        mCursor = cursor;
    }

    public class ReminderViewHolder extends RecyclerView.ViewHolder {
        public TextView txtTitle, txtNote, txtDate;
        public CardView noteCard;

        public ReminderViewHolder(@NonNull final View itemView) {
            super(itemView);

            txtTitle = itemView.findViewById(R.id.txtTitle);
            txtNote = itemView.findViewById(R.id.txtNote);
            txtDate = itemView.findViewById(R.id.txtDate);

            noteCard = itemView.findViewById(R.id.cardview_note);
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

        final long id = mCursor.getLong(mCursor.getColumnIndex(ReminderContract.NoteEntry._ID));
        String title = mCursor.getString(mCursor.getColumnIndex(ReminderContract.NoteEntry.COLUMN_TITLE));
        String note = mCursor.getString(mCursor.getColumnIndex(ReminderContract.NoteEntry.COLUMN_NOTE));
        String date = mCursor.getString(mCursor.getColumnIndex(ReminderContract.NoteEntry.COLUMN_NOTIFICATION));

        holder.itemView.setTag(id);
        holder.txtTitle.setText(title);
        holder.txtNote.setText(note);
        holder.txtDate.setText(date);

        holder.noteCard.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
//                Toast.makeText(mContext, Long.toString(id), Toast.LENGTH_LONG).show();
                Intent viewDetail = new Intent(mContext, AddNote.class);
                viewDetail.putExtra("id", id);
                ((Activity) mContext).startActivityForResult(viewDetail, 0);
            }
        });
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
