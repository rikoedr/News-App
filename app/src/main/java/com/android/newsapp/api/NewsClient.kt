package com.android.newsapp.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NewsClient{
    val newsAPI: NewsAPI by lazy { clientBuilder().create(NewsAPI::class.java) }
    val exploreAPI: EverythingAPI by lazy { clientBuilder().create(EverythingAPI::class.java) }

    private fun clientBuilder(): Retrofit{
        return Retrofit.Builder()
            .baseUrl(ContractAPI.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}

