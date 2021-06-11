package com.yachint.twitterdrift.ui.activity

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.yachint.twitterdrift.R
import com.yachint.twitterdrift.databinding.ActivityMainBinding
import com.yachint.twitterdrift.ui.activity.common.BaseActivity
import com.yachint.twitterdrift.ui.injector.DaggerVFMTrendsComponent
import com.yachint.twitterdrift.ui.modules.VMFTrendsModule
import com.yachint.twitterdrift.ui.viewmodel.TrendsViewModel
import de.mateware.snacky.Snacky
import hari.bounceview.BounceView

class MainActivity : BaseActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: TrendsViewModel
    var isRefreshedClicked = false

    override fun onCreate(savedInstanceState: Bundle?) {
        initMMKV()
        initTheme()
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
//        supportActionBar?.hide()

        initializeViewModel()
        setUpObservers()

        // Bind buttons to functionalities
        BounceView.addAnimTo(binding.btnRefresh)
        binding.btnRefresh.setOnClickListener {
            refreshData()
            isRefreshedClicked = true
        }

        binding.btnSettings.setOnClickListener {
            startActivity(
                Intent(this, SelectThemeActivity::class.java)
            )
        }
    }

    private fun refreshData(){
        viewModel.deleteTrendsList()
//        viewModel.fetchTrendsList(23424848)
    }

    private fun initializeViewModel(){
        val component = DaggerVFMTrendsComponent.builder().vMFTrendsModule(VMFTrendsModule(this)).build()
        val factory = component.getViewModelFactory()
        viewModel = ViewModelProvider(this, factory).get(TrendsViewModel::class.java)
//        viewModel.fetchTrendsList(23424848)
        viewModel.getTrendsList()
    }

    private fun setUpObservers(){
        viewModel.getErrorIdentifier().observe(this, Observer { error ->
            if(error){
                notifyError()
            }
        })

        viewModel.trendsList().observe(this, Observer {
            if(it.isEmpty()){
                Log.d("OBSERVER", "MainActivity: EMPTY ")
                viewModel.fetchTrendsList(23424848)
            } else {
                Log.d("OBSERVER", "MainActivity: $it ")
                if(isRefreshedClicked){
                    isRefreshedClicked = false
                    notifySuccess()
                }
            }
        })
    }

    private fun notifySuccess(){
        Snacky.builder().setActivity(this)
            .setBackgroundColor(ContextCompat.getColor(this, R.color.colorBlue))
            .setText("Trends Refreshed!")
            .setTextColor(ContextCompat.getColor(this, R.color.colorWhite))
            .setDuration(Snacky.LENGTH_LONG)
            .setIcon(R.drawable.ic_check)
            .build().show()
    }

    private fun notifyError(){
        Snacky.builder().setActivity(this)
            .setBackgroundColor(ContextCompat.getColor(this, R.color.colorRed))
            .setText("We faced some errors, please try again!")
            .setTextColor(ContextCompat.getColor(this, R.color.colorWhite))
            .setDuration(Snacky.LENGTH_LONG)
            .setIcon(R.drawable.ic_error)
            .build().show()
    }
}