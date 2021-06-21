package com.yachint.twitterdrift.data.retrofit

import com.yachint.twitterdrift.data.model.response.trends.TrendsObject
import com.yachint.twitterdrift.data.model.response.woeid.PlaceModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TwitterApiService {

    @GET("trends/place.json")
    fun getTrends(
        @Query("id") woeid: Int
    ): Call<List<TrendsObject>>

    @GET("trends/closest.json")
    fun getPlaceId(
        @Query("lat") lat: Double,
        @Query("long") long: Double
    ): Call<List<PlaceModel>>
}