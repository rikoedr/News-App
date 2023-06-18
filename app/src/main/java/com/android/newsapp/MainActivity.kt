package com.android.newsapp

import android.os.Bundle
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.android.newsapp.adapter.VPAdapter

class MainActivity : AppCompatActivity() {
    private lateinit var navigationBar: RadioGroup
    private lateinit var rbHome: RadioButton
    private lateinit var rbExplore: RadioButton
    private lateinit var rbSaved: RadioButton

    private lateinit var vpContent: ViewPager2
    private lateinit var vpAdapter: VPAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initNavigationBar()
        initViewPager()
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