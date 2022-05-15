package com.example.plugintest;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class GlobalApplication extends Application {
    private static final String TAG="PluginApplication";

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        Log.i(TAG,"执行 attachBaseContext() ");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i(TAG,"执行 onCreate() ");
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
                Log.i(TAG,String.format("%s -> onActivityCreated()",activity.getClass().getSimpleName()));
            }

            @Override
            public void onActivityStarted(@NonNull Activity activity) {
                Log.i(TAG,String.format("%s -> onActivityStarted()",activity.getClass().getSimpleName()));
            }

            @Override
            public void onActivityResumed(@NonNull Activity activity) {
                Log.i(TAG,String.format("%s -> onActivityResumed()",activity.getClass().getSimpleName()));
            }

            @Override
            public void onActivityPaused(@NonNull Activity activity) {
                Log.i(TAG,String.format("%s -> onActivityPaused()",activity.getClass().getSimpleName()));
            }

            @Override
            public void onActivityStopped(@NonNull Activity activity) {
                Log.i(TAG,String.format("%s -> onActivityStopped()",activity.getClass().getSimpleName()));
            }

            @Override
            public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {
                Log.i(TAG,String.format("%s -> onActivitySaveInstanceState()",activity.getClass().getSimpleName()));
            }

            @Override
            public void onActivityDestroyed(@NonNull Activity activity) {
                Log.i(TAG,String.format("%s -> onActivityDestroyed()",activity.getClass().getSimpleName()));
            }
        });
    }
}
