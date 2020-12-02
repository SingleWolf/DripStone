package com.example.plugintest;

import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.util.Log;

import java.lang.reflect.Method;

public class LoadUtil {

    private static String mLoadPath = "/sdcard/plugin-debug.apk";

    private static Resources mResources;

    public static void setLoadPath(String loadPath) {
        mLoadPath = loadPath;
    }


    public static Resources getResources(Context context) {
        if (mResources == null) {
            mResources = loadResource(context);
        }
        return mResources;
    }

    private static Resources loadResource(Context context) {
        // assets.addAssetPath(key.mResDir)
        try {
            Log.d("LoadUtil","loadPath="+mLoadPath);
            AssetManager assetManager = AssetManager.class.newInstance();
            // 让 这个 AssetManager对象 加载的 资源为插件的
            Method addAssetPathMethod = AssetManager.class.getMethod("addAssetPath", String.class);
            addAssetPathMethod.invoke(assetManager, mLoadPath);

            // 如果传入的是Activity的 context 会不断循环，导致崩溃
            Resources resources = context.getResources();

            // 加载插件的资源的 resources
            return new Resources(assetManager, resources.getDisplayMetrics(), resources.getConfiguration());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return context.getResources();
    }


}
