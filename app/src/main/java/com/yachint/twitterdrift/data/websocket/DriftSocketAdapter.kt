package com.yachint.twitterdrift.data.websocket

interface DriftSocketAdapter {
    fun isConnected(status: Boolean)
    fun receiveHash(regional: String, global: String)
}