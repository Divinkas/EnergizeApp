package com.yatsenko.core.components

import com.google.gson.Gson
import com.yatsenko.core.bean.Message
import com.yatsenko.core.bean.request.*
import com.yatsenko.core.bean.response.*
import com.yatsenko.core.utils.*
import okhttp3.*
import org.json.JSONObject
import org.koin.core.KoinComponent
import org.koin.core.inject
import timber.log.Timber


class SocketComponent : KoinComponent {

    private val gson: Gson by inject()

    private var webSocket: WebSocket? = null

    private val client = OkHttpClient().newBuilder().build()

    private val request: Request = Request.Builder().url(SERVER_API).build()

    var authLiveData = SingleLiveEvent<EnergizeResponse<LoginTokenResponse>>()

    var createChatLiveData = SingleLiveEvent<EnergizeResponse<CreateRoomResponse>>()

    var joinChatLiveData = SingleLiveEvent<EnergizeResponse<JoinRoomResponse>>()

    var sendMessageLiveData = SingleLiveEvent<EnergizeResponse<Message>>()

    var loadMessagesLiveData = SingleLiveEvent<EnergizeResponse<List<Message>>>()

    var createUSerLiveData = SingleLiveEvent<EnergizeResponse<String>>()

    private var isFirstRequest = true

    private var isConnected = false

    private var token: String = ""

    fun connectToSocket(token: String, listener: WebSocketListener = getWebSocketListener()) {
        if (!isConnected) {
            try {
                this.token = token

                webSocket = client.newWebSocket(request, listener)

                if (token.isNotEmpty()) {
                    loginByToken(token)
                }
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    private fun getWebSocketListener(onConnectedAction: () -> Unit = {}): WebSocketListener {
        return object : WebSocketListener() {

            override fun onOpen(webSocket: WebSocket, response: Response) {
                super.onOpen(webSocket, response)
                isConnected = true
                onConnectedAction.invoke()
                log("[webSocket] isConnected = true")
            }

            override fun onMessage(webSocket: WebSocket, text: String) {
                super.onMessage(webSocket, text)
                parseResponse(text)
            }

            override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                super.onFailure(webSocket, t, response)
                t.printStackTrace()
                log("[webSocket] isConnected = false ${response?.message}")
                isConnected = false
            }

            override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosing(webSocket, code, reason)
                log("[webSocket] isConnected = false $reason")
                isConnected = false
            }

            override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
                super.onClosed(webSocket, code, reason)
                log("[webSocket] onClosed / isConnected = false $reason")
                isConnected = false
            }
        }
    }

    private fun parseResponse(response: String) {
        try {
            val jsonResponse = JSONObject(response)
            Timber.i(jsonResponse.toString())

            val dataResponse = jsonResponse.getJSONObject("data")
            val metaObject = dataResponse.getJSONObject("meta")

            when (jsonResponse.getString("event")) {
                AUTH_LOGIN_RESPONSE -> {
                    if (metaObject.isNotEmpty() && metaObject.getString("status") == "200") {
                        val data = gson.fromJson(dataResponse.getString("data"), LoginTokenResponse::class.java)
                        authLiveData.postValue(EnergizeResponse.Success(data))
                    } else {
                        val meta = gson.fromJson(dataResponse.getString("meta"), Meta::class.java)
                        authLiveData.postValue(EnergizeResponse.Error(meta))
                    }
                }
                AUTH_LOGIN_BY_TOKEN_RESPONSE -> {
                    if (metaObject.isNotEmpty() && metaObject.getString("status") == "200") {
                        val data = gson.fromJson(dataResponse.getString("data"), LoginTokenResponse::class.java)
                        authLiveData.postValue(EnergizeResponse.Success(data))
                    } else {
                        val meta = gson.fromJson(dataResponse.getString("meta"), Meta::class.java)
                        authLiveData.postValue(EnergizeResponse.Error(meta))
                    }
                }
                AUTH_REGISTRATION_RESPONSE -> {
                    if (metaObject.isNotEmpty() && metaObject.getString("status") == "200") {
                        createUSerLiveData.postValue(EnergizeResponse.Success(""))
                    } else {
                        val meta = gson.fromJson(dataResponse.getString("meta"), Meta::class.java)
                        createUSerLiveData.postValue(EnergizeResponse.Error(meta))
                    }
                }
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

    private fun sendEvent(action: () -> Unit = {}) {
        if (!isConnected && token.isNotEmpty()) {
            connectToSocket(token, getWebSocketListener {
                action.invoke()
            })
        } else {
            action.invoke()
        }
    }

    fun closeAuthSocket() {
        webSocket?.close(1001, "Android: User exited.")
    }

    fun createUser(login: String, mail: String, password: String) {
        sendEvent {
            try {
                webSocket?.send(JSONObject().apply {
                    put("event", AUTH_REGISTRATION_REQUEST)
                    put("data", CreateUserRequest(login, mail, password, password).toJSONObject())
                }.toString())
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    fun loginByCredentials(login: String, password: String) {
        sendEvent {
            try {
                webSocket?.send(JSONObject().apply {
                    put("event", AUTH_LOGIN_REQUEST)
                    put("data", LoginRequest(login, password).toJSONObject())
                }.toString())
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    fun loginByToken(token: String) {
        try {
            webSocket?.send(JSONObject().apply {
                put("event", AUTH_LOGIN_BY_TOKEN_REQUEST)
                put("data", TokenRequest(token).toJSONObject())
            }.toString())
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun createChat() {
        sendEvent {
            try {
                webSocket?.send(JSONObject().apply {
                    put("event", CHAT_CREATE_REQUEST)
                }.toString())
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    fun joinToChat(chatId: String) {
        sendEvent {
            try {
                webSocket?.send(JSONObject().apply {
                    put("event", CHAT_JOIN_REQUEST)
                    put("data", JoinRoomRequest(chatId).toJSONObject())
                }.toString())
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    fun loadMessagesByChatId(chatId: String) {
        sendEvent {
            try {
                webSocket?.send(JSONObject().apply {
                    put("event", CHAT_GET_MESSAGES_REQUEST)
                    put("data", ChatMessagesRequest(chatId).toJSONObject())
                }.toString())
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

    fun sendMessage(message: String, chatId: String) {
        sendEvent {
            try {
                webSocket?.send(JSONObject().apply {
                    put("event", CHAT_SEND_MESSAGE_REQUEST)
                    put("data", SendMessageRequest(message, chatId).toJSONObject())
                }.toString())
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }
}