package com.walker.collect.news.newslist;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface NewsApiInterface {
    @GET("toutiao/index")
    Observable<NewsListBean> getNewsList(@Query("key") String newsKey, @Query("type") String channelId);
}
