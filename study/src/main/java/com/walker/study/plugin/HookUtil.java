package com.walker.study.plugin;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.List;

public class HookUtil {

    private static final String TAG = "HookUtil";
    private static final String TARGET_INTENT = "target_intent";
    public static final String KEY_HOOK_TAG = "is_hook_activity";
    /**
     * 代理类的包名（此处指整个应用的包名）
     */
    private static String mProxyAppPackageName = "com.example.walkerdemo";
    /**
     * 代理类的全名
     */
    private static String mProxyActivityClassName = "com.example.walkerdemo.plugin.ProxyActivity";

    /**
     * 代理配置
     *
     * @param appPackageName    应用的包名
     * @param activityClassName 代理类的全名
     */
    public static void setProxyConfig(String appPackageName, String activityClassName) {
        mProxyAppPackageName = appPackageName;
        mProxyActivityClassName = activityClassName;
    }

    public static void hookAMS() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.P) {
            hookIActivityTaskManager();
        } else {
            hookIActivityManager();
        }
    }

    private static void hookIActivityManager() {
        try {
            // 获取 singleton 对象
            Field singletonField = null;
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) { // 小于8.0
                Class<?> clazz = Class.forName("android.app.ActivityManagerNative");
                singletonField = clazz.getDeclaredField("gDefault");
            } else {
                Class<?> clazz = Class.forName("android.app.ActivityManager");
                singletonField = clazz.getDeclaredField("IActivityManagerSingleton");
            }

            singletonField.setAccessible(true);
            Object singleton = singletonField.get(null);

            // 获取 系统的 IActivityManager 对象
            Class<?> singletonClass = Class.forName("android.util.Singleton");
            Field mInstanceField = singletonClass.getDeclaredField("mInstance");
            mInstanceField.setAccessible(true);
            final Object mInstance = mInstanceField.get(singleton);

            // 创建动态代理对象
            Object proxyInstance = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(),
                    new Class[]{Class.forName("android.app.IActivityManager")}, new InvocationHandler() {
                        @Override
                        public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                            // do something
                            // Intent的修改 -- 过滤
                            /**
                             * IActivityManager类的方法
                             * startActivity(whoThread, who.getBasePackageName(), intent,
                             *                         intent.resolveTypeIfNeeded(who.getContentResolver()),
                             *                         token, target != null ? target.mEmbeddedID : null,
                             *                         requestCode, 0, null, options)
                             */
                            // 过滤
                            if ("startActivity".equals(method.getName())) {
                                int index = -1;

                                for (int i = 0; i < args.length; i++) {
                                    if (args[i] instanceof Intent) {
                                        index = i;
                                        break;
                                    }
                                }
                                // 启动插件的
                                Intent intent = (Intent) args[index];
                                boolean isPlugin = intent.getBooleanExtra(KEY_HOOK_TAG, false);
                                if (isPlugin) {
                                    Log.d(TAG, "hookIActivityManager");
                                    Intent proxyIntent = new Intent();
                                    proxyIntent.setClassName(mProxyAppPackageName,
                                            mProxyActivityClassName);
                                    proxyIntent.putExtra(TARGET_INTENT, intent);
                                    proxyIntent.putExtra(KEY_HOOK_TAG, true);
                                    args[index] = proxyIntent;
                                }
                            }

                            // args  method需要的参数  --- 不改变原有的执行流程
                            // mInstance 系统的 IActivityManager 对象
                            return method.invoke(mInstance, args);
                        }
                    });

            // ActivityManager.getService() 替换成 proxyInstance
            mInstanceField.set(singleton, proxyInstance);

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }

    private static void hookIActivityTaskManager() {
        try {
            Field singletonField = null;
            Class<?> activityManager = Class.forName("android.app.ActivityTaskManager");
            singletonField = activityManager.getDeclaredField("IActivityTaskManagerSingleton");
            singletonField.setAccessible(true);
            Object singleton = singletonField.get(null);
            //拿IActivityManager对象
            Class<?> singletonClass = Class.forName("android.util.Singleton");
            Field mInstanceField = singletonClass.getDeclaredField("mInstance");
            mInstanceField.setAccessible(true);
            //原始的IActivityTaskManager
            final Object IActivityTaskManager = mInstanceField.get(singleton);

            Object proxy = Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader()
                    , new Class[]{Class.forName("android.app.IActivityTaskManager")}
                    , new InvocationHandler() {
                        @Override
                        public Object invoke(final Object proxy, final Method method, final Object[] args) throws Throwable {
                            // 过滤
                            if ("startActivity".equals(method.getName())) {
                                int index = -1;

                                for (int i = 0; i < args.length; i++) {
                                    if (args[i] instanceof Intent) {
                                        index = i;
                                        break;
                                    }
                                }
                                // 启动插件的
                                Intent intent = (Intent) args[index];
                                boolean isPlugin = intent.getBooleanExtra(KEY_HOOK_TAG, false);
                                if (isPlugin) {
                                    Log.d(TAG, "hookIActivityTaskManager");
                                    Intent proxyIntent = new Intent();
                                    proxyIntent.setClassName(mProxyAppPackageName,
                                            mProxyActivityClassName);
                                    proxyIntent.putExtra(TARGET_INTENT, intent);
                                    proxyIntent.putExtra(KEY_HOOK_TAG, true);
                                    args[index] = proxyIntent;
                                }
                            }

                            // args  method需要的参数  --- 不改变原有的执行流程
                            // mInstance 系统的 IActivityManager 对象
                            return method.invoke(IActivityTaskManager, args);
                        }
                    });

            //            7. IActivityManagerProxy 融入到framework
            mInstanceField.set(singleton, proxy);

        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }


    public static void hookHandler() {
        try {
            // 获取 ActivityThread 类的 Class 对象
            Class<?> clazz = Class.forName("android.app.ActivityThread");

            // 获取 ActivityThread 对象
            Field activityThreadField = clazz.getDeclaredField("sCurrentActivityThread");
            activityThreadField.setAccessible(true);
            Object activityThread = activityThreadField.get(null);

            // 获取 mH 对象
            Field mHField = clazz.getDeclaredField("mH");
            mHField.setAccessible(true);
            final Handler mH = (Handler) mHField.get(activityThread);

            Field mCallbackField = Handler.class.getDeclaredField("mCallback");
            mCallbackField.setAccessible(true);

            // 创建的 callback
            Handler.Callback callback = new Handler.Callback() {

                @Override
                public boolean handleMessage(@NonNull Message msg) {
                    Log.d(TAG, "hookHandler->msg.what=" + msg.what);

                    // 通过msg  可以拿到 Intent，可以换回执行插件的Intent

                    // 找到 Intent的方便替换的地方  --- 在这个类里面 ActivityClientRecord --- Intent intent 非静态
                    // msg.obj == ActivityClientRecord
                    switch (msg.what) {
                        case 100:
                            try {
                                Field intentField = msg.obj.getClass().getDeclaredField("intent");
                                intentField.setAccessible(true);
                                // 启动代理Intent
                                Intent proxyIntent = (Intent) intentField.get(msg.obj);
                                boolean isPlugin = proxyIntent.getBooleanExtra(KEY_HOOK_TAG, false);
                                if (isPlugin) {
                                    Log.d(TAG, "hookHandler->替换activity");
                                    // 启动插件的 Intent
                                    Intent intent = proxyIntent.getParcelableExtra(TARGET_INTENT);
                                    if (intent != null) {
                                        intentField.set(msg.obj, intent);
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                        case 159:
                            try {
                                // 获取 mActivityCallbacks 对象
                                Field mActivityCallbacksField = msg.obj.getClass()
                                        .getDeclaredField("mActivityCallbacks");

                                mActivityCallbacksField.setAccessible(true);
                                List mActivityCallbacks = (List) mActivityCallbacksField.get(msg.obj);

                                for (int i = 0; i < mActivityCallbacks.size(); i++) {
                                    if (mActivityCallbacks.get(i).getClass().getName()
                                            .equals("android.app.servertransaction.LaunchActivityItem")) {
                                        Object launchActivityItem = mActivityCallbacks.get(i);

                                        // 获取启动代理的 Intent
                                        Field mIntentField = launchActivityItem.getClass()
                                                .getDeclaredField("mIntent");
                                        mIntentField.setAccessible(true);
                                        Intent proxyIntent = (Intent) mIntentField.get(launchActivityItem);

                                        // 目标 intent 替换 proxyIntent
                                        boolean isPlugin = proxyIntent.getBooleanExtra(KEY_HOOK_TAG, false);
                                        if (isPlugin) {
                                            Log.d(TAG, "hookHandler->替换activity");
                                            Intent intent = proxyIntent.getParcelableExtra(TARGET_INTENT);
                                            if (intent != null) {
                                                mIntentField.set(launchActivityItem, intent);
                                            }
                                        }
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            break;
                    }
                    // 必须 return false
                    return false;
                }
            };

            // 替换系统的 callBack
            mCallbackField.set(mH, callback);
        } catch (Exception e) {
            Log.e(TAG, e.getMessage());
        }
    }
}
