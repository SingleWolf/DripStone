package com.walker.crash;

import android.content.Context;

import java.io.File;

public class CrashReport {

    static {
        System.loadLibrary("bugly");
    }

    public static void init(Context context) {
        Context applicationContext = context.getApplicationContext();
        File file = new File(applicationContext.getExternalCacheDir(), "native_crash");
        if (!file.exists()) {
            file.mkdirs();
        }
        initNativeCrash(file.getAbsolutePath());
    }


    private static native void initNativeCrash(String path);

    public static native void testNativeCrash();

    public static void testJavaCrash() {
        int i = 1 / 0;
    }

    public static void testJavaCrash_1() {
        String test = null;
        int crash = test.hashCode();
    }

    public static void testJavaCrash_2() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String test = null;
                int crash = test.hashCode();
            }
        });
    }
}
