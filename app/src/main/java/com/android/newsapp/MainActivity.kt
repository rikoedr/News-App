package com.android.newsapp

import android.database.sqlite.SQLiteDatabase
import android.os.Bundle
import android.view.View
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDialogFragment
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.android.newsapp.adapter.VPAdapter
import com.android.newsapp.database.DBConfig
import com.android.newsapp.database.DBController
import com.android.newsapp.view.HomeFragment

class MainActivity : AppCompatActivity() {
    private lateinit var dbConfig: DBConfig
    private lateinit var database: SQLiteDatabase
    private lateinit var dbController: DBController

    private lateinit var rgNavigationBar: RadioGroup
    private lateinit var rbHome: RadioButton
    private lateinit var rbExplore: RadioButton
    private lateinit var rbSaved: RadioButton

    private lateinit var vpContent: ViewPager2
    private lateinit var vpAdapter: VPAdapter

    private lateinit var homeFragment: HomeFragment
    private lateinit var headlinesRV: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // CREATE LOCAL DATABASE
        dbConfig = DBConfig(this)
        database = dbConfig.readableDatabase
        dbController = DBController(this)

        // SET-UP VIEW PAGER
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

        // SET-UP NAVIGATION BAR
        rgNavigationBar = findViewById(R.id.rg_navbar)
        rbHome = findViewById(R.id.rb_navbar_home)
        rbExplore = findViewById(R.id.rb_navbar_explore)
        rbSaved = findViewById(R.id.rb_navbar_saved)

        rgNavigationBar.setOnCheckedChangeListener { group: RadioGroup?, checkedId: Int ->
            when (checkedId) {
                R.id.rb_navbar_explore -> vpContent.setCurrentItem(1, true)
                R.id.rb_navbar_saved -> vpContent.setCurrentItem(2, true)
                else -> vpContent.setCurrentItem(0, true)
            }
        }
    }

}