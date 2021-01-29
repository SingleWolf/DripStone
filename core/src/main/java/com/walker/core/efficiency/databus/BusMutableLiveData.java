package com.walker.core.efficiency.databus;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

public class BusMutableLiveData<T> extends MutableLiveData {
    @Override
    public void observe(@NonNull LifecycleOwner owner, @NonNull Observer observer) {
        super.observe(owner, observer);
        hook(observer);
    }

    private void hook(Observer<? super T> observer) {
        try {
            //1.得到mLastVersion
            //获取到LivData的类中的mObservers对象
            Class<LiveData> liveDataClass = LiveData.class;
            Field mObserversField = liveDataClass.getDeclaredField("mObservers");
            mObserversField.setAccessible(true);
            //获取到这个成员变量的对象
            Object mObserversObject = mObserversField.get(this);
            //得到map对象的class对象
            Class<?> mObserversClass = mObserversObject.getClass();
            //获取到mObservers对象的get方法
            Method get = mObserversClass.getDeclaredMethod("get", Object.class);
            get.setAccessible(true);
            //执行get方法
            Object invokeEntry = get.invoke(mObserversObject, observer);
            //取到entry中的value
            Object observerWrapper = null;
            if (invokeEntry != null && invokeEntry instanceof Map.Entry) {
                observerWrapper = ((Map.Entry) invokeEntry).getValue();
            }
            if (observerWrapper == null) {
                throw new NullPointerException("observerWrapper is null");
            }
            //得到observerWrapper的类对象
            Class<?> supperClass = observerWrapper.getClass().getSuperclass();
            Field mLastVersion = supperClass.getDeclaredField("mLastVersion");
            mLastVersion.setAccessible(true);
            //2.得到mVersion
            Field mVersion = liveDataClass.getDeclaredField("mVersion");
            mVersion.setAccessible(true);
            //3.mLastVersion=mVersion
            Object mVersionValue = mVersion.get(this);
            mLastVersion.set(observerWrapper, mVersionValue);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}