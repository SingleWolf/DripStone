package com.walker.optimize.group.network.netspeed;

import android.util.Log;

import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @Author Walker
 * @Date 2020/10/26 1:32 PM
 * @Summary 网络测试辅助类
 */
public class NetSpeedHelper implements INetSpeedMonitor, OnNetSpeedListener {
    private static final String TAG="NetSpeedHelper";
    private static AtomicReference<NetSpeedHelper> mAtomicReference = new AtomicReference<>(null);
    private CopyOnWriteArrayList<OnNetSpeedListener> mListenerList = new CopyOnWriteArrayList<>();
    private INetSpeedMonitor mNetSpeedMonitor;

    public static NetSpeedHelper get() {
        for (; ; ) {
            if (mAtomicReference.get() != null) {
                return mAtomicReference.get();
            }
            Log.d(TAG,"NetSpeedHelper generate instance");
            mAtomicReference.compareAndSet(null, new NetSpeedHelper());
        }
    }

    private NetSpeedHelper() {
        mNetSpeedMonitor = new NetSpeedMonitorImpl(this);
    }

    @Override
    public void start() {
        if (mNetSpeedMonitor != null) {
            mNetSpeedMonitor.start();
        }
    }

    @Override
    public void stop() {
        if (mNetSpeedMonitor != null) {
            mNetSpeedMonitor.stop();
        }
    }

    @Override
    public void register() {
        if (mNetSpeedMonitor != null) {
            Log.d(TAG,"register()");
            mNetSpeedMonitor.register();
        }
    }

    @Override
    public void unregister() {
        if (mNetSpeedMonitor != null) {
            Log.d(TAG,"unregister()");
            mNetSpeedMonitor.unregister();
        }
    }

    @Override
    public void onNetChanged(NetSpeed netSpeed) {
        for (OnNetSpeedListener listener : mListenerList) {
            Log.d(TAG,"当前网速状态="+netSpeed.toString());
            listener.onNetChanged(netSpeed);
        }
    }

    public void setOnNetSpeedListener(OnNetSpeedListener listener) {
        if (listener != null) {
            Log.d(TAG,"setOnNetSpeedListener()");
            mListenerList.add(listener);
        }
    }

    public void removeNetSpeedListener(OnNetSpeedListener listener) {
        if (listener != null) {
            Log.d(TAG,"removeNetSpeedListener()");
            mListenerList.remove(listener);
        }
    }
}
