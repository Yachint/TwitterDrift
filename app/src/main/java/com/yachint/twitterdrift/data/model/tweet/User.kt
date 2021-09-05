package com.yachint.twitterdrift.data.model.tweet

import com.yachint.twitterdrift.data.model.BaseModel
import java.io.Serializable

data class User(
    val userId: String,
    val name: String,
    val username: String,
    val location: String,
    val followersCount: Long,
    val followingCount: Long,
    val verified: Boolean,
    val profileImage: String,
    val profileBannerImage: String
): BaseModel, Serializable