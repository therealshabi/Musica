package com.finiteloop.musica.SharedPreferencesUtils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by therealshabi on 16/04/17.
 */

public class UserDataSharedPreference {
    private static final String SHARED_PREFERENCES_FILE = "preferences";
    private static final String SHARED_PREFERENCES_USERNAME = "username";
    private static final String SHARED_PREFERENCES_EMAIL = "email";

    /**
     * Change the Username in SharedPreferences
     */
    public static void setUsername(Context context, String username) {
        //Get shared preferences file
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);

        //Get editor
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //Add username
        editor.putString(SHARED_PREFERENCES_USERNAME, username);

        editor.commit();
    }

    /**
     * Get the Username from SharedPreferences
     */
    public static String getUsername(Context context) {
        //Get shared preferences file
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
        return sharedPreferences.getString(SHARED_PREFERENCES_USERNAME, null);
    }

    /**
     * Change the Email in SharedPreferences
     */
    public static void setEmail(Context context, String email) {
        //Get shared preferences file
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);

        //Get editor
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //Add username
        editor.putString(SHARED_PREFERENCES_EMAIL, email);

        editor.commit();
    }

    /**
     * Get the Email from SharedPreferences
     */
    public static String getEmail(Context context) {
        //Get shared preferences file
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
        return sharedPreferences.getString(SHARED_PREFERENCES_EMAIL, null);
    }
}
