package com.yachint.twitterdrift.data.common

import com.yachint.twitterdrift.data.model.BaseModel

interface RepositoryListener {
    fun onSuccess(dataModel: BaseModel)
    fun onFailure(t: Throwable)
}