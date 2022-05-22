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

    var createChatLiveData = SingleLiveEvent<EnergizeResponse<CreateRoomResponse>>()

    var joinChatLiveData = SingleLiveEvent<EnergizeResponse<JoinRoomResponse>>()

    var sendMessageLiveData = SingleLiveEvent<EnergizeResponse<Message>>()

    var loadMessagesLiveData = SingleLiveEvent<EnergizeResponse<List<Message>>>()

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
                CHAT_CREATE_RESPONSE -> {
                    if (metaObject.isNotEmpty() && metaObject.getString("status") == "200") {
                        val data = gson.fromJson(dataResponse.getString("data"), CreateRoomResponse::class.java)
                        createChatLiveData.postValue(EnergizeResponse.Success(data))
                    } else {
                        val meta = gson.fromJson(dataResponse.getString("meta"), Meta::class.java)
                        createChatLiveData.postValue(EnergizeResponse.Error(meta))
                    }
                }
                CHAT_JOIN_RESPONSE -> {
                    if (metaObject.isNotEmpty() && metaObject.getString("status") == "200") {
                        val data = gson.fromJson(dataResponse.getString("data"), JoinRoomResponse::class.java)
                        joinChatLiveData.postValue(EnergizeResponse.Success(data))
                    } else {
                        val meta = gson.fromJson(dataResponse.getString("meta"), Meta::class.java)
                        joinChatLiveData.postValue(EnergizeResponse.Error(meta))
                    }
                }
                CHAT_GET_MESSAGES_RESPONSE -> {
                    if (metaObject.isNotEmpty() && metaObject.getString("status") == "200") {
                        val data = gson.fromJson(dataResponse.getString("data"), ChatMessagesResponse::class.java)
                        loadMessagesLiveData.postValue(EnergizeResponse.Success(data.messages))
                    } else {
                        val meta = gson.fromJson(dataResponse.getString("meta"), Meta::class.java)
                        loadMessagesLiveData.postValue(EnergizeResponse.Error(meta))
                    }
                }
                CHAT_SEND_MESSAGE_RESPONSE -> {
                    if (metaObject.isNotEmpty() && metaObject.getString("status") == "200") {
                        val data = gson.fromJson(dataResponse.getString("data"), Message::class.java)
                        sendMessageLiveData.postValue(EnergizeResponse.Success(data))
                    } else {
                        val meta = gson.fromJson(dataResponse.getString("meta"), Meta::class.java)
                        sendMessageLiveData.postValue(EnergizeResponse.Error(meta))
                    }
                }
                CHAT_GET_USERS_RESPONSE -> {
                    // todo implement in future
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun createChat() {
        try {
            chatWebSocket?.send(JSONObject().apply {
                put("event", CHAT_CREATE_REQUEST)
            }.toString())
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun joinToChat(chatId: String) {
        try {
            chatWebSocket?.send(JSONObject().apply {
                put("event", CHAT_JOIN_REQUEST)
                put("data", JoinRoomRequest(chatId).toJSONObject())
            }.toString())
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun loadMessagesByChatId(chatId: String) {
        try {
            chatWebSocket?.send(JSONObject().apply {
                put("event", CHAT_GET_MESSAGES_REQUEST)
                put("data", ChatMessagesRequest(chatId).toJSONObject())
            }.toString())
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun sendMessage(message: String, chatId: String) {
        try {
            chatWebSocket?.send(JSONObject().apply {
                put("event", CHAT_SEND_MESSAGE_REQUEST)
                put("data", SendMessageRequest(message, chatId).toJSONObject())
            }.toString())
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun disconnectFromChatSocket() {
        chatWebSocket?.cancel()
    }
}