package com.walker.core.util;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.os.Build;
import android.util.TypedValue;
import android.view.Display;

/**
 * @author Walker
 * @e-mail feitianwumu@163.com
 * @date on 2018/9/12
 * @summary dp和px的转换
 */
public class DisplayUtils {

    /**
     * 获取屏幕的宽和高
     *
     * @param activity Activity
     * @return Point
     */
    public static Point getDisplaySize(Activity activity) {
        Point point = new Point();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            Display display = activity.getWindowManager().getDefaultDisplay();
            display.getSize(point);
        } else {
            Display display = activity.getWindowManager().getDefaultDisplay();
            point.x = display.getWidth();
            point.y = display.getHeight();
        }
        return point;
    }

    /**
     * dp转换为px
     *
     * @param context
     * @param dp
     * @returnp
     */
    public static float dp2px(Context context, float dp) {
        float px = getAbsValue(context, dp, TypedValue.COMPLEX_UNIT_DIP);
        return px;
    }

    /**
     * px转换为dp
     *
     * @param context
     * @param px
     * @return
     */
    public static float px2dp(Context context, float px) {
        float dp = (px / context.getResources().getDisplayMetrics().density);
        return dp;
    }

    /**
     * sp转px
     *
     * @param context
     * @param sp
     * @return
     */
    public static float sp2px(Context context, float sp) {
        float px = getAbsValue(context, sp, TypedValue.COMPLEX_UNIT_SP);
        return px;
    }

    private static float getAbsValue(Context context, float value, int unit) {
        return TypedValue.applyDimension(unit, value, context.getResources().getDisplayMetrics());
    }


}
