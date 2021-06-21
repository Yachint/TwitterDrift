package com.yachint.twitterdrift.utils

import android.Manifest
import android.annotation.SuppressLint
import android.content.Context
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Looper
import android.util.Log
import androidx.core.app.ActivityCompat
import com.google.android.gms.location.*
import com.yachint.twitterdrift.data.common.LocationListener
import com.yachint.twitterdrift.ui.activity.MainActivity

class LocationHelper(
    private val context: Context,
    private var fusedLocationProviderClient: FusedLocationProviderClient,
    private val locationManager: LocationManager,
    private val locationListener: LocationListener
) {

    @SuppressLint("MissingPermission")
    fun getLastLocation(){
        if(checkPermissions()){
            if(isLocationEnabled()){
                fusedLocationProviderClient.lastLocation.addOnCompleteListener {
                    val location = it.result
                    if(location == null){
                        (context as MainActivity).startLocationTimer()
                        requestNewLocationData()
                    } else {
                        Log.d("Location", "Last Longitude: ${location.longitude}")
                        Log.d("Location", "Last Longitude: ${location.latitude}")
                        locationListener.onLocationRetrieval(location.longitude, location.latitude)
                    }
                }
            } else {
              locationListener.askLocation()
            }
        } else {
            locationListener.askPermission()
        }
    }

    @SuppressLint("MissingPermission")
    fun requestNewLocationData(){
        val locationRequest = LocationRequest.create().apply {
            priority = LocationRequest.PRIORITY_HIGH_ACCURACY
            interval = 5
            fastestInterval = 0
            numUpdates = 1
        }

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper())
    }

    private val locationCallback = object : LocationCallback(){
        override fun onLocationResult(p0: LocationResult) {
            val newLocation = p0.lastLocation
            Log.d("Location", "Longitude: ${newLocation.longitude}")
            Log.d("Location", "Latitude: ${newLocation.latitude}")
            locationListener.onLocationRetrieval(newLocation.longitude, newLocation.latitude)
        }
    }

    private fun isLocationEnabled(): Boolean =
        locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)
                || locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER)


    private fun checkPermissions(): Boolean =
        ActivityCompat.checkSelfPermission(
            context,
            Manifest.permission.ACCESS_FINE_LOCATION
        ) == PackageManager.PERMISSION_GRANTED
}