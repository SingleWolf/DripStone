package com.walker.study.plugin;

import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;

import com.walker.study.util.Utils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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

    public static void loadPlugin(Context context) {
        loadPlugin(context, mLoadPath);
    }

    public static synchronized void loadPlugin(Context context, String loadPath) {
        mLoadPath = loadPath;
        if (TextUtils.isEmpty(mLoadPath)) {
            Log.d(TAG, "路径为空");
            return;
        }
        Log.d(TAG, mLoadPath + "路径开始加载...");
        loadDex(context, loadPath);
        loadNative(context, loadPath);
    }

    private static void loadDex(Context context, String loadPath) {
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
            Log.i(TAG, String.format("loadClass的加载路径：%s", mLoadPath));
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
            e.printStackTrace();
            Log.e(TAG, e.toString());
        }
    }

    /**
     * 加载插件的c++库
     *
     * @param context
     * @param zipFilePath loadPath
     */
    private static void loadNative(Context context, String zipFilePath) {
        try {
            ClassLoader appClassLoader = context.getClassLoader();
            String optimizedDirectory = new File(Utils.getCacheDir(context).getAbsolutePath() + File.separator + "plugin-lib").getAbsolutePath();
            String librarySearchPath = null;
            try {
                Utils.unZipFolder(zipFilePath, optimizedDirectory);
                librarySearchPath = new File(optimizedDirectory + File.separator + "lib").getAbsolutePath();
                // 需要删除其余的文件,防止占用磁盘空间。
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (TextUtils.isEmpty(librarySearchPath)) {
                return;
            }
            // 查询到so库中的文件目录
            File abi_file_dir = null;
            File dirFile = new File(librarySearchPath);
            File[] files = dirFile.listFiles();
            for (File file : files) {
                if (file != null && file.exists() && file.isDirectory()) {
                    final String abi = Build.CPU_ABI;
                    // 获取当前应用程序支持cpu(非手机cpu),配到对应的so库。
                    // 注意点： 若是宿主没有32位数Zygote，是无法加载 插件中32位so库。
                    if (file.getName().contains(abi)) {
                        abi_file_dir = file;
                        break;
                    }
                }
            }
            File mLibDir = null;
            try {
                // so库，不可以放在sdcard中。
                String mLibDirPath = context.getCacheDir() + File.separator + "lib" + File.separator + Build.CPU_ABI;
                mLibDir = new File(mLibDirPath);
                if (!mLibDir.exists()) {
                    mLibDir.mkdirs();
                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e(TAG, e.toString());
            }
            List<File> pluginNativeLibraryDirList = new LinkedList<>();
            if (mLibDir != null && abi_file_dir != null) {
                File[] so_file_array = abi_file_dir.listFiles();
                for (File file : so_file_array) {
                    File so_file = new File(mLibDir.getAbsolutePath() + File.separator + file.getName());
                    Utils.copyFiles(file.getAbsolutePath(), so_file.getAbsolutePath());
                    pluginNativeLibraryDirList.add(mLibDir);
                }
            }
            // 获取到DexPathList对象
            Class<?> baseDexClassLoaderClass = DexClassLoader.class.getSuperclass();
            Field pathListField = baseDexClassLoaderClass.getDeclaredField("pathList");
            pathListField.setAccessible(true);
            Object dexPathList = pathListField.get(appClassLoader);
            /**
             * 接下来,合并宿主so,系统so,插件so库
             */
            Class<?> DexPathListClass = dexPathList.getClass();
            if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP_MR1) {
                // 先创建一个汇总so库的文件夹,收集全部
                List<File> allNativeLibDirList = new ArrayList<>();
                // 先添加插件的so库地址
                allNativeLibDirList.addAll(pluginNativeLibraryDirList);
                // 获取到宿主的so库地址
                Field nativeLibraryDirectoriesField = DexPathListClass.getDeclaredField("nativeLibraryDirectories");
                nativeLibraryDirectoriesField.setAccessible(true);
                List<File> old_nativeLibraryDirectories = (List<File>) nativeLibraryDirectoriesField.get(dexPathList);
                allNativeLibDirList.addAll(old_nativeLibraryDirectories);
                // 获取到system的so库地址
                Field systemNativeLibraryDirectoriesField = DexPathListClass.getDeclaredField("systemNativeLibraryDirectories");
                systemNativeLibraryDirectoriesField.setAccessible(true);
                List<File> systemNativeLibraryDirectories = (List<File>) systemNativeLibraryDirectoriesField.get(dexPathList);
                allNativeLibDirList.addAll(systemNativeLibraryDirectories);
                //通过makePathElements获取到c++存放的Element
                Method makePathElementsMethod = DexPathListClass.getDeclaredMethod("makePathElements", List.class);
                makePathElementsMethod.setAccessible(true);
                Object[] allNativeLibraryPathElements = (Object[]) makePathElementsMethod.invoke(null, allNativeLibDirList);
                //将合并宿主和插件的so库，重新设置进去
                Field nativeLibraryPathElementsField = DexPathListClass.getDeclaredField("nativeLibraryPathElements");
                nativeLibraryPathElementsField.setAccessible(true);
                nativeLibraryPathElementsField.set(dexPathList, allNativeLibraryPathElements);
            } else {
                // 获取到宿主的so库地址
                Field nativeLibraryDirectoriesField = DexPathListClass.getDeclaredField("nativeLibraryDirectories");
                nativeLibraryDirectoriesField.setAccessible(true);
                File[] oldNativeDirs = (File[]) nativeLibraryDirectoriesField.get(dexPathList);
                int oldNativeLibraryDirSize = oldNativeDirs.length;
                // 创建一个汇总宿主，插件的so库地址的数组
                File[] totalNativeLibraryDir = new File[oldNativeLibraryDirSize + pluginNativeLibraryDirList.size()];
                System.arraycopy(oldNativeDirs, 0, totalNativeLibraryDir, 0, oldNativeLibraryDirSize);
                for (int i = 0; i < totalNativeLibraryDir.length; ++i) {
                    totalNativeLibraryDir[oldNativeLibraryDirSize + i] = pluginNativeLibraryDirList.get(i);
                }
                // 替换成合并的so库资源数组
                nativeLibraryDirectoriesField.set(dexPathList, totalNativeLibraryDir);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, e.toString());
        }
    }

}
