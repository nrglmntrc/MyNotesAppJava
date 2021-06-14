package com.nurgulmantarci.mynotesappjava.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nurgulmantarci.mynotesappjava.R;
import com.nurgulmantarci.mynotesappjava.activities.EditNoteActivity;
import com.nurgulmantarci.mynotesappjava.noteData.NoteContract;


public class NoteCursorAdapter extends CursorAdapter {

    TextView txtAlert;

    public NoteCursorAdapter(Context context, Cursor c, boolean autoRequery, TextView txtAlert) {
        super(context, c, autoRequery);
        this.txtAlert=txtAlert;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        return LayoutInflater.from(context).inflate(R.layout.item_note,parent,false);
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor)
    {

        String noteContent=cursor.getString(cursor.getColumnIndex(NoteContract.NotesEntry.COLUMN_NOTE_CONTENT));
        int noteId=cursor.getInt(cursor.getColumnIndex(NoteContract.NotesEntry._ID));
        int categoryId=cursor.getInt(cursor.getColumnIndex(NoteContract.NotesEntry.COLUMN_CATEGORY_ID));
        byte[] bytes=cursor.getBlob(cursor.getColumnIndex(NoteContract.NotesEntry.COLUMN_IMAGE));

        if(txtAlert.getVisibility()==View.VISIBLE){
            txtAlert.setVisibility(View.GONE);
        }

        TextView txtNote=view.findViewById(R.id.txtNote);
        LinearLayout linearNote=view.findViewById(R.id.linearNote);
        ImageView imageNote=view.findViewById(R.id.imageNote);


        txtNote.setText(noteContent);

        Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
        imageNote.setImageBitmap(bitmap);


        linearNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, EditNoteActivity.class);
                intent.putExtra("note_id",noteId);
                intent.putExtra("category_id",categoryId);
                intent.putExtra("image_bytes",bytes);
                intent.putExtra("note_content",noteContent);
                context.startActivity(intent);
            }
        });


    }
}
