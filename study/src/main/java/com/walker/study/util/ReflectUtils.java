package com.walker.study.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class ReflectUtils {

    public static Field getField(Object instance,String name) throws Exception {
        Class<?> clz=instance.getClass();
        while(clz!=Object.class){
            try{
                Field field=clz.getDeclaredField(name);
                if(field!=null){
                    field.setAccessible(true);
                    return field;
                }
            }catch (NoSuchFieldException e){
                clz=clz.getSuperclass();
            }
        }
        throw new Exception(instance.getClass().getSimpleName()+" not found filed: "+name);
    }

    public static Method getMethod(Object instance, String name,Class<?>... parameterTypes) throws Exception {
        Class<?> clz=instance.getClass();
        while(clz!=Object.class){
            try{
                Method method=clz.getDeclaredMethod(name,parameterTypes);
                if(method!=null){
                    method.setAccessible(true);
                    return method;
                }
            }catch (NoSuchMethodException e){
                clz=clz.getSuperclass();
            }
        }
        throw new Exception(instance.getClass().getSimpleName()+" not found method: "+name);
    }
}
