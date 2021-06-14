package com.nurgulmantarci.mynotesappjava.helper;

import android.content.Context;
import android.content.SharedPreferences;

public class MySharedPref {

    static  final String PREF_NAME = "MySharedPref";

    public  void  saveString(Context context, String key, String value){
        SharedPreferences settings = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putString(key,value);
        editor.commit();
    }

    public String getValueString(Context context,String key){
        SharedPreferences settings = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        String text = settings.getString(key,null);
        return text;
    }

}
