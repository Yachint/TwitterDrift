package com.yachint.twitterdrift.data.model.response.trends

import com.yachint.twitterdrift.data.model.BaseModel
import com.yachint.twitterdrift.data.model.trends.Trend
import java.io.Serializable

data class TrendsObject(
    val trends: ArrayList<Trend>
): BaseModel, Serializable
