package com.walker.demo.paging3.api

import com.walker.demo.paging3.model.RepoResponse
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface GitHubService {

    /**
     * 在使用kotlin协程的suspend时候会出现java.lang.IllegalArgumentException: Unable to create call adapter for class java.lang.Object for method的错误，
     * 这是由于Retrofit的版本低于2.6.0引起的，更正即可。
     */
    @GET("search/repositories?sort=stars&q=Android")
    suspend fun searchRepos(@Query("page") page: Int, @Query("per_page") perPage: Int): RepoResponse

    companion object {
        private const val BASE_URL = "https://api.github.com/"

        fun create(): GitHubService {
            return Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(GitHubService::class.java)
        }
    }

}