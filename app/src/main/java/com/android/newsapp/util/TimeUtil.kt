package com.android.newsapp.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

object TimeUtil {
    fun Formatter(time: String): String{
        val inputFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss'Z'")
        val outputFormat = DateTimeFormatter.ofPattern("dd MMM yyyy")

        return LocalDateTime.parse(time, inputFormat).format(outputFormat)
    }
}