package com.walker.network.retrofit.interceptor;

import com.walker.network.retrofit.base.INetworkRequiredInfo;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * @Author Walker
 * @Date 2020-03-27 10:23
 * @Summary 请求拦截器
 */
public class CommonRequestInterceptor implements Interceptor {
    private static final String TAG = "RequestInterceptor";
    private INetworkRequiredInfo requiredInfo;
    public CommonRequestInterceptor(INetworkRequiredInfo requiredInfo){
        this.requiredInfo = requiredInfo;
    }
    @Override
    public Response intercept(Chain chain) throws IOException {
        Request.Builder builder = chain.request().newBuilder();
        //builder.cacheControl(CacheControl.FORCE_CACHE);
        builder.addHeader("os", this.requiredInfo.getOS());
        builder.addHeader("appVersion",this.requiredInfo.getAppVersionCode());
        return chain.proceed(builder.build());
    }
}
