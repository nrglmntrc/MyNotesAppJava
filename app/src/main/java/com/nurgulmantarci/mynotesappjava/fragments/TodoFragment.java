package com.nurgulmantarci.mynotesappjava.fragments;

import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nurgulmantarci.mynotesappjava.R;
import com.nurgulmantarci.mynotesappjava.activities.AddNoteActivity;
import com.nurgulmantarci.mynotesappjava.activities.MainActivity;
import com.nurgulmantarci.mynotesappjava.adapters.NoteCursorAdapter;
import com.nurgulmantarci.mynotesappjava.noteData.NoteContract.NotesEntry;
import com.nurgulmantarci.mynotesappjava.noteData.NoteContract.CategoryEntry;

public class TodoFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    Context context;
    FloatingActionButton fab;
    NoteCursorAdapter adapterNote;
    Cursor cursorNote;
    private static final int ALL_NOTES = -1;
    ListView listViewNotes;

    public TodoFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = container.getContext();
        return inflater.inflate(R.layout.fragment_to_do, container, false);

    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fab = getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, AddNoteActivity.class);
                startActivity(intent);
            }
        });




        listViewNotes = getActivity().findViewById(R.id.listViewNotes);
        getLoaderManager().initLoader(100, null, this);
        adapterNote = new NoteCursorAdapter(context, cursorNote, false);
        listViewNotes.setAdapter(adapterNote);

    }



    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        if(id==100){
            String[] projection={NotesEntry.TABLE_NAME+"."+NotesEntry._ID,
                    NotesEntry.COLUMN_NOTE_CONTENT,
                    NotesEntry.COLUMN_EMAIL,
                    NotesEntry.COLUMN_IMAGE,
                    NotesEntry.COLUMN_CREATE_TIME,NotesEntry.COLUMN_FINISH_TIME,
                    NotesEntry.COLUMN_DONE, NotesEntry.COLUMN_CATEGORY_ID,CategoryEntry.COLUMN_CATEGORY};
            String selection=NotesEntry.COLUMN_CATEGORY_ID+"=?";
            String[] selectionArgs={String.valueOf(1)};
            return new CursorLoader(context,NotesEntry.CONTENT_URI,projection,selection,selectionArgs,NotesEntry.TABLE_NAME+"."+NotesEntry._ID+" DESC");
        }else{
            return null;
        }


    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {
        if(loader.getId()==100){
            adapterNote.swapCursor(data);
        }

    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        adapterNote.swapCursor(null);
    }
}





