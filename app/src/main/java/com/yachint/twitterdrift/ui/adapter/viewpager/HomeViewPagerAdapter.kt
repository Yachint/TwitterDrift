package com.yachint.twitterdrift.ui.adapter.viewpager

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.yachint.twitterdrift.ui.fragment.trends.GlobalTrendsFragment
import com.yachint.twitterdrift.ui.fragment.trends.RegionalTrendsFragment

class HomeViewPagerAdapter(
    val lifecycle: Lifecycle,
    fm: FragmentManager,
    private val globalTrendsFragment: GlobalTrendsFragment,
    private val regionalTrendsFragment: RegionalTrendsFragment
): FragmentStateAdapter(fm, lifecycle) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return  when(position) {
            0 -> {
                regionalTrendsFragment
            }

            1 -> {
                globalTrendsFragment
            }

            else -> {
                regionalTrendsFragment
            }
        }
    }
}