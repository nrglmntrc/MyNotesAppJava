package com.nurgulmantarci.mynotesappjava.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nurgulmantarci.mynotesappjava.R;
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

        if(txtAlert.getVisibility()==View.VISIBLE){
            txtAlert.setVisibility(View.GONE);
        }

        TextView txtNote=view.findViewById(R.id.txtNote);
        String noteContent=cursor.getString(cursor.getColumnIndex(NoteContract.NotesEntry.COLUMN_NOTE_CONTENT));
        txtNote.setText(noteContent);
        ImageView imageNote=view.findViewById(R.id.imageNote);
        int imageIx=cursor.getColumnIndex(NoteContract.NotesEntry.COLUMN_IMAGE);
        byte[] bytes=cursor.getBlob(imageIx);
        Bitmap bitmap= BitmapFactory.decodeByteArray(bytes,0,bytes.length);
        imageNote.setImageBitmap(bitmap);


    }
}
