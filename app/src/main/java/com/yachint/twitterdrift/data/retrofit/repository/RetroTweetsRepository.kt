package com.yachint.twitterdrift.data.retrofit.repository

import com.yachint.twitterdrift.data.common.RepositoryListener
import com.yachint.twitterdrift.data.model.response.tweets.TweetsResponse
import com.yachint.twitterdrift.data.repository.TweetsRepository
import com.yachint.twitterdrift.data.retrofit.TwitterApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RetroTweetsRepository: TweetsRepository{
    override fun fetchTrends(term: String, repositoryListener: RepositoryListener) {
        val twitterApiService = TwitterApi.APIService

        twitterApiService?.getTweets(term)?.enqueue(object : Callback<TweetsResponse> {
            override fun onResponse(
                call: Call<TweetsResponse>,
                response: Response<TweetsResponse>
            ) {
                if(response.isSuccessful){
                    response.body()?.let {
                        repositoryListener.onSuccess(it)
                    }
                } else {
                    repositoryListener.onFailure(Exception(response.message()))
                }
            }

            override fun onFailure(call: Call<TweetsResponse>, t: Throwable) {
                repositoryListener.onFailure(t)
            }

        })
    }
}