package com.yachint.twitterdrift.ui.activity

import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.yachint.twitterdrift.R
import com.yachint.twitterdrift.databinding.ActivityMainBinding
import com.yachint.twitterdrift.ui.activity.common.BaseActivity
import com.yachint.twitterdrift.ui.injector.DaggerVFMTrendsComponent
import com.yachint.twitterdrift.ui.modules.VMFTrendsModule
import com.yachint.twitterdrift.ui.viewmodel.TrendsViewModel

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    lateinit var viewModel: TrendsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initMMKV()
        initTheme()
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        supportActionBar?.hide()

        initializeViewModel()
    }

    fun initializeViewModel(){
        val component = DaggerVFMTrendsComponent.builder().vMFTrendsModule(VMFTrendsModule(this)).build()
        val factory = component.getViewModelFactory()
        viewModel = ViewModelProviders.of(this, factory).get(TrendsViewModel::class.java)
    }
}