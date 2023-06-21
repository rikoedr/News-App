package com.android.newsapp.model

data class SavedModel(
    val id: Int,
    val source: String,
    val title:String,
    val url: String,
    val publishedAt: String
)
