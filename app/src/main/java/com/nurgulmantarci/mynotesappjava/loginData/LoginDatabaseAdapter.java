package com.nurgulmantarci.mynotesappjava.loginData;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.HashMap;

public class LoginDatabaseAdapter {

    static final String DATABASE_NAME = "login.db";
    static final int DATABASE_VERSION = 1;
    public static final int NAME_COLUMN = 3;

    static final String DATABASE_CREATE = "create table "+"LOGIN"+
            "( " +"ID integer primary key autoincrement,"+ "PASSWORD  text,"+ "EMAIL text) ";

    public SQLiteDatabase db;
    private final Context context;
    private DatabaseHelper dbHelper;

    public  LoginDatabaseAdapter(Context _context)
    {
        context = _context;
        dbHelper = new DatabaseHelper(context, DATABASE_NAME, null, DATABASE_VERSION);

    }

    public  LoginDatabaseAdapter open() throws SQLException
    {
        db = dbHelper.getWritableDatabase();
        return this;
    }
    public void close()
    {
        db.close();
    }

    public  SQLiteDatabase getDatabaseInstance()
    {
        return db;
    }

    public void insertEntry(String password,String email)
    {
        ContentValues newValues = new ContentValues();
        newValues.put("PASSWORD", password);
        newValues.put("EMAIL",email);

        db.insert("LOGIN", null, newValues);
    }

    public int deleteEntry(String password)
    {
        String where="PASSWORD=?";
        int numberOFEntriesDeleted= db.delete("LOGIN", where, new String[]{password}) ;
        return numberOFEntriesDeleted;
    }

    public boolean controlEmailIsExist(String email)
    {
        Cursor cursor=db.query("LOGIN", null, " EMAIL=?", new String[]{email}, null, null, null);
        if(cursor.getCount()<1) // UserName Not Exist
        {
            cursor.close();
            return false;
        }else{
            cursor.close();
            return true;
        }
    }

    public ArrayList<String> getSingleEntry(String password)
    {
        ArrayList<String> saved_mails=new ArrayList<>();
        Cursor cursor=db.query("LOGIN", null, " PASSWORD=?", new String[]{password}, null, null, null);
        if(cursor.getCount()<1) // UserName Not Exist
        {
            cursor.close();
            saved_mails.add("NOT EXIST");
            return saved_mails;
        }

       // cursor.moveToFirst();
        while(cursor.moveToNext()){
            String email= cursor.getString(cursor.getColumnIndex("EMAIL"));
            saved_mails.add(email);
        }
        cursor.close();

        return saved_mails;
    }





    public String getAllTags(String a) {

        Cursor c = db.rawQuery("SELECT * FROM " + "LOGIN" + " where EMAIL = '" +a + "'" , null);
        String str = null;
        if (c.moveToFirst()) {
            do {
                str = c.getString(c.getColumnIndex("PASSWORD"));
            } while (c.moveToNext());
        }
        return str;
    }

    public HashMap<String, String> getNoteInfo(String id) {
        HashMap<String, String> wordList = new HashMap<String, String>();
        String selectQuery = "SELECT * FROM LOGIN where EMAIL='"+id+"'";
        Cursor cursor = db.rawQuery(selectQuery, null);
        if (cursor.moveToFirst()) {
            do {
                wordList.put("PASSWORD", cursor.getString(1));
            } while (cursor.moveToNext());
        }
        return wordList;
    }

}
