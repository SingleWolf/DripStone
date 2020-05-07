package com.walker.study.hotfix;

import android.content.Context;

import com.walker.study.util.ReflectUtils;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class HotfixHelper {

    public static void installPatch(Context context,File path) {
        //1、获取当前应用的PathClassLoader
        ClassLoader classLoader = context.getClassLoader();
        File filesDir = context.getFilesDir();
        //2、反射获取DexPathList属性对象pathList
        try {
            Field pathListField= ReflectUtils.getField(classLoader,"pathList");
            Object pathList=pathListField.get(pathListField);
            //3、反射修改pathList的dexElements
            //将补丁包path.dex转换为Element[]
            List<File> files=new ArrayList<>();
            files.add(path);
            Method makePathElements=ReflectUtils.getMethod(pathList,"makePathElements",List.class,File.class,List.class);
            List<IOException> suppressedExceptions=new ArrayList<>();
            //反射执行makePathElements方法
            Object[] patchElement=(Object[])makePathElements.invoke(null,files,filesDir,suppressedExceptions);
            Field dexElementField=ReflectUtils.getField(pathList,"dexElements");
            Object[] dexElements=(Object[])dexElementField.get(pathList);
            //新旧合并
            Object[] newElements=(Object[]) Array.newInstance(dexElements.getClass().getComponentType(),patchElement.length+dexElements.length);
            System.arraycopy(patchElement,0,newElements,0,patchElement.length);
            System.arraycopy(dexElements,0,newElements,patchElement.length,dexElements.length);
            //反射赋值给pathList的dexElement
            dexElementField.set(pathList,newElements);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
