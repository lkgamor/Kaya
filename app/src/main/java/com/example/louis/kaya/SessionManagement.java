package com.example.louis.kaya;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.view.View;
import android.widget.Toast;

/*
 * Created by Louis on 11/7/2017.
 */
public class SessionManagement {

    // Shared Preferences
    SharedPreferences pref;

    // Editor for Shared preferences
    SharedPreferences.Editor editor;

    // Context
    Context _context;

    // View
    View view;

    int internet;

    // Shared pref mode
    int PRIVATE_MODE = 0;

    // Sharedpref file name
    private static final String PREF_NAME = "KAYA_SIGNUP_PREF";

    // All Shared Preferences Keys
    private static final String IS_SIGNED_UP = "IsLoggedIn";

    // User name (make variable public to access from outside)
    public static final String KEY_NAME = "name";

    // Email address (make variable public to access from outside)
    public static final String KEY_EMAIL = "email";

    // Constructor
    public SessionManagement(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public void createSignUpSession(String name, String email) {
        //Saving SIGNUP Value as true
        editor.putBoolean(IS_SIGNED_UP, true);

        //Storing username in PREF
        editor.putString(KEY_NAME, name);

        //Storing password in PREF
        editor.putString(KEY_EMAIL, email);

        //saving changes
        editor.commit();
    }

    public void checkSignUp() {
        //Check login status
        if (!this.isSignedUp()) {
            // user is not logged in redirect him to Login Activity
            Intent i = new Intent(_context, UserSignUp.class);

            // Closing all the Activities || Add new Flag to start new Activity
            i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);

            // Staring Login Activity
            _context.startActivity(i);
        }
    }

    private boolean isSignedUp() {

        return pref.getBoolean(IS_SIGNED_UP, false);
    }

    public int isNetworkAvailable() {

        ConnectivityManager connectivityManager = (ConnectivityManager) _context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo == null || !networkInfo.isAvailable() || !networkInfo.isConnected()) {
            internet = 0;
            Toast.makeText(_context, "No Internet Connection... \nCheck your Data or Wi-fi", Toast.LENGTH_LONG).show();

        } else {
            internet = 1;
        }
        return internet;
    }
}
