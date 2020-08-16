package com.walker.core.util;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.palette.graphics.Palette;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Walker
 * @e-mail feitianwumu@163.com
 * @date on 2018/9/13
 * @summary 沉浸式状态栏工具类
 */
public class SteepStatusBarUtils {
    /**
     * 利用反射获取状态栏高度
     *
     * @param context 上下文
     * @return 状态栏高（px）
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        //获取状态栏高度的资源id
        try {
            int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
            if (resourceId > 0) {
                result = context.getResources().getDimensionPixelSize(resourceId);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    /**
     * 设置状态栏透明
     *
     * @param context Activity级别上下文
     * @param dark    状态栏文字颜色深浅标识
     */
    public static void setTranslucentTheme(Activity context) {
        setTranslucentTheme(context, false);
    }

    /**
     * 设置状态栏透明
     *
     * @param context Activity级别上下文
     * @param dark    状态栏文字颜色深浅标识
     */
    public static void setTranslucentTheme(Activity context, boolean dark) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                Window window = context.getWindow();
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    setAndroidNativeLightStatusBar(context, dark);
                    window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                    window.setStatusBarColor(Color.TRANSPARENT);
                    //导航栏颜色也可以正常设置
                    //window.setNavigationBarColor(Color.TRANSPARENT);
                } else {
                    WindowManager.LayoutParams attributes = window.getAttributes();
                    int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
                    int flagTranslucentNavigation = WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION;
                    attributes.flags |= flagTranslucentStatus;
                    //attributes.flags |= flagTranslucentNavigation;
                    window.setAttributes(attributes);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 添加状态栏占位视图
     *
     * @param context Activity级别上下文
     * @param color   颜色值
     */
    public static void addStatusViewWithColor(Activity context, int color) {
        try {
            int statusBarHeight = getStatusBarHeight(context);
            //设置 paddingTop
            ViewGroup rootView = (ViewGroup) context.getWindow().getDecorView().findViewById(android.R.id.content);
            rootView.setPadding(0, statusBarHeight, 0, 0);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                //5.0 以上直接设置状态栏颜色
                context.getWindow().setStatusBarColor(color);
            } else {
                //根布局添加占位状态栏
                ViewGroup decorView = (ViewGroup) context.getWindow().getDecorView();
                View statusBarView = new View(context);
                ViewGroup.LayoutParams lp = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, statusBarHeight);
                statusBarView.setBackgroundColor(color);
                decorView.addView(statusBarView, lp);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 改变状态栏文字颜色
     *
     * @param activity Activity级别上下文
     * @param dark     深色标识
     */
    private static void setAndroidNativeLightStatusBar(Activity activity, boolean dark) {
        try {
            View decor = activity.getWindow().getDecorView();
            if (dark) {
                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
            } else {
                decor.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据图片获取状态栏颜色
     *
     * @param bitmap               源图片
     * @param extractColorListener 回调接口
     */
    public static void getStatusColorWithBitmap(Bitmap bitmap, final ExtractColorListener extractColorListener) {
        final int statuaBarAlaphaValue = 100;
        Palette.Builder builder = Palette.from(bitmap);
        builder.generate(new Palette.PaletteAsyncListener() {
            @TargetApi(Build.VERSION_CODES.LOLLIPOP)
            @Override
            public void onGenerated(Palette palette) {
                Palette.Swatch vibrant = palette.getVibrantSwatch();
                Palette.Swatch darkVibrant = palette.getDarkVibrantSwatch();
                Palette.Swatch lightVibrant = palette.getLightVibrantSwatch();
                Palette.Swatch muted = palette.getMutedSwatch();
                Palette.Swatch darkMuted = palette.getDarkMutedSwatch();
                Palette.Swatch lightMuted = palette.getLightMutedSwatch();

                Map<Palette.Swatch, Integer> valueMap = new HashMap<>(16);
                int vibrantCount = getColorCountBySwatch(vibrant);
                int darkVibrantCount = getColorCountBySwatch(darkVibrant);
                int lightVibrantCount = getColorCountBySwatch(lightVibrant);
                int mutedCount = getColorCountBySwatch(muted);
                int darkMutedCount = getColorCountBySwatch(darkMuted);
                int lightMutedCount = getColorCountBySwatch(lightMuted);

                valueMap.put(vibrant, vibrantCount);
                valueMap.put(darkVibrant, darkVibrantCount);
                valueMap.put(lightVibrant, lightVibrantCount);
                valueMap.put(muted, mutedCount);
                valueMap.put(darkMuted, darkMutedCount);
                valueMap.put(lightMuted, lightMutedCount);

                int maxValue = 0;
                boolean isLight = false;
                Palette.Swatch maxSwatch = null;
                for (Map.Entry<Palette.Swatch, Integer> entry : valueMap.entrySet()) {
                    if (maxValue < entry.getValue()) {
                        maxValue = entry.getValue();
                        maxSwatch = entry.getKey();
                        isLight = entry.getKey() == lightVibrant || entry.getKey() == lightMuted;
                    }
                }
                int statusBarColor;
                if (maxSwatch == null) {
                    statusBarColor = Color.argb(statuaBarAlaphaValue, 0, 0, 0);
                    isLight = false;
                } else {
                    int color = maxSwatch.getRgb();
                    int red = (color & 0xff0000) >> 16;
                    int green = (color & 0x00ff00) >> 8;
                    int blue = (color & 0x0000ff);
                    statusBarColor = Color.argb(statuaBarAlaphaValue, red, green, blue);
                }
                extractColorListener.setColor(statusBarColor, isLight);
            }
        });
    }

    private static int getColorCountBySwatch(Palette.Swatch swatch) {
        return swatch == null ? 0 : swatch.getPopulation();
    }

    public interface ExtractColorListener {
        void setColor(int color, boolean isLight);
    }
}
