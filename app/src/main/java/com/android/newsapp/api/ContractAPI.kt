package com.android.newsapp.api

object ContractAPI {

    // BASE CONTRACT
    const val BASE_URL: String = "https://newsapi.org/v2/"
    const val COUNTRY: String = "us"
    const val API_KEY: String = "596fe12676a14ca8b9f24b9b018e1875"
    const val PAGE_SIZE_EXPLORE: Int = 10

    // EXPLORE NEWS : CATEGORY
    const val TECH: String = "technology"
    const val HEALTH: String = "health OR fitness OR well-being"
    const val ECONOMY: String = "economy OR business OR finance OR investment"
    const val SPORT: String = "sport"
    const val SCIENCE: String = "science OR education OR space OR nature OR research"
}