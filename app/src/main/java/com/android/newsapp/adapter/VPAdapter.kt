package com.android.newsapp.adapter

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.android.newsapp.view.ExploreFragment
import com.android.newsapp.view.HomeFragment
import com.android.newsapp.view.SavedFragment

class VPAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity){
    override fun createFragment(position: Int): Fragment {
        return when(position){
            1 -> ExploreFragment()
            2 -> SavedFragment()
            else -> HomeFragment()
        }
    }

    override fun getItemCount(): Int {
        return 3
    }
}