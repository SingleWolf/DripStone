package com.walker.core.store.storage;

import android.content.Context;
import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.os.storage.StorageManager;
import android.text.TextUtils;

import com.walker.core.util.StringBuilderUtils;

import java.io.File;
import java.lang.reflect.Method;

/**
 * @author Walker
 * @date on 2018/4/17 0017 上午 10:33
 * @email feitianwumu@163.com
 * @desc sd存储辅助类（包括手机内存(Internal Storage),内置存储卡(Primary Storage),外置存储卡(Secondary Storage)）
 */
public class StorageHelper {
    /**
     * 手机内存类型
     */
    public static final int TYPE_INTERNAL = 1;
    /**
     * 手机主存储类型
     */
    public static final int TYPE_PRIMARY = 2;
    /**
     * 手机外置存储类型
     */
    public static final int TYPE_SECONDARY = 3;
    /**
     * 默认类型
     */
    private static final int TYPE_DEFAULT = 2;
    /**
     * context
     */
    private static Context sContext;
    /**
     * 根目录路径
     */
    private static String sFileRootPath;

    /**
     * 初始化，需再application中设置
     *
     * @param context application级别的context
     */
    public static void init(Context context, String fileRoot) {
        if (sContext == null) {
            sContext = context;
        }
        if (TextUtils.isEmpty(sFileRootPath)) {
            sFileRootPath = fileRoot;
        }
    }

    /**
     * 获取程序存取数据的默认根目录路径
     *
     * @return String
     */
    public static String getRootPath() {
        return getStorageRootPath(TYPE_DEFAULT, sFileRootPath);
    }

    /**
     * 获取程序存取数据的根目录路径
     * StorageHelper.TYPE_INTERNAL 手机内存
     * StorageHelper.TYPE_PRIMARY  主存储
     * StorageHelper.TYPE_SECONDARY 副存储（外置的SD卡）
     *
     * @param typeValue 存储级别类型
     * @return String
     */
    public static String getRootPath(int typeValue) {
        return getStorageRootPath(typeValue, sFileRootPath);
    }

    /**
     * 获取存储的根目录路径
     *
     * @param typeValue 存储类型（手机内存、主存储、副存储）
     * @param fileRoot  文件根目录
     * @return String
     */
    private static String getStorageRootPath(int typeValue, String fileRoot) {
        String filePath = null;
        if (sContext == null) {
            throw new RuntimeException("context not is null !");
        }
        if (TextUtils.isEmpty(fileRoot)) {
            return null;
        }
        try {
            switch (typeValue) {
                case TYPE_SECONDARY:
                    if (!TextUtils.isEmpty(Secondary.path())) {
                        filePath = StringBuilderUtils.pliceStr(Secondary.path(), File.separator, fileRoot);
                        break;
                    }
                case TYPE_PRIMARY:
                    if (Primary.ready()) {
                        filePath = sContext.getExternalFilesDir(fileRoot).getPath();
                        break;
                    }
                case TYPE_INTERNAL:
                    filePath = StringBuilderUtils.pliceStr(sContext.getFilesDir().getPath(), File.separator, fileRoot);
                    break;
                default:
                    filePath = StringBuilderUtils.pliceStr(sContext.getFilesDir().getPath(), File.separator, fileRoot);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
            filePath = StringBuilderUtils.pliceStr(Environment.getExternalStorageDirectory().getPath(), File.separator, fileRoot);
        } finally {
            File file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }
        }
        return filePath;
    }

    /**
     * 手机内存
     */
    public static class Internal {

        /**
         * 路径
         *
         * @return Stirng
         */
        public static String path() {
            return Environment.getDataDirectory().getPath();
        }

        /**
         * 总共可用
         *
         * @return long
         */
        public static long total() {
            File path = Environment.getDataDirectory();
            return getTotal(path);
        }

        /**
         * 已用大小
         *
         * @return long
         */
        public static long used() {
            File path = Environment.getDataDirectory();
            return getUsed(path);
        }

        /**
         * 剩余容量
         *
         * @return long
         */
        public static long free() {
            File path = Environment.getDataDirectory();
            return getFree(path);
        }

    }

    /**
     * 主存储
     * 1. 如果没有内部存储,则为外部存储
     * 2. 一般默认说的sdcard就是指这一层
     */
    public static class Primary {

        /**
         * 存储是否可用
         *
         * @return boolean
         */
        public static boolean ready() {
            String state = Environment.getExternalStorageState();
            return Environment.MEDIA_MOUNTED.equals(state);
        }

        /**
         * 路径
         *
         * @return String
         */
        public static String path() {
            return Environment.getExternalStorageDirectory().getPath();
        }

        /**
         * 总共大小
         *
         * @return long
         */
        public static long total() {
            File path = Environment.getExternalStorageDirectory();
            return getTotal(path);
        }

        /**
         * 已用大小
         *
         * @return long
         */
        public static long used() {
            File path = Environment.getExternalStorageDirectory();
            return getUsed(path);
        }

        /**
         * 剩余容量
         *
         * @return long
         */
        public static long free() {
            File path = Environment.getExternalStorageDirectory();
            return getFree(path);
        }

    }

    /**
     * 外部存储(外置SD卡)
     */
    public static class Secondary {
        /**
         * 路径
         *
         * @return String
         */
        public static String path() {
            String mPath = null;
            //android 4.4系统以下
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.KITKAT) {
                try {
                    StorageManager mStorageManager = (StorageManager) sContext.getSystemService(Context.STORAGE_SERVICE);
                    Method mMethodGetPaths = mStorageManager.getClass().getMethod("getVolumeList");
                    Object[] list = (Object[]) mMethodGetPaths.invoke(mStorageManager);
                    if (1 < list.length) {
                        Object reflectItem = list[1];
                        Method fmPath = reflectItem.getClass().getDeclaredMethod("getPath");
                        fmPath.setAccessible(true);
                        mPath = (String) fmPath.invoke(reflectItem);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else { //android 4.4系统及以上
                try {
                    File[] files = sContext.getExternalFilesDirs(null);
                    if (1 < files.length) {
                        mPath = files[1].getPath();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            return mPath;
        }
    }

    /**
     * 文件总共大小
     *
     * @param path 路径
     * @return long
     */
    private static long getTotal(File path) {
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long totalBlocks = stat.getBlockCount();
        return blockSize * totalBlocks;
    }

    /**
     * 文件剩余大小
     *
     * @param path 路径
     * @return long
     */
    private static long getFree(File path) {
        StatFs stat = new StatFs(path.getPath());
        long blockSize = stat.getBlockSize();
        long freeBlocks = stat.getAvailableBlocks();
        return blockSize * freeBlocks;
    }

    /**
     * 文件已用
     *
     * @param path 路径
     * @return long
     */
    private static long getUsed(File path) {
        return getTotal(path) - getFree(path);
    }
}
