package com.despectra.android.classjournal_new.App;

import android.app.Application;

/**
 * Created by Андрей on 23.02.14.
 */
public class JournalApplication extends Application {

    public static String PACKAGE;

    @Override
    public void onCreate() {
        super.onCreate();
        PACKAGE = getApplicationContext().getPackageName();
    }
}
