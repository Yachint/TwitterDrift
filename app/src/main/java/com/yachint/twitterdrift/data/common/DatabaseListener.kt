package com.yachint.twitterdrift.data.common

interface DatabaseListener {
    fun onQueryComplete(type: String)
}