package com.yachint.twitterdrift.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.yachint.twitterdrift.data.retrofit.repository.RetroTrendsRepository
import com.yachint.twitterdrift.data.websocket.DriftSocketAdapter
import com.yachint.twitterdrift.data.websocket.DriftSocketListener
import com.yachint.twitterdrift.utils.TrendStateMaintainer
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import org.json.JSONObject

class DriftSocketViewModel(
    private val retroTrendsRepository: RetroTrendsRepository,
): ViewModel(), DriftSocketAdapter{

    private val SERVER_PATH = "ws://15.206.79.239:5000/ws/checkUpdate"
    private var webSocket: WebSocket
    private var connected: MutableLiveData<Boolean> = MutableLiveData(false)
    private var isUpdateRequired: MutableLiveData<Boolean> = MutableLiveData(false)
    private var woeid: Int = 0
    private var stateMaintainer = TrendStateMaintainer.getInstance()

    init {
        val client = OkHttpClient()
        val request = Request.Builder().url(SERVER_PATH).build()
        webSocket = client.newWebSocket(request, DriftSocketListener(this))
    }

    fun isUpdateRequired(): LiveData<Boolean> = isUpdateRequired
    fun isConnected(): LiveData<Boolean> = connected

    override fun isConnected(status: Boolean) {
        connected.postValue(status)
    }


    fun askForHash(regionalWoeid: Int){
        woeid = regionalWoeid
        val message = JSONObject()
        message.put("woeid", woeid)
        if(connected.value == true){
            webSocket.send(message.toString())
            Log.d("WEBSOCKET", "askForHash: request sent")
        } else {
            Log.d("WEBSOCKET", "askForHash: not connected")
        }
    }

    override fun receiveHash(regional: String, global: String) {
        val roomRegionalHash = retroTrendsRepository.roomGetHash(woeid)
        val roomGlobalHash = retroTrendsRepository.roomGetHash(1)
        Log.d("WEBSOCKET", "global: $roomGlobalHash -- $global")
        Log.d("WEBSOCKET", "regional: $roomRegionalHash -- $regional")

        if(roomGlobalHash != global) stateMaintainer.setState(
            TrendStateMaintainer.GLOBAL,
            TrendStateMaintainer.OLD
        )

        if(roomRegionalHash != regional) stateMaintainer.setState(
            TrendStateMaintainer.REGIONAL,
            TrendStateMaintainer.OLD
        )

        val decision = roomGlobalHash != global || roomRegionalHash != regional
        Log.d("WEBSOCKET", "receiveHash: update is required -> $decision")
        isUpdateRequired.postValue(decision)
    }
}