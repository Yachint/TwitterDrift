package com.yachint.twitterdrift.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.databinding.DataBindingUtil
import com.yachint.twitterdrift.R
import com.yachint.twitterdrift.databinding.ActivitySelectThemeBinding
import com.yachint.twitterdrift.ui.activity.common.BaseActivity
import hari.bounceview.BounceView

class SelectThemeActivity : BaseActivity() {

    private lateinit var binding: ActivitySelectThemeBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        initMMKV()
        initTheme()
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_select_theme)

        //Deciding which Radio button group to activate
        if(kv.decodeBool("theme")){
            binding.dark.isChecked = true
        } else {
            binding.light.isChecked = true
        }

        BounceView.addAnimTo(binding.back)
        binding.back.setOnClickListener {
            onBackPressed()
        }

        binding.save.setOnClickListener {
            if(binding.dark.isChecked){
                kv.encode("theme", true)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                kv.encode("theme", false)
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
        }
    }
}