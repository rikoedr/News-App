package com.android.newsapp

import android.content.Intent
import android.graphics.Bitmap
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.webkit.WebView
import android.webkit.WebViewClient
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import android.widget.ToggleButton
import com.android.newsapp.database.DBController

class NewsViewerActivity : AppCompatActivity() {
    private lateinit var source: String
    private lateinit var title: String
    private lateinit var publishedAt: String
    private lateinit var urlToOpen: String

    private lateinit var webView: WebView
    private lateinit var progressBar: ProgressBar

    private lateinit var backButton: Button
    private lateinit var saveButton: ToggleButton
    private lateinit var tvTopSource: TextView


    private lateinit var dbController: DBController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_viewer)

        // INIT DBCONTROLLER
        dbController = DBController(this)

        // CATCH ARTICLE DATA
        source = intent.getStringExtra("articleSource")!!
        title = intent.getStringExtra("articleTitle")!!
        publishedAt = intent.getStringExtra("articlePublishedAt")!!
        urlToOpen = intent.getStringExtra("urlToOpen")!!


        // SETUP TOP BAR LAYOUT
        backButton = findViewById(R.id.bt_news_viewer_back)
        saveButton = findViewById(R.id.tb_news_viewer_save)
        tvTopSource = findViewById(R.id.tv_news_viewer_source)
        topBackButton()
        topSaveButton()
        tvTopSource.text = source

        // SETUP WEB VIEW
        webView = findViewById(R.id.wv_news_viewer)
        webView.webViewClient = WebViewClient()
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true

        // PROGRESS BAR EVENT
        progressBar = findViewById(R.id.pb_loading_web)
        progressBarEvent()

        // LOAD URL
        webView.loadUrl(urlToOpen)
    }

    override fun onBackPressed() {
        // DESTROY ACTIVITY WHEN USER PRESSED BACK BUTTON
        finish()
    }

    private fun progressBarEvent(){
        webView.webViewClient = object: WebViewClient(){
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                progressBar.visibility = View.GONE
                super.onPageStarted(view, url, favicon)
            }
        }
    }

    private fun topBackButton(){
        backButton.setOnClickListener{
            finish()
        }
    }

    private fun topSaveButton(){
        // CHECK ARTICLE SAVE STATUS
        when(dbController.isArticleSaved(urlToOpen)){
            true -> {
                dbController.closeDatabase()
                saveButton.isChecked = true
            }
            else -> {
                dbController.closeDatabase()
                saveButton.isChecked = false
            }
        }

        // SAVE BUTTON EVENT ON CLICK
        val isSaveButtonChecked = saveButton.isChecked
        saveButton.setOnCheckedChangeListener { buttonView, isChecked ->
            when(isChecked){
                true -> {
                    dbController.insertArticles(source, title, publishedAt, urlToOpen)
                    Toast.makeText(this, "Articles Saved", Toast.LENGTH_SHORT).show()
                }
                else -> {
                    dbController.deleteArticles(urlToOpen)
                    Toast.makeText(this, "Articles Unsaved", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}