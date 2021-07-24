package com.yachint.twitterdrift.ui.activity

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.MenuItem
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.yachint.twitterdrift.R
import com.yachint.twitterdrift.data.common.LocationListener
import com.yachint.twitterdrift.databinding.ActivityMainBinding
import com.yachint.twitterdrift.ui.activity.common.BaseActivity
import com.yachint.twitterdrift.ui.dialog.CustomDialog
import com.yachint.twitterdrift.ui.fragment.StatsFragment
import com.yachint.twitterdrift.ui.fragment.TrendsFragment
import com.yachint.twitterdrift.ui.injector.DaggerLocationHelperComponent
import com.yachint.twitterdrift.ui.injector.DaggerVFMTrendsComponent
import com.yachint.twitterdrift.ui.modules.LocationHelperModule
import com.yachint.twitterdrift.ui.modules.VMFTrendsModule
import com.yachint.twitterdrift.ui.viewmodel.TrendsViewModel
import com.yachint.twitterdrift.utils.LocationHelper
import de.mateware.snacky.Snacky

class MainActivity : BaseActivity(), LocationListener,
    BottomNavigationView.OnNavigationItemSelectedListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: TrendsViewModel
    private lateinit var locationHelper: LocationHelper
    private lateinit var customDialog: CustomDialog
    private val LOCATION = 45
    private var isNavigatedToSettings = false
    private var woeid: Int = 0
    private var isUpdateReceived = false
    lateinit var activeFragment: Fragment
    private var trendsFragment = TrendsFragment()
    private var statsFragment = StatsFragment()

    override fun onCreate(savedInstanceState: Bundle?) {
        initMMKV()
        initTheme()
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        supportActionBar?.hide()

        //Fragment management
        binding.bottomNav.setOnNavigationItemSelectedListener(this)
        activeFragment = trendsFragment
        supportFragmentManager.beginTransaction().add(R.id.fragmentHolder, trendsFragment, "trends").commit()
        supportFragmentManager.beginTransaction().add(R.id.fragmentHolder, statsFragment, "stats")
            .hide(statsFragment).commit()

        //Initialize class components
        customDialog = CustomDialog(this)

        //Check if first time app has been opened
        if(!kv.decodeBool("location", false)){
            Log.d("MMKV", "location false ")
            customDialog.showDialogSingle(
                title = getString(R.string.explain_gps),
                body = getString(R.string.explain_gps_body),
                successText = getString(R.string.understand),
                handleSuccess = ::initializeLocationHelper,
                isNullable = false,
                type = "info"
            )
        } else {
            Log.d("MMKV", "location true ")
            woeid = kv.decodeInt("woeid")
            Log.i("Place", "$woeid")
        }

        // supportActionBar?.hide()
        initializeViewModel()
        setUpObservers()
    }

    fun refreshData(type: Int){
        //0 -> Initial fetch after woeid has been received
        //1 -> Delete existing data and refresh
        when(type){
            0 -> {
                viewModel.fetchRegionalTrends(woeid, 20)
                viewModel.fetchGlobalTrends(20)
            }

            1 -> {
                viewModel.deleteTrendsList()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        if(isNavigatedToSettings){
            isNavigatedToSettings = false
            customDialog.showLoadingDialog()
            Log.d("Location", "onResume: came back from settings, pinging...")
            Handler(Looper.getMainLooper()).postDelayed({
                locationHelper.getLastLocation()
            }, 1000)
        }
    }

    private fun initializeLocationHelper(){
        val locationComponent = DaggerLocationHelperComponent.builder().locationHelperModule(
            LocationHelperModule(
                this,
                this
            )
        ).build()

        locationHelper = locationComponent.getLocationHelper()
        locationHelper.getLastLocation()
    }

    fun navigateToSettings(){
        startActivity(
            Intent(this, SelectThemeActivity::class.java)
        )
    }

    private fun initializeViewModel(){
        val component = DaggerVFMTrendsComponent.builder().vMFTrendsModule(VMFTrendsModule(this)).build()
        val factory = component.getViewModelFactory()
        viewModel = ViewModelProvider(this, factory).get(TrendsViewModel::class.java)
//        viewModel.fetchTrendsList(23424848)
    }

    private fun setUpObservers(){
        viewModel.getErrorIdentifier().observe(this, { error ->
            if(error){
                notifyError()
            }
        })

//        viewModel.trendsList().observe(this, {
//            if(it.isEmpty()){
//                Log.d("OBSERVER", "MainActivity: EMPTY ")
//                if(woeid != 0){
//                    viewModel.fetchTrendsList(woeid)
//                }
//            } else {
//                Log.d("OBSERVER", "MainActivity: $it ")
//                if(isRefreshedClicked){
//                    isRefreshedClicked = false
//                    notifySuccess()
//                }
//            }
//        })

        if(!kv.decodeBool("location", false)){
            viewModel.getPlace().observe(this, {
                if(it != null){
                    woeid = it.woeid
                    Log.i("Place", "$woeid")
                    kv.encode("woeid", it.woeid)
                    kv.encode("country", it.country)
                    kv.encode("place", it.name)
                    showProcessCompletionDialog()
                }
            })
        }
    }

    fun notifySuccess(){
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

    private fun requestPermissions(){
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION
            ),
            LOCATION
        )
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)

        if(requestCode == LOCATION){
            if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                locationHelper.getLastLocation()
            } else {
                customDialog.showDialogDouble(
                    title = getString(R.string.explain_perm),
                    body = getString(R.string.explain_perm_body),
                    successText = getString(R.string.grant),
                    rejectText = getString(R.string.exit),
                    handleSuccess = ::takeUserToAppSettings,
                    handleReject = ::onBackPressed,
                    type = "info",
                    isNullable = false
                )
            }
        }
    }

    private fun takeUserToAppSettings(){
        val intent = Intent()
        intent.action = android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
        val uri = Uri.fromParts("package", packageName, null)
        intent.data = uri
        isNavigatedToSettings = true
        startActivity(intent)
    }

    private fun takeUserToLocationSettings(){
        isNavigatedToSettings = true
        startActivity(Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS))
    }

    override fun askPermission() {
        customDialog.dismiss()
        requestPermissions()
    }

    override fun askLocation() {
        customDialog.dismiss()
        customDialog.showDialogDouble(
            title = getString(R.string.explain_toggle),
            body = getString(R.string.explain_toggle_body),
            successText = getString(R.string.enable),
            rejectText = getString(R.string.cancel),
            handleSuccess = ::takeUserToLocationSettings,
            handleReject = ::onBackPressed,
            type = "info",
            isNullable = false
        )
    }

    override fun onLocationRetrieval(longitude: Double, latitude: Double) {
        isUpdateReceived = true
        Log.d("Location", "Main Longitude: $longitude")
        Log.d("Location", "Main Latitude: $latitude")
        viewModel.fetchPlaceId(latitude, longitude)
        kv.encode("location", true)
    }

    private fun initialFetch(){
        refreshData(0)
    }

    fun startLocationTimer(){
        Handler(Looper.getMainLooper()).postDelayed({
            if(!isUpdateReceived){
             showProcessErrorDialog()
            }
        }, 8000)
    }

    private fun showProcessErrorDialog(){
        customDialog.dismiss()
        customDialog.showDialogSingle(
            title = getString(R.string.explain_gps_fail),
            body = getString(R.string.explain_gps_fail_body),
            successText = getString(R.string.restart),
            handleSuccess = ::onBackPressed,
            isNullable = false,
            type = "error"
        )
    }

    private fun showProcessCompletionDialog(){
        customDialog.dismiss()
        customDialog.showDialogSingle(
            title = getString(R.string.explain_ping),
            body = getString(R.string.explain_ping_body),
            successText = getString(R.string.gotIt),
            handleSuccess = ::initialFetch,
            isNullable = true,
            type = "success"
        )
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        return  when(item.itemId) {
            R.id.menu_trends -> {
                supportFragmentManager.beginTransaction().hide(activeFragment)
                    .show(trendsFragment).commit()
                activeFragment = trendsFragment
                true
            }

            R.id.menu_stats -> {
                supportFragmentManager.beginTransaction().hide(activeFragment)
                    .show(statsFragment).commit()
                activeFragment = statsFragment
                true
            }

            else -> {
                supportFragmentManager.beginTransaction().hide(activeFragment)
                    .show(trendsFragment).commit()
                activeFragment = trendsFragment
                true
            }
        }
    }

}