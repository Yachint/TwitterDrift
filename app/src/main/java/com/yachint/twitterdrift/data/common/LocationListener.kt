package com.yachint.twitterdrift.data.common

interface LocationListener {
    fun askPermission()
    fun askLocation()
    fun onLocationRetrieval(longitude: Double, latitude: Double)
}