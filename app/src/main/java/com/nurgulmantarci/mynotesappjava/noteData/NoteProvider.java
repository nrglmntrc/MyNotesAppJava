package com.nurgulmantarci.mynotesappjava.noteData;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class NoteProvider extends ContentProvider {

    private static final UriMatcher matcher=new UriMatcher(UriMatcher.NO_MATCH);
    private static final int URICODE_NOTES=1;
    private static final int URICODE_CATEGORIES=2;
    static {
        matcher.addURI(NoteContract.CONTENT_AUTHORITY,NoteContract.PATH_NOTES,URICODE_NOTES);
        matcher.addURI(NoteContract.CONTENT_AUTHORITY,NoteContract.PATH_CATEGORIES,URICODE_CATEGORIES);
    }
    private SQLiteDatabase db;
    private NoteDatabaseHelper noteDatabaseHelper;

    @Override
    public boolean onCreate() {
        noteDatabaseHelper=new NoteDatabaseHelper(getContext());
        db=noteDatabaseHelper.getWritableDatabase();
        return true;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor cursor;
        SQLiteQueryBuilder queryBuilder;
        String tabloBirlestir = "notes inner join categories on notes.categoryID=categories._id";       //Notlar gösterilirken 2 tablonun birbirine bağlanması gerekir..çünkü kategory Id yerine kategory nin ismi gelsin isteriz..
        switch (matcher.match(uri)) {
            case URICODE_NOTES:
                queryBuilder = new SQLiteQueryBuilder();
                queryBuilder.setTables(tabloBirlestir);  //2 tablo virgüller birleştirilebilir yada burdaki gibi inner join ile birleştirilebilir.
                cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            case URICODE_CATEGORIES:
                cursor = db.query(NoteContract.CategoryEntry.TABLE_NAME, projection, selection, selectionArgs, null, null, sortOrder);
                break;
            default:
                throw new IllegalArgumentException("Bilinmeyen URI"+uri);
        }
        cursor.setNotificationUri(getContext().getContentResolver(),uri); //uri ile gelen veri değişiklişini dinler..
        return cursor;
    }


    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        switch (matcher.match(uri)){
            case URICODE_NOTES:
                return kayitEkle(NoteContract.NotesEntry.TABLE_NAME,values,uri);
            case URICODE_CATEGORIES:
                return kayitEkle(NoteContract.CategoryEntry.TABLE_NAME,values,uri);
            default:
                throw new IllegalArgumentException("Bilinmeyen Uri" +uri);
        }
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
       switch (matcher.match(uri)){
           case URICODE_NOTES:
               return kayitSil(NoteContract.NotesEntry.TABLE_NAME,selection,selectionArgs,uri);
           case URICODE_CATEGORIES:
               return kayitSil(NoteContract.CategoryEntry.TABLE_NAME,selection,selectionArgs,uri);
           default:
               throw new IllegalArgumentException("Bilinmeyen Update URI"+uri);
       }
    }


    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        switch (matcher.match(uri)){
            case URICODE_NOTES:
                return kayitGuncelle(NoteContract.NotesEntry.TABLE_NAME,values,selection,selectionArgs,uri);
            case URICODE_CATEGORIES:
                return kayitGuncelle(NoteContract.CategoryEntry.TABLE_NAME,values,selection,selectionArgs,uri);
            default:
                throw new IllegalArgumentException("Bilinmeyen Update URI"+uri);
        }
    }

    private Uri kayitEkle(String tabloAdi, ContentValues values, Uri uri){
        long id=db.insert(tabloAdi,null,values);
        if (id==-1){
            Log.e("NoteProvider","INSERT HATA"+uri);
            return null;
        }
        getContext().getContentResolver().notifyChange(uri,null);  //bir kayıt ekleme işleminde query methodu bunları dinler ve  cursor u tekrar oluşturur.
        return ContentUris.withAppendedId(uri,id);   //kendisine gelen Uri nin sonuna Id ekler.
    }

    private int kayitGuncelle(String tabloAdi, ContentValues values, String selection, String[] selectionArgs, Uri uri){
        int islemSayisi=db.update(tabloAdi,values,selection,selectionArgs);
        if(islemSayisi==0){
            Log.e("HATA","UPDATE HATA"+uri);
            return -1;
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return islemSayisi;
    }

    private int kayitSil(String tableName, String selection, String[] selectionArgs, Uri uri) {
        int islemSayisi=db.delete(tableName,selection,selectionArgs);
        if(islemSayisi==0){
            Log.e("HATA","DELETE HATA "+uri);
            return -1;
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return islemSayisi;
    }

}
