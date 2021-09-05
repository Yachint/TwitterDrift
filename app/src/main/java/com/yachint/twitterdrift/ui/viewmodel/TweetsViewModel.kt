package com.yachint.twitterdrift.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yachint.twitterdrift.data.common.RepositoryListener
import com.yachint.twitterdrift.data.model.BaseModel
import com.yachint.twitterdrift.data.model.response.tweets.TweetsResponse
import com.yachint.twitterdrift.data.model.tweet.Tweet
import com.yachint.twitterdrift.data.retrofit.repository.RetroTweetsRepository

class TweetsViewModel(
    private val retroTweetsRepository: RetroTweetsRepository
): ViewModel(), RepositoryListener {
    private var tweets: MutableLiveData<List<Tweet>> = MutableLiveData()
    private var error: MutableLiveData<Boolean> = MutableLiveData(false)
    private var topTweet: MutableLiveData<Tweet> = MutableLiveData()
    private var tweetReady: MutableLiveData<String> = MutableLiveData("null")
    var term: String = "null"

    fun tweetList(): LiveData<List<Tweet>> = tweets
    fun readyStatus(): LiveData<String> = tweetReady
    fun topTweet(): LiveData<Tweet> = topTweet

    fun fetchTweets(searchTerm: String){
        term = searchTerm
        retroTweetsRepository.fetchTrends(term, this)
    }

    fun resetStatus(){
        tweetReady.value = "null"
    }

    override fun onSuccess(dataModel: BaseModel) {
        when(dataModel){
            is TweetsResponse -> {
                tweets.value = dataModel.data
                topTweet.value = dataModel.data[0]
                tweetReady.value = term
            }
        }
    }

    override fun onFailure(t: Throwable) {
        error.value = true
    }
}