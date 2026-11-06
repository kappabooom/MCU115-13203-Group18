package com.example.threadhandlerwizard

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class MainActivity : AppCompatActivity() {

    private val titles = listOf("Start", "Progress", "Result")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val pager = findViewById<ViewPager2>(R.id.viewPager)
        val tabs = findViewById<TabLayout>(R.id.tabLayout)

        pager.adapter = object : FragmentStateAdapter(this) {
            override fun getItemCount() = titles.size
            override fun createFragment(position: Int): Fragment = when (position) {
                0 -> StartFragment()
                1 -> ProgressFragment()
                else -> ResultFragment()
            }
        }
        // 讓三個頁面都保持存在，切換分頁時觀察者不會被重建
        pager.offscreenPageLimit = 2

        TabLayoutMediator(tabs, pager) { tab, pos -> tab.text = titles[pos] }.attach()
    }
}
