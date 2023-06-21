package com.android.newsapp.database

object SavedTable {
    // SAVED NEWS TABLE CONFIGURATION
    const val TABLE_NAME = "saved_news"
    const val ID = "id"
    const val SOURCE = "source"
    const val TITLE = "title"
    const val URL = "url"
    const val PUBLISHED_AT = "publishedAt"

    // DB STRING COLLECTION
    const val createTableQuery = "CREATE TABLE $TABLE_NAME (" +
            "$ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "$SOURCE TEXT NOT NULL, " +
            "$TITLE TEXT NOT NULL, " +
            "$URL TEXT NOT NULL, " +
            "$PUBLISHED_AT TEXT NOT NULL)"

    const val selectQuery = "SELECT * FROM $TABLE_NAME"

    fun insertQuery(source: String, title: String, publishedAt: String, url: String): String{
        return "INSERT INTO $TABLE_NAME ($SOURCE, $TITLE, $URL, $PUBLISHED_AT) " +
                "VALUES ('$source', '$title', '$url', '$publishedAt')"
    }

    fun deleteQuery(url: String): String = "DELETE FROM $TABLE_NAME WHERE $URL = '$url'"

    fun searchQuery(url: String): String = "SELECT * FROM $TABLE_NAME WHERE $URL = '$url'"
}