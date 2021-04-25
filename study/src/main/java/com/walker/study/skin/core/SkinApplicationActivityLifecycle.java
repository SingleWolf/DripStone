package com.walker.study.skin.core;

import android.app.Activity;
import android.app.Application;
import android.os.Build;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.LayoutInflater;

import androidx.core.view.LayoutInflaterCompat;

import com.walker.core.log.LogHelper;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Observable;

/**
 * @Author Walker
 * @Date 2021/4/23 11:08 AM
 * @Summary 通过监听ApplicationActivityLifecycle无侵入式地调用换肤管理器
 */
public class SkinApplicationActivityLifecycle implements Application.ActivityLifecycleCallbacks {

    private Observable mObserable;
    private ArrayMap<Activity, SkinLayoutInflaterFactory> mLayoutInflaterFactories = new ArrayMap<>();

    public SkinApplicationActivityLifecycle(Observable observable) {
        mObserable = observable;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        /**
         *  更新状态栏
         */
        SkinThemeUtils.updateStatusBarColor(activity);

        /**
         *  更新布局视图
         */
        hookAndSetFactory(activity);
    }

    @Override
    public void onActivityStarted(Activity activity) {

    }

    @Override
    public void onActivityResumed(Activity activity) {

    }

    @Override
    public void onActivityPaused(Activity activity) {

    }

    @Override
    public void onActivityStopped(Activity activity) {

    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        SkinLayoutInflaterFactory observer = mLayoutInflaterFactories.remove(activity);
        SkinManager.getInstance().deleteObserver(observer);
    }

    private void hookAndSetFactory(Activity activity) {
        if (Build.VERSION.SDK_INT >= 29) {
            setFactoryAfterQ(activity);
        } else {
            setFactoryBeforeQ(activity);
        }
    }

    private void setFactoryAfterQ(Activity activity) {
        //获得Activity的布局加载器
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        Class<LayoutInflaterCompat> compatClass = LayoutInflaterCompat.class;
        Class<LayoutInflater> inflaterClass = LayoutInflater.class;
        try {
            Field sCheckedField = compatClass.getDeclaredField("sCheckedField");
            sCheckedField.setAccessible(true);
            sCheckedField.setBoolean(layoutInflater, false);

            Method forceSetFactory2=compatClass.getDeclaredMethod("forceSetFactory2",LayoutInflater.class,LayoutInflater.Factory2.class);
            SkinLayoutInflaterFactory skinLayoutInflaterFactory = new SkinLayoutInflaterFactory(activity);
            forceSetFactory2.setAccessible(true);
            forceSetFactory2.invoke(null,layoutInflater,skinLayoutInflaterFactory);
            mLayoutInflaterFactories.put(activity, skinLayoutInflaterFactory);
            mObserable.addObserver(skinLayoutInflaterFactory);

            LogHelper.get().i("SkinActivityLifecycle", "[AfterQ] Hook InflaterFactory");

//            Field mFactory = inflaterClass.getDeclaredField("mFactory");
//            mFactory.setAccessible(true);
//            Field mFactory2 = inflaterClass.getDeclaredField("mFactory2");
//            mFactory2.setAccessible(true);
//            SkinLayoutInflaterFactory skinLayoutInflaterFactory = new SkinLayoutInflaterFactory
//                    (activity);
//            if (layoutInflater.getFactory2() != null) {
//                skinLayoutInflaterFactory.setFactory2(layoutInflater.getFactory2());
//            } else if (layoutInflater.getFactory() != null) {
//                skinLayoutInflaterFactory.setInterceptFactory(layoutInflater.getFactory());
//            }
//            mFactory2.set(layoutInflater, skinLayoutInflaterFactory);
//            mFactory.set(layoutInflater, skinLayoutInflaterFactory);
       } catch (Exception e) {
            LogHelper.get().e("SkinActivityLifecycle", e.toString());
        }
    }


    private void setFactoryBeforeQ(Activity activity) {
        try {
            //获得Activity的布局加载器
            LayoutInflater layoutInflater = activity.getLayoutInflater();
            //Android 布局加载器 使用 mFactorySet 标记是否设置过Factory
            //如设置过抛出一次
            //设置 mFactorySet 标签为false
            Field field = LayoutInflater.class.getDeclaredField("mFactorySet");
            field.setAccessible(true);
            field.setBoolean(layoutInflater, false);

            //如果mFactorySet未设置成功，则不进行hook LayoutInflater，以避免不兼容导致的崩溃
            //使用factory2 设置布局加载工程
            SkinLayoutInflaterFactory skinLayoutInflaterFactory = new SkinLayoutInflaterFactory
                    (activity);
            LayoutInflaterCompat.setFactory2(layoutInflater, skinLayoutInflaterFactory);
            mLayoutInflaterFactories.put(activity, skinLayoutInflaterFactory);
            mObserable.addObserver(skinLayoutInflaterFactory);

            field.setBoolean(layoutInflater, false);
            LogHelper.get().i("SkinActivityLifecycle", "[BeforeQ] Hook InflaterFactory");
        } catch (Exception e) {
            LogHelper.get().e("SkinActivityLifecycle", e.toString());
        }
    }
}
