package com.walker.study.hotfix;

import android.util.Log;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class InstallUtil {

    private static final String TAG = "ShareReflectUtil";

    public static final class V23 {

        public static void install(ClassLoader loader, List<File> additionalClassPathEntries,
                                   File optimizedDirectory)
                throws IllegalArgumentException, IllegalAccessException,
                NoSuchFieldException, InvocationTargetException, NoSuchMethodException,
                IOException {
            //找到 pathList
            Field pathListField = HotfixReflectUtil.findField(loader, "pathList");
            Object dexPathList = pathListField.get(loader);

            ArrayList<IOException> suppressedExceptions = new ArrayList<>();
            // 从 pathList找到 makePathElements 方法并执行
            // 得到补丁创建的 Element[]
            Object[] objects = makePathElements(dexPathList,
                    new ArrayList<>(additionalClassPathEntries), optimizedDirectory,
                    suppressedExceptions);

            //将原本的 dexElements 与 makePathElements生成的数组合并
            HotfixReflectUtil.expandFieldArray(dexPathList, "dexElements", objects);
            if (suppressedExceptions.size() > 0) {
                for (IOException e : suppressedExceptions) {
                    Log.w(TAG, "Exception in makePathElement", e);
                    throw e;
                }

            }
        }

        /**
         * 把dex转化为Element数组
         */
        public static Object[] makePathElements(
                Object dexPathList, ArrayList<File> files, File optimizedDirectory,
                ArrayList<IOException> suppressedExceptions)
                throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
            //通过阅读android6、7、8、9源码，都存在makePathElements方法
            Method makePathElements = HotfixReflectUtil.findMethod(dexPathList, "makePathElements",
                    List.class, File.class,
                    List.class);
            return (Object[]) makePathElements.invoke(dexPathList, files, optimizedDirectory,
                    suppressedExceptions);
        }
    }

    public static final class V19 {

        public static void install(ClassLoader loader, List<File> additionalClassPathEntries,
                                   File optimizedDirectory)
                throws IllegalArgumentException, IllegalAccessException,
                NoSuchFieldException, InvocationTargetException, NoSuchMethodException,
                IOException {
            Field pathListField = HotfixReflectUtil.findField(loader, "pathList");
            Object dexPathList = pathListField.get(loader);
            ArrayList<IOException> suppressedExceptions = new ArrayList<IOException>();
            HotfixReflectUtil.expandFieldArray(dexPathList, "dexElements",
                    makeDexElements(dexPathList,
                            new ArrayList<File>(additionalClassPathEntries), optimizedDirectory,
                            suppressedExceptions));
            if (suppressedExceptions.size() > 0) {
                for (IOException e : suppressedExceptions) {
                    Log.w(TAG, "Exception in makeDexElement", e);
                    throw e;
                }
            }
        }

        public static Object[] makeDexElements(
                Object dexPathList, ArrayList<File> files, File optimizedDirectory,
                ArrayList<IOException> suppressedExceptions)
                throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
            Method makeDexElements = HotfixReflectUtil.findMethod(dexPathList, "makeDexElements",
                    ArrayList.class, File.class,
                    ArrayList.class);


            return (Object[]) makeDexElements.invoke(dexPathList, files, optimizedDirectory,
                    suppressedExceptions);
        }
    }

    /**
     * 14, 15, 16, 17, 18.
     */
    public static final class V14 {


        public static void install(ClassLoader loader, List<File> additionalClassPathEntries,
                                   File optimizedDirectory)
                throws IllegalArgumentException, IllegalAccessException,
                NoSuchFieldException, InvocationTargetException, NoSuchMethodException {

            Field pathListField = HotfixReflectUtil.findField(loader, "pathList");
            Object dexPathList = pathListField.get(loader);

            HotfixReflectUtil.expandFieldArray(dexPathList, "dexElements",
                    makeDexElements(dexPathList,
                            new ArrayList<File>(additionalClassPathEntries), optimizedDirectory));
        }

        public static Object[] makeDexElements(
                Object dexPathList, ArrayList<File> files, File optimizedDirectory)
                throws IllegalAccessException, InvocationTargetException,
                NoSuchMethodException {
            Method makeDexElements =
                    HotfixReflectUtil.findMethod(dexPathList, "makeDexElements", ArrayList.class,
                            File.class);
            return (Object[]) makeDexElements.invoke(dexPathList, files, optimizedDirectory);
        }
    }

}
