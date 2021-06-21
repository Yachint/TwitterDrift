package com.yachint.twitterdrift.data.model.response.woeid

import com.yachint.twitterdrift.data.model.BaseModel

data class PlaceModel(
    val name: String,
    val country: String,
    val woeid: Int,
    val countryCode: String
): BaseModel
