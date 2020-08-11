package com.walker.core.router;

import android.util.Log;

import java.util.ServiceLoader;

/**
 * @Author Walker
 * @Date 2020-08-11 10:34
 * @Summary 路由加载
 */
public final class RouterLoader {
    private static final String TAG = "RouterLoader";

    private RouterLoader() {
    }

    public static <S> S load(Class<S> service) {
        try {
            return ServiceLoader.load(service).iterator().next();
        } catch (Exception e) {
            Log.i(TAG, e.toString());
            return null;
        }
    }
}
