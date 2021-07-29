package com.walker.optimize.group.caton.blockcanary;

import android.os.Build;
import android.os.Looper;

public class BlockCanary {
    /**
     * 1、我们只需要替换主线程Looper的printer对象，通过计算执行dispatchMessage方法之后和之前打印字符串的时间的差值，
     * 就可以拿到到dispatchMessage方法执行的时间。而大部分的主线程的操作最终都会执行到这个dispatchMessage方法中。
     * <p>
     * 2、然而有些情况的卡顿，这种方案从原理上就无法监控到，比如：1）View的TouchEvent中的卡顿 2）dleHandler的queueIdle()中的卡顿 3）SyncBarrier（同步屏障）的泄漏。
     * 既然该方案监控不到这三种卡顿的情况，通过其他的一些手段，专门监控这些卡顿的case
     */
    public static void install() {
        LogMonitor logMonitor = new LogMonitor();
        Looper.getMainLooper().setMessageLogging(logMonitor);
        //监控IdleHandler卡顿
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            IdleHandlerCanary.get().onCanary();
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            SyncBarrierCanary.get().onCanary();
        }
    }
}
