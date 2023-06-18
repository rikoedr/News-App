package com.android.newsapp.model

data class NewsModel(
    val status: String,
    val totalResults: Int,
    val articles: List<Articles>
)

data class Articles(
    val source: ArticleSource,
    val author: String?,
    val title: String,
    val description: String,
    val url: String,
    val urlToImage:String?,
    val publishedAt: String
)

data class ArticleSource(
    val id: String?,
    val name: String
)
