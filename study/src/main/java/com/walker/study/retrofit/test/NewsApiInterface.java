package com.walker.study.retrofit.test;

import com.walker.study.retrofit.annotation.GET;
import com.walker.study.retrofit.annotation.Query;

import okhttp3.Call;

public interface NewsApiInterface {
    @GET("toutiao/index")
    Call getNewsList(@Query("key") String newsKey, @Query("type") String channelId);
}
