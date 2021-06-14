package com.nurgulmantarci.mynotesappjava.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.nurgulmantarci.mynotesappjava.R;
import com.nurgulmantarci.mynotesappjava.noteData.NoteContract;

public class CategoryCursorAdapter extends CursorAdapter {

    public CategoryCursorAdapter(Context context, Cursor c, boolean autoRequery) {
        super(context, c, autoRequery);
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {
        View view= LayoutInflater.from(context).inflate(R.layout.item_category,parent,false);
        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        TextView txtCategory=view.findViewById(R.id.txtCategory);
        String categoryName=cursor.getString(cursor.getColumnIndex(NoteContract.CategoryEntry.COLUMN_CATEGORY)); //getString(1)
        txtCategory.setText(categoryName);
    }
}
