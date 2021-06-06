package com.yachint.twitterdrift.data.model.response.trends

import com.yachint.twitterdrift.data.model.BaseModel
import java.io.Serializable

data class TrendsMainResponse(
    var status: Long = 200,
    var message: String = "",
    val data : ArrayList<TrendsObject>,
    val errors : Boolean
): BaseModel, Serializable
