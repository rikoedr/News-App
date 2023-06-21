package com.android.newsapp.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DBConfig(context: Context) : SQLiteOpenHelper(context, "news_app", null, 1) {
    override fun onCreate(db: SQLiteDatabase?) {
        // SETUP SAVED NEWS TABLE
        db?.execSQL(SavedTable.createTableQuery)
        val sampleData1 = SavedTable.insertQuery("GOAL", "Why Chelsea must go all out to sign £80m Caicedo", "17 Jun 2023", "https://www.goal.com/en/lists/moises-caicedo-six-reasons-chelsea-break-bank-ngolo-kante-successor/blt987e4339e237ef26")
        val sampleData2 = SavedTable.insertQuery("Mashable", "Google, Maker of AI Chatbot Bard, Warns Its Employees About Using Chatbots", "15 Jun 2023", "https://sea.mashable.com/tech/24314/google-maker-of-ai-chatbot-bard-warns-its-employees-about-using-chatbots")
        val sampleData3 = SavedTable.insertQuery("Science Update", "Eating earlier in the day may improve blood sugar levels, slow prediabetes progression", "15 Jun 2023", "https://www.medicalnewstoday.com/articles/eating-earlier-in-the-day-may-improve-blood-sugar-levels-slow-prediabetes-progession")

        db?.execSQL(sampleData1)
        db?.execSQL(sampleData2)
        db?.execSQL(sampleData3)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
    }
}