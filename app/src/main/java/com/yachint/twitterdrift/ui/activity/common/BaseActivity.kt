package com.yachint.twitterdrift.ui.activity.common

import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.lifecycleScope
import com.tencent.mmkv.MMKV
import com.yachint.twitterdrift.R
import kotlinx.coroutines.launch

abstract class BaseActivity: AppCompatActivity() {

    lateinit var kv: MMKV

    fun initMMKV(){
        val str = MMKV.initialize(this)
        kv = MMKV.defaultMMKV()!!
        Log.d("MMKV", "initMMKV: $str")
    }

    fun initTheme(){
        lifecycleScope.launch {
            val theme = kv.decodeBool("theme", false)
            if (theme){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            }
            if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
                setTheme(R.style.DarkTheme)
                kv.encode("theme", true)
            } else {
                setTheme(R.style.LightTheme)
                kv.encode("theme", false)
            }
        }
    }
}