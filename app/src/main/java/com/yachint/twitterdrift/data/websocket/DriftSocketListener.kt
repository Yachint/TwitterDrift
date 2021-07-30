package com.yachint.twitterdrift.data.websocket

import android.util.Log
import okhttp3.Response
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import org.json.JSONObject

class DriftSocketListener(
    private val adapter: DriftSocketAdapter
): WebSocketListener() {

    override fun onMessage(webSocket: WebSocket, text: String) {
        super.onMessage(webSocket, text)
        val jsonObj = JSONObject(text)
        adapter.receiveHash(
            jsonObj.getString("regional"),
            jsonObj.getString("global")
        )
    }

    override fun onOpen(webSocket: WebSocket, response: Response) {
        super.onOpen(webSocket, response)
        Log.d("WEBSOCKET", "onOpen: connected!")
        adapter.isConnected(true)
    }

    override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosed(webSocket, code, reason)
        Log.d("WEBSOCKET", "onClosed: CLOSED")
        adapter.isConnected(false)
    }

    override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
        super.onClosing(webSocket, code, reason)
        Log.d("WEBSOCKET", "onClosing: closing")
        adapter.isConnected(false)
    }

    override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
        super.onFailure(webSocket, t, response)
        Log.d("WEBSOCKET", "onFailure: failed : ${t.localizedMessage}")
        adapter.isConnected(false)
    }
}