package com.walker.core.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.KeyguardManager;
import android.content.Context;
import android.os.PowerManager;
import android.util.Log;
import android.view.WindowManager;

/**
 * @author walker zheng
 * @date 2018/12/21
 * @desc 设备工具类
 */
public class DeviceUtils {
    private static final String TAG = "DeviceUtils";

    /**
     * 是否允许屏幕常亮，
     *
     * @param context activity上下文
     * @param isKeep  是否保持常亮
     */
    public static void keepScreenOn(Activity context, boolean isKeep) {
        if (isKeep) {
            //添加标致，从而允许屏幕常亮
            context.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        } else {
            //清除标致，从而允许屏幕熄灭
            context.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

    /**
     * 跳过锁屏界面设置(在setContentView之前调用)
     *
     * @param activity 目标页面
     */
    public void setFlagShowWhenLocked(Activity activity) {
        //FLAG_DISMISS_KEYGUARD用于去掉系统锁屏页，FLAG_SHOW_WHEN_LOCKED使Activity在锁屏时仍然能够显示
        int flags = WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD | WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED;
        activity.getWindow().addFlags(flags);
    }

    /**
     * 唤起并点亮屏幕
     *
     * @param context 上下文
     */
    public void wakeUpAndUnlock(Context context) {
        try {
            //屏锁管理器
            KeyguardManager km = (KeyguardManager) context.getSystemService(Context.KEYGUARD_SERVICE);
            KeyguardManager.KeyguardLock kl = km.newKeyguardLock("unLock");
            //解锁
            kl.disableKeyguard();
            //获取电源管理器对象
            PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
            //获取PowerManager.WakeLock对象,后面的参数|表示同时传入两个值,最后的是LogCat里用的Tag
            @SuppressLint("InvalidWakeLockTag") PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.ACQUIRE_CAUSES_WAKEUP |
                    PowerManager.SCREEN_DIM_WAKE_LOCK, "bright");
            //点亮屏幕
            wl.acquire();
            //释放
            wl.release();
        } catch (Exception e) {
            Log.e(TAG, "wakeUpAndUnlock:" + e.toString());
        }
    }
}
