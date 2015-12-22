package com.water.waterreminder;

import android.app.Application;
import android.content.Context;


/**
 * Created by kurayogun on 01/11/15.
 */
public class MainApplication extends Application {
    private static MainApplication instance = new MainApplication();

    public MainApplication() {
        instance = this;
    }

    public static Context getContext() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();

        //Parse.com initialization for notification
        /*
        Parse.initialize(this, "tpBEx1C5sinaYuH53a6WNLEpQm2gLJUnqhXL2IRQ", "tgaoiA1Q2v1R6CEv0FutIVRggLcalLP9XU0sYQ2P");
        ParseInstallation.getCurrentInstallation().saveInBackground();
        */

    }
}