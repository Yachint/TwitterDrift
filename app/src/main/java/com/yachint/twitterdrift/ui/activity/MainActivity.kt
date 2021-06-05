package com.yachint.twitterdrift.ui.activity

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.yachint.twitterdrift.R
import com.yachint.twitterdrift.databinding.ActivityMainBinding
import com.yachint.twitterdrift.ui.activity.common.BaseActivity

class MainActivity : BaseActivity() {

    lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initMMKV()
        initTheme()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        supportActionBar?.hide()


    }
}