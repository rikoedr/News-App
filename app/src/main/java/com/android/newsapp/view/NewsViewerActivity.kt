package com.android.newsapp.view

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
import com.android.newsapp.R
import com.android.newsapp.database.DBController
import com.android.newsapp.util.TimeUtil

class NewsViewerActivity : AppCompatActivity() {
    private lateinit var source: String
    private lateinit var title: String
    private lateinit var publishedAt: String
    private lateinit var urlToOpen: String

    private lateinit var webView: WebView
    private lateinit var progressBar: ProgressBar

    private lateinit var btBack: Button
    private lateinit var btSave: ToggleButton
    private lateinit var tvTopSource: TextView

    private lateinit var dbController: DBController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news_viewer)

        // CATCH ARTICLE DATA FROM MAIN ACTIVITY
        source = intent.getStringExtra("articleSource")!!
        title = intent.getStringExtra("articleTitle")!!
        publishedAt = intent.getStringExtra("articlePublishedAt")!!
        urlToOpen = intent.getStringExtra("urlToOpen")!!

        // LAYOUT ELEMENTS SET-UP
        setupTopBar()
        setupWebView()

    }
    override fun onBackPressed() {
        finish()
    }

    private fun setupWebView(){
        // Initialize webview client
        webView = findViewById(R.id.wv_news_viewer)
        webView.webViewClient = WebViewClient()
        webView.settings.javaScriptEnabled = true
        webView.settings.domStorageEnabled = true
        progressBar = findViewById(R.id.pb_loading_web)

        // Progress bar event
        webView.webViewClient = object: WebViewClient(){
            override fun onPageStarted(view: WebView?, url: String?, favicon: Bitmap?) {
                progressBar.visibility = View.GONE
                super.onPageStarted(view, url, favicon)
            }
        }
        // Load articles url
        webView.loadUrl(urlToOpen)
    }

    private fun setupTopBar(){
        // Initialize top bar elements
        btBack = findViewById(R.id.bt_news_viewer_back)
        btSave = findViewById(R.id.tb_news_viewer_save)
        tvTopSource = findViewById(R.id.tv_news_viewer_source)
        tvTopSource.text = source

        // Back button event handling
        btBack.setOnClickListener{
            finish()
        }
        // Top save button event handling
        setupSaveButton()
    }

    private fun setupSaveButton(){
        dbController = DBController(this) // Connect to database controller
        // Check article save status
        if (dbController.isArticleSaved(urlToOpen)){
            dbController.closeDatabase()
            btSave.isChecked = true
        } else {
            dbController.closeDatabase()
            btSave.isChecked = false
        }
        // Save button event handling
        btSave.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                dbController.insertArticles(source, title, TimeUtil.Formatter(publishedAt), urlToOpen)
                Toast.makeText(this, "Articles Saved", Toast.LENGTH_SHORT).show()
            } else {
                dbController.deleteArticles(urlToOpen)
                Toast.makeText(this, "Articles Unsaved", Toast.LENGTH_SHORT).show() }
        }
    }

}