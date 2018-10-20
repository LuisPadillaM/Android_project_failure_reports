package com.example.luispadilla.projecto_averias.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {
    private static SharedPreferences mPreferences;
    private static String USER_NAME_KEY = "user_name";
    private static String USER_EMAIL_KEY = "user_email";

    public static void savePreferences(Context context, String username, String email){

        SharedPreferences.Editor editor = PreferenceManager.getSharedPreferences(context).edit();
        editor.putString(USER_NAME_KEY, username);
        editor.putString(USER_EMAIL_KEY, email);
        editor.commit();
    }

    static SharedPreferences getSharedPreferences(Context context) {
        return mPreferences == null ? context.getSharedPreferences(Constants.PREFERENCES_FILE, context.MODE_PRIVATE) : mPreferences;
    }

}
