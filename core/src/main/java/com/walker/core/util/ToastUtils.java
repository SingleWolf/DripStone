package com.walker.core.util;

import android.content.Context;
import android.text.TextUtils;
import android.widget.Toast;

public class ToastUtils {
    private static Toast mToast;
    private static Context mContext;
    public static void init(Context context) {
        mContext = context;
    }

    public static void show(String msg) {
        try {
            if (mContext != null && !TextUtils.isEmpty(msg)) {
                if(mToast != null){
                    mToast.cancel();
                }
                mToast = Toast.makeText(mContext, "", Toast.LENGTH_LONG);
                mToast.setText(msg);
                mToast.show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
