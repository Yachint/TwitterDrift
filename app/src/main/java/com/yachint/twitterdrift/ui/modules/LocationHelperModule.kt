package com.yachint.twitterdrift.ui.modules

import android.content.Context
import android.location.LocationManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.yachint.twitterdrift.data.common.LocationListener
import com.yachint.twitterdrift.utils.LocationHelper
import dagger.Module
import dagger.Provides

@Module
class LocationHelperModule(
    private val context: Context,
    private val locationListener: LocationListener
) {

    @Provides
    fun provideFusedLocationProviderClient():
            FusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(context)

    @Provides
    fun provideLocationManager():
            LocationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager

    @Provides
    fun provideLocationHelper(
        fusedLocationProviderClient: FusedLocationProviderClient,
        locationManager: LocationManager
    ): LocationHelper = LocationHelper(
                context,
                fusedLocationProviderClient,
                locationManager,
                locationListener
            )
}