package com.walker.core.util;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Toast;

import java.lang.ref.SoftReference;

/**
 * @Author Walker
 * @Date 2020-03-26 11:02
 * @Summary Toast工具类
 */
public class ToastUtils {
    private static SoftReference<Toast> sToastRef = new SoftReference<>(null);
    private static Context sContext;

    public static void init(Context context) {
        sContext = context;
    }

    public static void show(String msg) {
        show(msg, Toast.LENGTH_SHORT);
    }

    public static void showLong(String msg) {
        show(msg, Toast.LENGTH_LONG);
    }

    public static void showCenter(String msg) {
        showCenter(msg, Toast.LENGTH_SHORT);
    }

    public static void showCenterLong(String msg) {
        showCenter(msg, Toast.LENGTH_LONG);
    }

    private static void show(String msg, int time) {
        if (sContext != null && !TextUtils.isEmpty(msg)) {
            ExecutorUtils.runTaskOnUiThread(() -> {
                Toast toast = sToastRef.get();
                checkGenToast(toast, time);
                toast.setText(msg);
                toast.show();
            });
        }
    }

    private static void showCenter(String msg, int time) {
        if (sContext != null && !TextUtils.isEmpty(msg)) {
            ExecutorUtils.runTaskOnUiThread(() -> {
                Toast toast = sToastRef.get();
                checkGenToast(toast, time);
                toast.setText(msg);
                toast.setGravity(Gravity.CENTER, 0, 0);
                toast.show();
            });
        }
    }

    private static void checkGenToast(Toast toast, int time) {
        if (toast != null) {
            toast.cancel();
            toast.setDuration(time);
        } else {
            toast = Toast.makeText(sContext, "", time);
            sToastRef = new SoftReference<>(toast);
        }
    }
}
