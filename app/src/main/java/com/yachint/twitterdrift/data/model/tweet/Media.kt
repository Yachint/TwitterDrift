package com.yachint.twitterdrift.data.model.tweet

import com.yachint.twitterdrift.data.model.BaseModel
import java.io.Serializable

data class Media(
    val type: String,
    val link: String
): BaseModel, Serializable
