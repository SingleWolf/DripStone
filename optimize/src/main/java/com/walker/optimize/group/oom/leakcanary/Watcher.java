package com.walker.optimize.group.oom.leakcanary;

import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.walker.core.log.LogHelper;
import com.walker.core.util.ToastUtils;

import java.lang.ref.Reference;
import java.lang.ref.ReferenceQueue;
import java.util.HashMap;
import java.util.UUID;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class Watcher {
    private static final String TAG = "leakcanary-watcher";
    //观察列表
    private HashMap<String, KeyWeakReference> watchedReferences = new HashMap<>();
    //怀疑列表
    private HashMap<String, KeyWeakReference> retainedReferences = new HashMap<>();
    //引用队列，相当于一个监视器设备，所有需要监视的对象，盛放监视对象的容器 都与之关联
    //当被监视的对象被gc回收后，对应的容器就会被加入到queue
    private ReferenceQueue queue = new ReferenceQueue();

    public Watcher() {
    }

    /**
     * 清理观察列表和怀疑列表的引用容器
     */
    private void removeWeaklyReachableReferences() {
        Log.d(TAG, "清理列表...");
        KeyWeakReference findRef = null;
        do {
            findRef = (KeyWeakReference) queue.poll();
            //不为空说明对应的对象被gc回收了，那么可以把对应的容器从观察列表，怀疑列表移除
            Log.d(TAG, "findRef = " + findRef);
            if (findRef != null) {
                Log.d(TAG, "打印对应的对象的key: " + findRef.getKey());
                //根据key把观察列表中对应的容器移除
                Reference removedRef = watchedReferences.remove(findRef.getKey());
                //如果removedRef为空，那么有可能被放入到怀疑列表了
                //那么尝试从怀疑列表中移除
                if (removedRef == null) {
                    retainedReferences.remove(findRef.getKey());
                }
            }
        } while (findRef != null);//把所有放到referenceQueue的引用容器找出来
    }

    /**
     * 根据key把对应的容器加入到怀疑列表
     *
     * @param key
     */
    private synchronized void moveToRetained(String key) {
        Log.d(TAG, "加入到怀疑列表...");
        //在加入怀疑列表之前，做一次清理工作
        removeWeaklyReachableReferences();
        //根据key从观察列表中去找盛放对象的容器，如果被找到，说明到目前为止key对应的对象还没被释放
        KeyWeakReference retainedRef = watchedReferences.remove(key);
        if (retainedRef != null) {
            //把从观察列表中移除出来的对象加入到怀疑列表
            retainedReferences.put(key, retainedRef);
        }
    }


    public void watch(Object watchedReference, String referenceName) {
        Log.d(TAG, "开始watch对象...");
        //1. 在没有被监视之前，先清理下观察列表和怀疑列表
        removeWeaklyReachableReferences();

        //2. 为要监视的对象生成一个唯一的uuid
        //相当于把要监视的对象 和容器 与 引用队列建立联系
        final String key = UUID.randomUUID().toString();
        Log.d(TAG, "待监视对象的key: " + key);
        //3. 让watchedReference 与一个KeyWeakReference建立一对一映射关系，并与引用队列queue关联
        KeyWeakReference reference = new KeyWeakReference(watchedReference, queue, key, "");

        //4. 加入到观察列表
        watchedReferences.put(key, reference);

        //5.过5秒后去看是否还在观察列表，如果还在，则加入到怀疑列表
        Executor executor = Executors.newSingleThreadExecutor();
        executor.execute(() -> {
            Utils.sleep(5000);
            moveToRetained(key);
            if (0 < getRetainedRefCount()) {
                Utils.gc();
                removeWeaklyReachableReferences();
                if (0 < getRetainedRefCount()) {
                    KeyWeakReference keyWeakReference = retainedReferences.get(key);
                    if (keyWeakReference != null) {
                        String result = "发现内存泄漏: key: " + key + " , obj: " + keyWeakReference.get() + " , keyWeakReference: " + keyWeakReference;
                        LogHelper.get().d(TAG, result);
                        ToastUtils.showCenterLong(result);
                    }
                }
            }
        });

    }

    public HashMap<String, KeyWeakReference> getRetainedReferences() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            retainedReferences.forEach((key, keyWeakReference) -> {
                        Log.d(TAG, "key: " + key + " , obj: " + keyWeakReference.get() + " , keyWeakReference: " + keyWeakReference);
                    }
            );
        }
        return retainedReferences;
    }

    public int getRetainedRefCount() {
        return retainedReferences.size();
    }
}
