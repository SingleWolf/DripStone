package com.example.plugintest;

import android.app.Application;
import android.content.Context;
import android.util.Log;

public class GlobalApplication extends Application {
    private static final String TAG=GlobalApplication.class.getSimpleName();
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        Log.i(TAG,TAG+"执行 attachBaseContext() ");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG,TAG+"执行 onCreate() ");
    }
}
