package com.yachint.twitterdrift.utils

import android.content.Context
import androidx.constraintlayout.motion.widget.MotionLayout
import androidx.viewpager2.widget.ViewPager2

class PagerMotionHelper(
    val context: Context,
    private val headerMotionLayout: MotionLayout,
    viewPager: ViewPager2
): ViewPager2.OnPageChangeCallback() {

    init {
        viewPager.registerOnPageChangeCallback(this)
    }

    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
        val numOfPages = 2
        headerMotionLayout.progress = (position + positionOffset) / (numOfPages - 1)
    }

    override fun onPageSelected(position: Int) {

    }

    override fun onPageScrollStateChanged(state: Int) {

    }

}