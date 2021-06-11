package com.nurgulmantarci.mynotesappjava.noteData;

import android.net.Uri;
import android.provider.BaseColumns;

public class NoteContract {

    public static final String CONTENT_AUTHORITY="com.nurgulmantarci.mynotesappjava.noteprovider";
    public static final String PATH_NOTES="notes";   //Notlar verisinin tutulduğu tablo adı.
    public static final String PATH_CATEGORIES="categories";
    public static final Uri BASE_CONTENT_URI= Uri.parse("content://"+CONTENT_AUTHORITY);

    public static final class NotesEntry implements BaseColumns {
        //content provider için olan tanımlamalar.
        public static final Uri CONTENT_URI= Uri.withAppendedPath(BASE_CONTENT_URI,PATH_NOTES); //  "content://com.nurgulmantarci.mynotesappjava.noteprovider/notes

        //Sutun adları
        public static final String TABLE_NAME="notes";
        public static final String _ID= BaseColumns._ID;
        public static final String COLUMN_NOTE_CONTENT="note";
        public static final String COLUMN_CREATE_TIME="oluşturulmaTarihi";
        public static final String COLUMN_FINISH_TIME="bitişTarihi";
        public static final String COLUMN_DONE="Yapıldı";
        public static final String COLUMN_CATEGORY_ID="categoryID";
    }

    public static final class CategoryEntry implements BaseColumns {
        public static final Uri CONTENT_URI= Uri.withAppendedPath(BASE_CONTENT_URI,PATH_CATEGORIES);

        public static final String TABLE_NAME="categories";
        public static final String _ID= BaseColumns._ID;
        public static final String COLUMN_CATEGORY="category";
    }
}
