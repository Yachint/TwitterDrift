package com.yachint.twitterdrift.data.model.response.trends

import com.yachint.twitterdrift.data.model.BaseModel
import java.io.Serializable

data class TrendsMainResponse(
    val root: ArrayList<TrendsObject>
): BaseModel, Serializable
