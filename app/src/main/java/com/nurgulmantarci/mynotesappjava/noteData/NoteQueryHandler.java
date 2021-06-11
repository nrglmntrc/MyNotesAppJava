package com.nurgulmantarci.mynotesappjava.noteData;

import android.content.AsyncQueryHandler;
import android.content.ContentResolver;

public class NoteQueryHandler extends AsyncQueryHandler {
    public NoteQueryHandler(ContentResolver cr) {
        super(cr);
    }
}
