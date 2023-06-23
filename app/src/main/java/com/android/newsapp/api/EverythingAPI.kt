package com.android.newsapp.api

import com.android.newsapp.model.NewsModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface EverythingAPI {
    @GET("everything")
    fun getEverythingNews(
        @Query("q") q:String,
        @Query("apiKey") apiKey: String,
        @Query("pageSize") pageSize: Int,
        @Query("page") page: Int
    ): Call<NewsModel>
}