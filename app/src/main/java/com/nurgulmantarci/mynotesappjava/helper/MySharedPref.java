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

    public  void  saveBoolean(Context context,String key, Boolean value){
        SharedPreferences settings = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.putBoolean(key,value);
        editor.commit();
    }

    public Boolean getValueBoolean(Context context, String key){
        SharedPreferences settings = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        Boolean text = settings.getBoolean(key,false);
        return text;
    }
    public Integer getValueInt(Context context,String key){
        SharedPreferences settings = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        Integer text = settings.getInt(key,0);
        return text;
    }
    public void clear(Context context){
        SharedPreferences settings = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.clear();
        editor.commit();
    }
    public void remove(Context context,String key){
        SharedPreferences settings = context.getSharedPreferences(PREF_NAME,Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = settings.edit();
        editor.remove(key);
        editor.commit();
    }

}
