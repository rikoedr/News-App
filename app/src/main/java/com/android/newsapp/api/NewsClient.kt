package com.android.newsapp.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object NewsClient{
    val headlinesAPI: HeadlinesAPI by lazy { clientBuilder().create(HeadlinesAPI::class.java) }
    val exploreAPI: ExploreAPI by lazy { clientBuilder().create(ExploreAPI::class.java) }

    private fun clientBuilder(): Retrofit{
        return Retrofit.Builder()
            .baseUrl(ContractAPI.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
}

