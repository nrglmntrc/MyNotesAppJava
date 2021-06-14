package com.nurgulmantarci.mynotesappjava.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nurgulmantarci.mynotesappjava.R;
import com.nurgulmantarci.mynotesappjava.activities.AddNoteActivity;
import com.nurgulmantarci.mynotesappjava.adapters.NoteCursorAdapter;
import com.nurgulmantarci.mynotesappjava.helper.UserInformationHelper;
import com.nurgulmantarci.mynotesappjava.noteData.NoteContract;


public class DoneFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    Context context;
    FloatingActionButton fab;
    NoteCursorAdapter adapterNote;
    Cursor cursorNote;
    ListView listViewNotes;
    TextView txtNoteAlert;

    public DoneFragment() {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        context = container.getContext();
        return inflater.inflate(R.layout.fragment_done, container, false);

    }

    @SuppressLint("RestrictedApi")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        fab=getActivity().findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context, AddNoteActivity.class);
                startActivity(intent);
            }
        });

        txtNoteAlert=view.findViewById(R.id.txtNoteAlert);

        listViewNotes = getActivity().findViewById(R.id.listViewNotes);
        getLoaderManager().initLoader(100, null, this);
        adapterNote = new NoteCursorAdapter(context, cursorNote, false,txtNoteAlert);
        listViewNotes.setAdapter(adapterNote);

    }



    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {
        String email= UserInformationHelper.getUserEmail(getContext());
        if(id==100){
            String[] projection={NoteContract.NotesEntry.TABLE_NAME+"."+ NoteContract.NotesEntry._ID,
                    NoteContract.NotesEntry.COLUMN_NOTE_CONTENT,
                    NoteContract.NotesEntry.COLUMN_EMAIL,
                    NoteContract.NotesEntry.COLUMN_IMAGE,
                    NoteContract.NotesEntry.COLUMN_CREATE_TIME, NoteContract.NotesEntry.COLUMN_FINISH_TIME,
                    NoteContract.NotesEntry.COLUMN_DONE, NoteContract.NotesEntry.COLUMN_CATEGORY_ID, NoteContract.CategoryEntry.COLUMN_CATEGORY};
            String selection= NoteContract.NotesEntry.COLUMN_CATEGORY_ID+"=? AND " + NoteContract.NotesEntry.COLUMN_EMAIL+"=? " ;
            String[] selectionArgs={String.valueOf(3),email};
            return new CursorLoader(context, NoteContract.NotesEntry.CONTENT_URI,projection,selection,selectionArgs, NoteContract.NotesEntry.TABLE_NAME+"."+ NoteContract.NotesEntry._ID+" DESC");
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



