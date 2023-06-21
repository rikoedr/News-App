package com.android.newsapp

import android.content.Intent
import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.android.newsapp.adapter.VPAdapter
import com.android.newsapp.database.DBConfig
import com.android.newsapp.database.DBController
import com.android.newsapp.model.SavedModel

class MainActivity : AppCompatActivity() {
    private lateinit var dbConfig: DBConfig
    private lateinit var database: SQLiteDatabase
    private lateinit var dbController: DBController

    private lateinit var navigationBar: RadioGroup
    private lateinit var rbHome: RadioButton
    private lateinit var rbExplore: RadioButton
    private lateinit var rbSaved: RadioButton

    private lateinit var vpContent: ViewPager2
    private lateinit var vpAdapter: VPAdapter

    private lateinit var savedNewsList: MutableList<SavedModel>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // CREATE LOCAL DATABASE
        dbConfig = DBConfig(this)
        database = dbConfig.readableDatabase
        dbController = DBController(this)

        // GET SAVED NEWS DATABASE

        // INIT LAYOUT ELEMENTS
        initNavigationBar()
        initViewPager()

        // troubleshooting
        val logo = findViewById<ImageView>(R.id.title_iv)
        logo.setOnClickListener{
            val intent = Intent(this, NewsViewerActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initNavigationBar(){
        navigationBar = findViewById(R.id.rg_top_navigation)
        rbHome = findViewById(R.id.rb_navigation_home)
        rbExplore = findViewById(R.id.rb_navigation_explore)
        rbSaved = findViewById(R.id.rb_navigation_saved)

        navigationBar.setOnCheckedChangeListener { group: RadioGroup?, checkedId: Int ->
            when (checkedId) {
                R.id.rb_navigation_explore -> vpContent.setCurrentItem(1, true)
                R.id.rb_navigation_saved -> vpContent.setCurrentItem(2, true)
                else -> vpContent.setCurrentItem(0, true)
            }
        }
    }
    private fun initViewPager(){
        vpContent = findViewById(R.id.vp_content)
        vpAdapter = VPAdapter(this)
        vpContent.adapter = vpAdapter
        vpContent.registerOnPageChangeCallback(object : OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                when(position){
                    1 -> rbExplore.isChecked = true
                    2 -> rbSaved.isChecked = true
                    else -> rbHome.isChecked = true
                }
            }
        })

    }
}