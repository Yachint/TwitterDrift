package com.yachint.twitterdrift.data.retrofit

import com.yachint.twitterdrift.data.model.response.trends.TrendsMainResponse
import com.yachint.twitterdrift.data.model.response.woeid.PlaceMainResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface TwitterApiService {

    @GET("trends/getTrends")
    fun getTrends(
        @Query("woeid") woeid: Int,
        @Query("limit") limit: Int
    ): Call<TrendsMainResponse>

    @GET("trends/getWoeid")
    fun getPlaceId(
        @Query("lat") lat: Double,
        @Query("long") long: Double
    ): Call<PlaceMainResponse>
}