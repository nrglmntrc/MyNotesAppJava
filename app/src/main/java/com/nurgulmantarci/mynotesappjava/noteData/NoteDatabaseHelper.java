package com.nurgulmantarci.mynotesappjava.noteData;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.nurgulmantarci.mynotesappjava.noteData.NoteContract.CategoryEntry;
import com.nurgulmantarci.mynotesappjava.noteData.NoteContract.NotesEntry;


public class NoteDatabaseHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME="note.db";
    private static final int DATABASE_VERSION=2;
    private static final String TABLE_CATEGORİES_CREATE=
            "CREATE TABLE " + CategoryEntry.TABLE_NAME + " (" +
                              CategoryEntry._ID + " INTEGER PRIMARY KEY, " +
                              CategoryEntry.COLUMN_CATEGORY + " TEXT)";

    private static final String TABLE_NOTES_CREATE=
            "CREATE TABLE " + NotesEntry.TABLE_NAME + " (" +
                              NotesEntry._ID + " INTEGER PRIMARY KEY," +
                              NotesEntry.COLUMN_NOTE_CONTENT + " TEXT," +
                              NotesEntry.COLUMN_EMAIL + " TEXT," +
                              NotesEntry.COLUMN_IMAGE + " BLOB," + NotesEntry.COLUMN_CREATE_TIME + " TEXT DEFAULT CURRENT_TIMESTAMP, " +
                              NotesEntry.COLUMN_FINISH_TIME + " TEXT," + NotesEntry.COLUMN_DONE +" INTEGER, " +
                              NotesEntry.COLUMN_CATEGORY_ID + " INTEGER," +
                              " FOREIGN KEY (" + NotesEntry.COLUMN_CATEGORY_ID + ") REFERENCES " + CategoryEntry.TABLE_NAME +"("+CategoryEntry._ID+"))";

    public NoteDatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
            db.execSQL(TABLE_CATEGORİES_CREATE);
            db.execSQL(TABLE_NOTES_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + CategoryEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " +NotesEntry.TABLE_NAME);
        onCreate(db);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        db.setForeignKeyConstraintsEnabled(true); //olmayan kategoriye not eklenemez ve bi kategori silinirse tüm notları da silinir..
    }
}
