package com.walker.network.retrofit.interceptor;

import com.walker.core.log.LogHelper;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Response;

/**
 * @Author Walker
 * @Date 2020-03-27 10:24
 * @Summary 响应拦截器
 */
public class CommonResponseInterceptor implements Interceptor {
    private static final String TAG = "ResponseInterceptor";

    @Override
    public Response intercept(Chain chain) throws IOException {
        long requestTime = System.currentTimeMillis();
        Response response = chain.proceed(chain.request());
        LogHelper.get().d(TAG, "requestUrl =" + response.request().url().toString() + "and requestTime=" + (System.currentTimeMillis() - requestTime),true);
        return response;
    }
}
