package com.example.rotem.beats;

import android.app.Application;
import android.content.Context;

/**
 * Created by Rotem on 25/06/2016.
 */
public class MyApplication extends Application {
    private static Context context;
    private static String currentUser;

    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();
    }

    public static Context getAppContext() {
        return MyApplication.context;
    }

    public static String getCurrentUser() {
        return currentUser;
    }

    public static void setCurrentUser(String currentUser) {
        MyApplication.currentUser = currentUser;
    }
}
