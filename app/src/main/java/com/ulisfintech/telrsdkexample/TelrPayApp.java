package com.ulisfintech.telrsdkexample;

import android.app.Application;
import android.content.Context;

public class TelrPayApp extends Application {

    public static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        context = getApplicationContext();
    }
}
