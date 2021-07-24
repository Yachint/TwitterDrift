package com.yachint.twitterdrift.data.model.response.trends

import com.yachint.twitterdrift.data.model.BaseModel
import com.yachint.twitterdrift.data.model.trends.Trend
import java.io.Serializable

data class TrendsMainResponse(
    var status: Long = 200,
    var message: String = "",
    var hash: String = "",
    var data: ArrayList<Trend>,
    val errors : Boolean
): BaseModel, Serializable
