package com.yachint.twitterdrift.data.model.response.woeid

import com.yachint.twitterdrift.data.model.BaseModel
import java.io.Serializable

data class PlaceMainResponse(
    var status: Long = 200,
    var message: String = "",
    var data: ArrayList<PlaceModel>,
    val errors : Boolean
): BaseModel, Serializable
