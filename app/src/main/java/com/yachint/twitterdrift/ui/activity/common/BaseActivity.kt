package com.yachint.twitterdrift.ui.activity.common

import android.content.res.Configuration
import android.content.res.Configuration.UI_MODE_NIGHT_YES
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
            val firstLaunch = kv.decodeBool("first", true)
            val theme = kv.decodeBool("theme", false)
            //Check for initial launch
            if(firstLaunch){
                kv.encode("first", false)
                //If yes, then borrow theme from OS
                if((resources.configuration.uiMode and
                            Configuration.UI_MODE_NIGHT_MASK == UI_MODE_NIGHT_YES)){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
            } else {
                //If no, then check for user defined theme
                if (theme){
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                }
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