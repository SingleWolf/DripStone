package com.walker.optimize.group.oom.leakcanary;

import android.util.Log;

public class Utils {
    private static final String TAG = "leakcanary-watcher";

    public static void sleep(long millis) {
        Log.d(TAG, "sleep: " + millis);
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public static void gc() {
        Log.d(TAG, "执行gc...");
        //主要这里不是使用System.gc,因为它仅仅是通知系统在合适的时间进行一次垃圾回收操作
        //实际上并不保证一定执行
        Runtime.getRuntime().gc();
        sleep(100);
        System.runFinalization();
    }


}
