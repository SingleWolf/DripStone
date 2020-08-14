package com.walker.study.annotation;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.SparseArray;
import android.view.View;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Arrays;

public class InjectUtils {

    private static SparseArray<String> sIdMap = new SparseArray<>();

    public static void injectIntent(Activity activity) {
        Class<? extends Activity> clz = activity.getClass();
        Field[] declaredFields = clz.getDeclaredFields();
        for (Field field : declaredFields) {
            if (field.isAnnotationPresent(LazyIntent.class)) {
                LazyIntent lazyIntent = field.getAnnotation(LazyIntent.class);
                String key = lazyIntent.value();
                if (TextUtils.isEmpty(key)) {
                    key = field.getName();
                }
                Bundle extra = activity.getIntent().getExtras();
                if (extra.containsKey(key)) {
                    Object paramsValue = extra.get(key);
                    //Parcelable数组不能直接设置
                    //获取数组单个元素类型
                    Class<?> componentType = field.getType().getComponentType();
                    //判断当前元素是数组并且是Parcelable数组
                    if (field.getType().isArray() && Parcelable.class.isAssignableFrom(componentType)) {
                        //创建数组并copy
                        Object[] objects = (Object[]) paramsValue;
                        Object[] copyParclableValue = Arrays.copyOf(objects, objects.length, (Class<? extends Object[]>) field.getType());
                        paramsValue = copyParclableValue;
                    }
                    field.setAccessible(true);
                    try {
                        field.set(activity, paramsValue);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    public static void injectClick(Activity activity) {
        Class<? extends Activity> clz = activity.getClass();
        Method[] methods = clz.getDeclaredMethods();
        for (Method method : methods) {
            //过滤OnClick注解标识的方法
            OnClick onClick = method.getAnnotation(OnClick.class);
            if (onClick != null) {
                int[] viewIds = onClick.value();
                //兼容组件化、模块化导致的元素值必须为常量表达式问题
                if (viewIds.length < 1) {
                    String[] viewIdStr = onClick.idStr();
                    for (String idName : viewIdStr) {
                        int id = findId(activity, idName);
                        //根据id查找view，并实现点击事件的分发
                        transactClick(activity, method, id);
                    }
                } else {
                    //正常的资源id
                    for (int id : viewIds) {
                        //根据id查找view，并实现点击事件的分发
                        transactClick(activity, method, id);
                    }
                }
            }
        }
    }

    private static void transactClick(Activity activity, Method method, int id) {
        if (activity == null || method == null || id == -1) {
            return;
        }
        View view = activity.findViewById(id);
        view.setOnClickListener((v) -> {
            try {
                method.setAccessible(true);
                method.invoke(activity, v);
            } catch (IllegalArgumentException e) {
                try {
                    method.invoke(activity, new Object[]{});
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public static int findId(Activity activity, String id) {
        String idTemp = id;
        int resId = activity.getResources().getIdentifier(idTemp.replace("R.id.", ""), "id", activity.getPackageName());
        sIdMap.put(resId, id);
        return resId;
    }

    public static String findIdName(int id) {
        String idName = sIdMap.get(id, "");
        return idName;
    }

    public static void injectEvent(Activity activity) {
        Class<? extends Activity> activityClass = activity.getClass();
        Method[] declaredMethods = activityClass.getDeclaredMethods();

        for (Method method : declaredMethods) {
            //获得方法上所有注解
            Annotation[] annotations = method.getAnnotations();

            for (Annotation annotation : annotations) {
                //注解类型
                Class<? extends Annotation> annotationType = annotation.annotationType();
                if (annotationType.isAnnotationPresent(EventType.class)) {
                    EventType eventType = annotationType.getAnnotation(EventType.class);
                    // OnClickListener.class
                    Class listenerType = eventType.listenerType();
                    //setOnClickListener
                    String listenerSetter = eventType.listenerSetter();

                    try {
                        // 不需要关心到底是OnClick 还是 OnLongClick
                        Method valueMethod = annotationType.getDeclaredMethod("value");
                        int[] viewIds = (int[]) valueMethod.invoke(annotation);
                        if (viewIds.length < 1) {
                            Method idStrMethod = annotationType.getDeclaredMethod("idStr");
                            String[] idStrs = (String[]) idStrMethod.invoke(annotation);
                            int idIndex = 0;
                            int[] viewIdsTemp=new int[idStrs.length];
                            for (String id : idStrs) {
                                viewIdsTemp[idIndex++] = findId(activity, id);
                            }
                            viewIds=viewIdsTemp;
                        }

                        method.setAccessible(true);
                        ListenerInvocationHandler<Activity> handler = new ListenerInvocationHandler(activity, method);
                        Object listenerProxy = Proxy.newProxyInstance(listenerType.getClassLoader(),
                                new Class[]{listenerType}, handler);
                        // 遍历注解的值
                        for (int viewId : viewIds) {
                            // 获得当前activity的view（赋值）
                            View view = activity.findViewById(viewId);
                            // 获取指定的方法(不需要判断是Click还是LongClick)
                            // 如获得：setOnClickLisnter方法，参数为OnClickListener
                            // 获得 setOnLongClickLisnter，则参数为OnLongClickLisnter
                            Method setter = view.getClass().getMethod(listenerSetter, listenerType);
                            // 执行方法
                            //执行setOnclickListener里面的回调 onclick方法
                            setter.invoke(view, listenerProxy);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }

        }
    }

    /**
     * 还可能在自定义view注入，所以是泛型： T = Activity/View
     *
     * @param <T>
     */
    static class ListenerInvocationHandler<T> implements InvocationHandler {

        private Method method;
        private T target;

        public ListenerInvocationHandler(T target, Method method) {
            this.target = target;
            this.method = method;
        }

        @Override
        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            return this.method.invoke(target, args);
        }
    }
}
