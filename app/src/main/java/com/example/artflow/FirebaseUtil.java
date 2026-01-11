package com.example.artflow;

import android.app.Application;
import android.util.Log;

import com.google.firebase.FirebaseApp;

public class FirebaseUtil extends Application {
    private static final String TAG = "FirebaseUtil";

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "Initializing Firebase...");
        FirebaseApp.initializeApp(this);
    }
}