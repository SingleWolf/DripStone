package com.walker.network.retrofit.base;

import com.walker.network.dsn.OkHttpDns;
import com.walker.network.environment.EnvironmentActivity;
import com.walker.network.environment.IEnvironment;
import com.walker.network.retrofit.errorhandler.HttpErrorHandler;
import com.walker.network.retrofit.interceptor.CommonRequestInterceptor;
import com.walker.network.retrofit.interceptor.CommonResponseInterceptor;
import com.walker.network.wakeNetwork.DoraemonWeakNetworkInterceptor;
import com.walker.network.wakeNetwork.WeakNetworkManager;

import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.ObservableSource;
import io.reactivex.ObservableTransformer;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @Author Walker
 * @Date 2020-03-27 10:36
 * @Summary 基于Retrofit的网络请求基类
 */
public abstract class RetrofitNetworkApi implements IEnvironment {
    private static INetworkRequiredInfo mNetworkRequiredInfo;
    private static final int CACHE_SIZE = 100 * 1024 * 1024;
    private static HashMap<String, Retrofit> mRetrofitHashMap = new HashMap<>();
    private String mBaseUrl;
    private OkHttpClient mOkHttpClient;
    private static boolean mIsFormal = true;

    public RetrofitNetworkApi() {
        if (!mIsFormal) {
            mBaseUrl = getTest();
        }
        mBaseUrl = getFormal();
    }

    public static void init(INetworkRequiredInfo networkRequiredInfo) {
        mNetworkRequiredInfo = networkRequiredInfo;
        mIsFormal = EnvironmentActivity.isOfficialEnvironment(networkRequiredInfo.getApplicationContext());
        int networkType = EnvironmentActivity.getNetworkType(networkRequiredInfo.getApplicationContext());
        WeakNetworkManager.get().setType(networkType);
    }

    protected Retrofit getRetrofit(Class service) {
        if (mRetrofitHashMap.get(mBaseUrl + service.getName()) != null) {
            return mRetrofitHashMap.get(mBaseUrl + service.getName());
        }
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder();
        retrofitBuilder.baseUrl(mBaseUrl);
        retrofitBuilder.client(getOkHttpClient());
        retrofitBuilder.addConverterFactory(GsonConverterFactory.create());
        retrofitBuilder.addCallAdapterFactory(RxJava2CallAdapterFactory.create());
        Retrofit retrofit = retrofitBuilder.build();
        mRetrofitHashMap.put(mBaseUrl + service.getName(), retrofit);
        return retrofit;
    }

    private OkHttpClient getOkHttpClient() {
        if (mOkHttpClient == null) {
            OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
            okHttpClientBuilder.dns(OkHttpDns.getInstance(mNetworkRequiredInfo.getApplicationContext()));
            if (getInterceptor() != null) {
                okHttpClientBuilder.addInterceptor(getInterceptor());
            }
            int cacheSize = CACHE_SIZE;
            okHttpClientBuilder.cache(new Cache(mNetworkRequiredInfo.getApplicationContext().getCacheDir(), cacheSize));
            okHttpClientBuilder.addInterceptor(new DoraemonWeakNetworkInterceptor());
            okHttpClientBuilder.addInterceptor(new CommonRequestInterceptor(mNetworkRequiredInfo));
            okHttpClientBuilder.addInterceptor(new CommonResponseInterceptor());
            if (mNetworkRequiredInfo != null && (mNetworkRequiredInfo.isDebug())) {
                HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
                httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                okHttpClientBuilder.addInterceptor(httpLoggingInterceptor);
            }
            mOkHttpClient = okHttpClientBuilder.build();
        }
        return mOkHttpClient;
    }


    public <T> ObservableTransformer<T, T> applySchedulers(final Observer<T> observer) {
        return new ObservableTransformer<T, T>() {
            @Override
            public ObservableSource<T> apply(Observable<T> upstream) {
                Observable<T> observable = (Observable<T>) upstream
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .map(getAppErrorHandler())
                        .onErrorResumeNext(new HttpErrorHandler<T>());
                observable.subscribe(observer);
                return observable;
            }
        };
    }

    protected abstract Interceptor getInterceptor();

    protected abstract <T> Function<T, T> getAppErrorHandler();
}
