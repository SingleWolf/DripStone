package com.walker.crash;

import android.app.Application;
import android.util.Log;

public class GlobalApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d("optimize-crash","GlobalApplication->onCreate()");
        CrashReport.init(this);
    }
}
