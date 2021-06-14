package com.nurgulmantarci.mynotesappjava.helper;

import android.content.Context;


public class UserInformationHelper {

    private static final MySharedPref mySharedPref=new MySharedPref();

    public static void saveUserEmail(Context context,String user_email) {
        mySharedPref.saveString(context,"user_email",user_email);
    }

    public static String getUserEmail(Context context){
        String name=mySharedPref.getValueString(context,"user_email");
        return name;
    }

    public static void deleteUserEmail(Context context) {
        mySharedPref.saveString(context,"user_email","");
    }





}
