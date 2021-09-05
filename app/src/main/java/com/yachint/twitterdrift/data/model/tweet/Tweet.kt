package com.yachint.twitterdrift.data.model.tweet

import com.yachint.twitterdrift.data.model.BaseModel
import java.io.Serializable

data class Tweet(
    val createdAt: String,
    val id: String,
    val text: String,
    val retweets: Long,
    val likes: Long,
    val link: String,
    val media: Media,
    val user: User
): BaseModel, Serializable