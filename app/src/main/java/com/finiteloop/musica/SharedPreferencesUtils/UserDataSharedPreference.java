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
    private static final String SHARED_PREFERENCES_PROFILE_PIC_URL = "profile_pic";
    private static final String SHARED_PREFERENCES_DESCRIPTION = "description";

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

    /**
     * Change the Email in SharedPreferences
     */
    public static void setProfilePicURL(Context context, String profilePicPath) {
        //Get shared preferences file
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);

        //Get editor
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //Add username
        editor.putString(SHARED_PREFERENCES_PROFILE_PIC_URL, profilePicPath);

        editor.commit();
    }

    /**
     * Get the Email from SharedPreferences
     */
    public static String getProfileURL(Context context) {
        //Get shared preferences file
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
        return sharedPreferences.getString(SHARED_PREFERENCES_PROFILE_PIC_URL, null);
    }

    /**
     * Change the Description in SharedPreferences
     */
    public static void setDescription(Context context, String description) {
        //Get shared preferences file
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);

        //Get editor
        SharedPreferences.Editor editor = sharedPreferences.edit();

        //Add username
        editor.putString(SHARED_PREFERENCES_DESCRIPTION, description);

        editor.commit();
    }

    /**
     * Get the Description from SharedPreferences
     */
    public static String getDescription(Context context) {
        //Get shared preferences file
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);
        return sharedPreferences.getString(SHARED_PREFERENCES_DESCRIPTION, null);
    }

    public static void removeAllSharedPreferences(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences(SHARED_PREFERENCES_FILE, Context.MODE_PRIVATE);

        //Get editor
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.commit();
    }
}
