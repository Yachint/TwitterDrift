package com.yachint.twitterdrift.data.model.response.tweets

import com.yachint.twitterdrift.data.model.BaseModel
import com.yachint.twitterdrift.data.model.tweet.Tweet
import java.io.Serializable

data class TweetsResponse(
    var status: Long = 200,
    var message: String = "",
    var data: ArrayList<Tweet>,
    val errors : Boolean
): BaseModel, Serializable
