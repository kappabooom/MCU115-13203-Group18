package com.example.foodorderingwizard

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

/** 四個副畫面：主餐、副餐、飲料、確認 */
class WizardPagerAdapter(activity: FragmentActivity) : FragmentStateAdapter(activity) {

    companion object {
        const val PAGE_MAIN = 0
        const val PAGE_SIDES = 1
        const val PAGE_DRINK = 2
        const val PAGE_CONFIRM = 3
    }

    override fun getItemCount(): Int = 4

    override fun createFragment(position: Int): Fragment = when (position) {
        PAGE_MAIN -> MainMealFragment()
        PAGE_SIDES -> SideDishesFragment()
        PAGE_DRINK -> DrinkFragment()
        else -> ConfirmFragment()
    }
}
