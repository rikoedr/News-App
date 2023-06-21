package com.android.newsapp.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.android.newsapp.model.SavedModel

class DBController(context: Context) {
    private val dbConfig: DBConfig = DBConfig(context)
    private lateinit var database: SQLiteDatabase

    fun openDatabase(){ database = dbConfig.readableDatabase }
    fun closeDatabase(){ database.close() }
    fun getSavedNews(): MutableList<SavedModel>{
        openDatabase()
        val savedNewsList: MutableList<SavedModel> = mutableListOf()
        val savedCursor = database.rawQuery(SavedTable.selectQuery, null)

        savedCursor.moveToFirst()
        for (i in 0 until savedCursor.count) {
            savedCursor.moveToPosition(i)

            val id = savedCursor.getInt(0)
            val source = savedCursor.getString(1)
            val title = savedCursor.getString(2)
            val publishedAt = savedCursor.getString(3)
            val url = savedCursor.getString(4)

            val savedModel = SavedModel(id, source, title, publishedAt, url)
            savedNewsList.add(savedModel)
        }
        closeDatabase()
        return savedNewsList
    }
    fun isArticleSaved(url: String): Boolean{
        openDatabase()
        val savedCursor = database.rawQuery(SavedTable.searchQuery(url), null)
        return savedCursor.moveToFirst()
    }
    fun insertArticles(source: String, title: String, publishedAt: String, url: String){
        openDatabase()
        database.execSQL(SavedTable.insertQuery(source, title, publishedAt, url))
        closeDatabase()
    }
    fun deleteArticles(url: String){
        openDatabase()
        database.execSQL(SavedTable.deleteQuery(url))
        closeDatabase()
    }
}