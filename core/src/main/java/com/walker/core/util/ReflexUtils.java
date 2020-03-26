package com.walker.core.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @Author Walker
 *
 * @Date   2019/6/10 下午7:29
 *
 * @Summary 反射封装类
 */
public class ReflexUtils {

    /**
     * 获取无参构造函数
     * @param className
     * @return
     */
    public static Object createObject(String className) {
        Class[] pareTypes = new Class[]{};
        Object[] pareValues = new Object[]{};

        try {
            Class r = Class.forName(className);
            return createObject(r, pareTypes, pareValues);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 获取无参构造方法
     * @param clazz
     * @return
     */
    public static Object createObject(Class clazz) {
        Class[] pareType = new Class[]{};
        Object[] pareValues = new Object[]{};

        return createObject(clazz, pareType, pareValues);
    }

    /**
     * 获取一个参数的构造函数  已知className
     *
     * @param className
     * @param pareType
     * @param pareValue
     * @return
     */
    public static Object createObject(String className, Class pareType, Object pareValue) {

        Class[] pareTypes = new Class[]{pareType};
        Object[] pareValues = new Object[]{pareValue};

        try {
            Class r = Class.forName(className);
            return createObject(r, pareTypes, pareValues);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * 获取单个参数的构造方法 已知类
     *
     * @param clazz
     * @param pareType
     * @param pareValue
     * @return
     */
    public static Object createObject(Class clazz, Class pareType, Object pareValue) {
        Class[] pareTypes = new Class[]{pareType};
        Object[] pareValues = new Object[]{pareValue};

        return createObject(clazz, pareTypes, pareValues);
    }

    /**
     * 获取多个参数的构造方法 已知className
     * @param className
     * @param pareTypes
     * @param pareValues
     * @return
     */
    public static Object createObject(String className, Class[] pareTypes, Object[] pareValues) {
        try {
            Class r = Class.forName(className);
            return createObject(r, pareTypes, pareValues);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * 获取构造方法
     *
     * @param clazz
     * @param pareTypes
     * @param pareValues
     * @return
     */
    public static Object createObject(Class clazz, Class[] pareTypes, Object[] pareValues) {
        try {
            Constructor ctor = clazz.getDeclaredConstructor(pareTypes);
            ctor.setAccessible(true);
            return ctor.newInstance(pareValues);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    /**
     * 获取多个参数的方法
     * @param obj
     * @param methodName
     * @param pareTypes
     * @param pareValues
     * @return
     */
    public static Object invokeInstanceMethod(Object obj, String methodName, Class[] pareTypes, Object[] pareValues) {
        if (obj == null) {
            return null;
        }

        try {
            //调用一个private方法 //在指定类中获取指定的方法
            Method method = obj.getClass().getDeclaredMethod(methodName, pareTypes);
            method.setAccessible(true);
            return method.invoke(obj, pareValues);

        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 获取一个参数的方法
     * @param obj
     * @param methodName
     * @param pareType
     * @param pareValue
     * @return
     */
    public static Object invokeInstanceMethod(Object obj, String methodName, Class pareType, Object pareValue) {
        Class[] pareTypes = {pareType};
        Object[] pareValues = {pareValue};

        return invokeInstanceMethod(obj, methodName, pareTypes, pareValues);
    }

    /**
     * 获取无参方法
     * @param obj
     * @param methodName
     * @return
     */
    public static Object invokeInstanceMethod(Object obj, String methodName) {
        Class[] pareTypes = new Class[]{};
        Object[] pareValues = new Object[]{};

        return invokeInstanceMethod(obj, methodName, pareTypes, pareValues);
    }


    /**
     * 无参静态方法
     * @param className
     * @param method_name
     * @return
     */
    public static Object invokeStaticMethod(String className, String method_name) {
        Class[] pareTypes = new Class[]{};
        Object[] pareValues = new Object[]{};

        return invokeStaticMethod(className, method_name, pareTypes, pareValues);
    }

    /**
     * 获取一个参数的静态方法
     * @param className
     * @param method_name
     * @param pareType
     * @param pareValue
     * @return
     */
    public static Object invokeStaticMethod(String className, String method_name, Class pareType, Object pareValue) {
        Class[] pareTypes = new Class[]{pareType};
        Object[] pareValues = new Object[]{pareValue};

        return invokeStaticMethod(className, method_name, pareTypes, pareValues);
    }

    /**
     * 获取多个参数的静态方法
     * @param className
     * @param method_name
     * @param pareTypes
     * @param pareValues
     * @return
     */
    public static Object invokeStaticMethod(String className, String method_name, Class[] pareTypes, Object[] pareValues) {
        try {
            Class obj_class = Class.forName(className);
            return invokeStaticMethod(obj_class, method_name, pareTypes, pareValues);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * 无参静态方法
     * @param method_name
     * @return
     */
    public static Object invokeStaticMethod(Class clazz, String method_name) {
        Class[] pareTypes = new Class[]{};
        Object[] pareValues = new Object[]{};

        return invokeStaticMethod(clazz, method_name, pareTypes, pareValues);
    }

    /**
     * 一个参数静态方法
     * @param clazz
     * @param method_name
     * @param classType
     * @param pareValue
     * @return
     */
    public static Object invokeStaticMethod(Class clazz, String method_name, Class classType, Object pareValue) {
        Class[] classTypes = new Class[]{classType};
        Object[] pareValues = new Object[]{pareValue};

        return invokeStaticMethod(clazz, method_name, classTypes, pareValues);
    }

    /**
     * 多个参数的静态方法
     * @param clazz
     * @param method_name
     * @param pareTypes
     * @param pareValues
     * @return
     */
    public static Object invokeStaticMethod(Class clazz, String method_name, Class[] pareTypes, Object[] pareValues) {
        try {
            Method method = clazz.getDeclaredMethod(method_name, pareTypes);
            method.setAccessible(true);
            return method.invoke(null, pareValues);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    public static Object getFieldObject(String className, Object obj, String filedName) {
        try {
            Class obj_class = Class.forName(className);
            return getFieldObject(obj_class, obj, filedName);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Object getFieldObject(Class clazz, Object obj, String filedName) {
        try {
            Field field = clazz.getDeclaredField(filedName);
            field.setAccessible(true);
            return field.get(obj);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }


    public static void setFieldObject(Class clazz, Object obj, String filedName, Object filedValue) {
        try {
            Field field = clazz.getDeclaredField(filedName);
            field.setAccessible(true);
            field.set(obj, filedValue);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void setFieldObject(String className, Object obj, String filedName, Object filedValue) {
        try {
            Class obj_class = Class.forName(className);
            setFieldObject(obj_class, obj, filedName, filedValue);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }


    public static Object getStaticFieldObject(String className, String filedName) {
        return getFieldObject(className, null, filedName);
    }

    public static Object getStaticFieldObject(Class clazz, String filedName) {
        return getFieldObject(clazz, null, filedName);
    }

    public static void setStaticFieldObject(String classname, String filedName, Object filedValue) {
        setFieldObject(classname, null, filedName, filedValue);
    }

    public static void setStaticFieldObject(Class clazz, String filedName, Object filedValue) {
        setFieldObject(clazz, null, filedName, filedValue);
    }
}