package com.walker.study.plugin;

import android.content.Context;
import android.util.Log;

import java.lang.reflect.Array;
import java.lang.reflect.Field;

import dalvik.system.DexClassLoader;

public class LoadUtil {
    private static final String TAG = "LoadUtil";
    /**
     * 加载路径
     */
    private static String mLoadPath = "/sdcard/pluginTest-debug.apk";

    public static void setLoadPath(String loadPath) {
        mLoadPath = loadPath;
    }

    public static void loadClass(Context context) {
        loadClass(context, mLoadPath);
    }

    public static void loadClass(Context context, String loadPath) {
        mLoadPath = loadPath;
        /**
         * 宿主dexElements = 宿主dexElements + 插件dexElements
         *
         * 1.获取宿主dexElements
         * 2.获取插件dexElements
         * 3.合并两个dexElements
         * 4.将新的dexElements 赋值到 宿主dexElements
         *
         * 目标：dexElements  -- DexPathList类的对象 -- BaseDexClassLoader的对象，类加载器
         *
         * 获取的是宿主的类加载器  --- 反射 dexElements  宿主
         *
         * 获取的是插件的类加载器  --- 反射 dexElements  插件
         */
        try {
            Class<?> clazz = Class.forName("dalvik.system.BaseDexClassLoader");
            Field pathListField = clazz.getDeclaredField("pathList");
            pathListField.setAccessible(true);

            Class<?> dexPathListClass = Class.forName("dalvik.system.DexPathList");
            Field dexElementsField = dexPathListClass.getDeclaredField("dexElements");
            dexElementsField.setAccessible(true);

            // 宿主的 类加载器
            ClassLoader pathClassLoader = context.getClassLoader();
            // DexPathList类的对象
            Object hostPathList = pathListField.get(pathClassLoader);
            // 宿主的 dexElements
            Object[] hostDexElements = (Object[]) dexElementsField.get(hostPathList);

            // 插件的 类加载器
            Log.e(TAG, String.format("loadClass的加载路径：%s", mLoadPath));
            ClassLoader dexClassLoader = new DexClassLoader(mLoadPath, context.getCacheDir().getAbsolutePath(),
                    null, pathClassLoader);
            // DexPathList类的对象
            Object pluginPathList = pathListField.get(dexClassLoader);
            // 插件的 dexElements
            Object[] pluginDexElements = (Object[]) dexElementsField.get(pluginPathList);

            // 宿主dexElements = 宿主dexElements + 插件dexElements
            //Object[] obj = new Object[]; // 不行

            // 创建一个新数组
            Object[] newDexElements = (Object[]) Array.newInstance(hostDexElements.getClass().getComponentType(),
                    hostDexElements.length + pluginDexElements.length);
            System.arraycopy(hostDexElements, 0, newDexElements,
                    0, hostDexElements.length);
            System.arraycopy(pluginDexElements, 0, newDexElements,
                    hostDexElements.length, pluginDexElements.length);

            // 赋值
            // hostDexElements = newDexElements
            dexElementsField.set(hostPathList, newDexElements);

        } catch (Exception e) {
            Log.e(TAG, e.toString());
        }
    }

}
