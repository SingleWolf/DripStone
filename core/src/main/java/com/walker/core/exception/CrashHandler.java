package com.walker.core.exception;

import android.os.Process;

/**
 * @author Walker
 * @date on 2018/3/30 0030 下午 13:28
 * @email feitianwumu@163.com
 * @desc 获取应用crash信息
 */
public class CrashHandler implements Thread.UncaughtExceptionHandler {
    private static final String TAG = "CrashHandler";
    private static CrashHandler sInstance = new CrashHandler();
    private Thread.UncaughtExceptionHandler mDefaultCrashHandler;
    private OnCrashListener mCrashListener;

    private CrashHandler() {
    }

    public static CrashHandler getInstance() {
        return sInstance;
    }

    public void init(OnCrashListener listener) {
        mDefaultCrashHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
        mCrashListener = listener;
    }

    @Override
    public void uncaughtException(Thread t, Throwable e) {
        if (mCrashListener != null) {
            mCrashListener.onTransact(e);
        }
        e.printStackTrace();
        //如果系统提供了默认的异常处理器，则交给系统去结束程序，否则就由自己结束自己
        if (mDefaultCrashHandler != null) {
            mDefaultCrashHandler.uncaughtException(t, e);
        } else {
            Process.killProcess(Process.myPid());
        }
    }
}
