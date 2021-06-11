package com.yachint.twitterdrift.data.retrofit

import com.yachint.twitterdrift.data.model.response.trends.TrendsMainResponse
import com.yachint.twitterdrift.data.model.response.trends.TrendsObject
import retrofit2.Call
import retrofit2.http.*

interface TwitterApiService {

    @GET("trends/place.json")
    fun getTrends(
        @Query("id") woeid: Int
    ): Call<List<TrendsObject>>
}