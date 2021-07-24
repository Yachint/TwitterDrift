package com.yachint.twitterdrift.data.retrofit

object TwitterApi {
    private const val SERVER_URL = "http://15.206.79.239:5000/api/v1/"
    val APIService: TwitterApiService?
        get() = RetrofitClient
            .getClient(SERVER_URL)
            ?.create(TwitterApiService::class.java)

    fun getHeaderAPIService(token: String?): TwitterApiService? {
        return RetrofitClient
            .getClientWithHeader(SERVER_URL, token!!)
            ?.create(TwitterApiService::class.java)
    }
}