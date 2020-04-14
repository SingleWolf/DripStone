package com.walker.collect.cook.cooklist;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface CookApiInterface {
    @GET("cook/query")
    Observable<CookListBean> listCook(@Query("key") String key, @Query("menu") String menu, @Query("pn") String pn, @Query("rn") String rn);
}
