package com.walker.optimize.group.epic;

import android.graphics.Bitmap;
import android.util.Log;
import android.widget.ImageView;

import dalvik.system.DexFile;
import de.robv.android.xposed.DexposedBridge;
import de.robv.android.xposed.XC_MethodHook;

/**
 * @Author Walker
 * @Date 2021/4/7 3:30 PM
 * @Summary 基于epic实现hook
 */
public class EpicHookHelper {
    public static String TAG = "EpicHookHelper";
    private volatile static EpicHookHelper sInstance;

    private EpicHookHelper() {
    }

    public static EpicHookHelper get() {
        if (sInstance == null) {
            synchronized (EpicHookHelper.class) {
                if (sInstance == null) {
                    sInstance = new EpicHookHelper();
                }
            }
        }
        return sInstance;
    }

    public void hookThreads() {
        DexposedBridge.hookAllConstructors(Thread.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                Thread thread = (Thread) param.thisObject;
                Class<?> clazz = thread.getClass();
                if (clazz != Thread.class) {
                    Log.d(TAG, "[ThreadMethodHook] Found class extend Thread:" + clazz);
                    DexposedBridge.findAndHookMethod(clazz, "run", new ThreadMethodHook());
                }
                Log.d(TAG, "[ThreadMethodHook] Thread:" + thread.getName() + " class:" + thread.getClass() + " is created..");
            }
        });
        DexposedBridge.findAndHookMethod(Thread.class, "run", new ThreadMethodHook());
    }

    public void hookDexLoad() {
        DexposedBridge.findAndHookMethod(DexFile.class, "loadDex", String.class, String.class, int.class, new XC_MethodHook() {
            @Override
            protected void beforeHookedMethod(MethodHookParam param) throws Throwable {
                super.beforeHookedMethod(param);
                String dex = (String) param.args[0];
                String odex = (String) param.args[1];
                Log.i(TAG, "[DexLoadHook] load dex, input:" + dex + ", output:" + odex);
            }
        });
    }

    public void hookImageView() {
        DexposedBridge.hookAllConstructors(ImageView.class, new XC_MethodHook() {
            @Override
            protected void afterHookedMethod(MethodHookParam param) throws Throwable {
                super.afterHookedMethod(param);
                DexposedBridge.findAndHookMethod(ImageView.class, "setImageBitmap", Bitmap.class, new ImageViewHook());
            }
        });
    }
}
