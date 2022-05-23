package com.yatsenko.core.components

import com.google.gson.Gson
import com.yatsenko.core.bean.Message
import com.yatsenko.core.bean.request.*
import com.yatsenko.core.bean.response.CreateRoomResponse
import com.yatsenko.core.bean.response.EnergizeResponse
import com.yatsenko.core.bean.response.JoinRoomResponse
import com.yatsenko.core.bean.response.Meta
import com.yatsenko.core.utils.*
import okhttp3.*
import org.json.JSONObject
import org.koin.core.KoinComponent
import org.koin.core.inject
import timber.log.Timber

class ChatComponent : KoinComponent {

    private val gson: Gson by inject()

    private var chatWebSocket: WebSocket? = null



    fun connectToChatWebSocket() {
        val client = OkHttpClient().newBuilder().build()
        val request: Request = Request.Builder().url(SERVER_API).build()

        chatWebSocket = client.newWebSocket(request, object : WebSocketListener() {
            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
                parseResponse(text)
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                super.onFailure(webSocket, t, response)
                t.printStackTrace()
            }
        })
    }

    private fun parseResponse(response: String) {
        try {
            val jsonResponse = JSONObject(response)
            Timber.i(jsonResponse.toString())

            val dataResponse = jsonResponse.getJSONObject("data")
            val metaObject = dataResponse.getJSONObject("meta")

            when (jsonResponse.getString("event")) {

            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }



    fun disconnectFromChatSocket() {
        chatWebSocket?.cancel()
    }
}