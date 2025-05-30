package com.walker.core.util;

import android.content.Context;
import android.text.TextUtils;
import android.view.Gravity;
import android.widget.Toast;

/**
 * @Author Walker
 * @Date 2020-03-26 11:02
 * @Summary Toast工具类
 */
public class ToastUtils {
    private static Toast sToast;
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
                checkGenToast(time);
                sToast.setText(msg);
                sToast.show();
            });
        }
    }

    private static void showCenter(String msg, int time) {
        if (sContext != null && !TextUtils.isEmpty(msg)) {
            ExecutorUtils.runTaskOnUiThread(() -> {
                checkGenToast(time);
                sToast.setText(msg);
                sToast.setGravity(Gravity.CENTER, 0, 0);
                sToast.show();
            });
        }
    }

    private static void checkGenToast(int time) {
        if (sToast != null) {
            sToast.cancel();
            sToast.setDuration(time);
        } else {
            sToast = Toast.makeText(sContext, "", time);
        }
    }
}
