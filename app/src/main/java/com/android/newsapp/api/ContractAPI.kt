package com.android.newsapp.api

object ContractAPI {
    // API DATA
    const val BASE_URL: String = "https://newsapi.org/v2/"
    const val COUNTRY: String = "us"
    const val API_KEY: String = null // CHANGE THIS WITH YOUR API KEY
    const val PAGE_SIZE_EXPLORE: Int = 10

    // EXPLORE NEWS CATEGORY
    const val EVERYTHING: String = "economy OR technology OR politic OR sport OR international OR science OR health"
    const val TECH: String = "technology"
    const val HEALTH: String = "health OR fitness OR well-being"
    const val ECONOMY: String = "economy OR business OR finance OR investment"
    const val SPORT: String = "sport"
    const val SCIENCE: String = "science OR education OR space OR nature OR research"
}