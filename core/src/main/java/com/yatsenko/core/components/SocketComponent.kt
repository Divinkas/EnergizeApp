package com.yatsenko.core.components

import com.yatsenko.core.bean.Message
import com.yatsenko.core.bean.User
import com.yatsenko.core.bean.request.*
import com.yatsenko.core.bean.response.*
import com.yatsenko.core.utils.*
import okhttp3.*
import org.json.JSONObject
import timber.log.Timber


class SocketComponent : BaseSocketComponent() {

    private var webSocket: WebSocket? = null

    private val client = OkHttpClient().newBuilder().build()

    private val request: Request = Request.Builder().url(SERVER_API).build()

    var authLiveData = SingleLiveEvent<EnergizeResponse<LoginTokenResponse>>()

    var createChatLiveData = SingleLiveEvent<EnergizeResponse<CreateRoomResponse>>()

    var joinChatLiveData = SingleLiveEvent<EnergizeResponse<JoinRoomResponse>>()

    var sendMessageLiveData = SingleLiveEvent<EnergizeResponse<Message>>()

    var chatUsersLiveData = SingleLiveEvent<EnergizeResponse<List<User>>>()

    var loadMessagesLiveData = SingleLiveEvent<EnergizeResponse<List<Message>>>()

    var createUSerLiveData = SingleLiveEvent<EnergizeResponse<String>>()

    private var token: String = ""

    private val socketListener = SocketListener(
        onConnectedAction = ::connectToSocket,
        parseResponseAction = ::parseResponse
    )

    fun connectToSocket(token: String) {
        this.token = token
        if (!socketListener.isConnected) {
            connectToSocket()
        }
    }

    private fun connectToSocket() {
        try {
            webSocket = client.newWebSocket(request, socketListener)

            if (token.isNotEmpty()) {
                loginByToken(token)
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
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
                    if (isSuccessResponse(metaObject)) {
                        val data = gson.fromJson(dataResponse.getString("data"), LoginTokenResponse::class.java)
                        authLiveData.postValue(EnergizeResponse.Success(data))
                    } else {
                        parseErrorResponse(authLiveData, dataResponse)
                    }
                }
                AUTH_LOGIN_BY_TOKEN_RESPONSE -> {
                    if (isSuccessResponse(metaObject)) {
                        socketListener.isConnected = true
                        parseSuccessResponse(authLiveData, dataResponse, LoginTokenResponse::class.java)
                    } else {
                        parseErrorResponse(authLiveData, dataResponse)
                    }
                }
                AUTH_REGISTRATION_RESPONSE -> {
                    if (isSuccessResponse(metaObject)) {
                        createUSerLiveData.postValue(EnergizeResponse.Success(""))
                    } else {
                        parseErrorResponse(createUSerLiveData, dataResponse)
                    }
                }
                CHAT_CREATE_RESPONSE -> {
                    if (isSuccessResponse(metaObject)) {
                        parseSuccessResponse(createChatLiveData, dataResponse, CreateRoomResponse::class.java)
                    } else {
                        parseErrorResponse(createChatLiveData, dataResponse)
                    }
                }
                CHAT_JOIN_RESPONSE -> {
                    if (isSuccessResponse(metaObject)) {
                        parseSuccessResponse(joinChatLiveData, dataResponse, JoinRoomResponse::class.java)
                    } else {
                        parseErrorResponse(joinChatLiveData, dataResponse)
                    }
                }
                CHAT_GET_MESSAGES_RESPONSE -> {
                    if (isSuccessResponse(metaObject)) {
                        val data = gson.fromJson(dataResponse.getString("data"), ChatMessagesResponse::class.java)
                        loadMessagesLiveData.postValue(EnergizeResponse.Success(data.messages))
                    } else {
                        parseErrorResponse(loadMessagesLiveData, dataResponse)
                    }
                }
                CHAT_SEND_MESSAGE_SELF_RESPONSE,
                CHAT_SEND_MESSAGE_RESPONSE -> {
                    if (isSuccessResponse(metaObject)) {
                        val messageResponse =
                            gson.fromJson(dataResponse.getString("data"), SendMessageResponse::class.java)
                        sendMessageLiveData.postValue(EnergizeResponse.Success(messageResponse.message))
                    } else {
                        parseErrorResponse(sendMessageLiveData, dataResponse)
                    }
                }
                CHAT_GET_USERS_RESPONSE -> {
                    if (isSuccessResponse(metaObject)) {
                        val data = gson.fromJson(dataResponse.getString("data"), GetChatUserResponse::class.java)
                        chatUsersLiveData.postValue(EnergizeResponse.Success(data.users))
                    } else {
                        parseErrorResponse(chatUsersLiveData, dataResponse)
                    }
                }
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun closeAuthSocket() {
        webSocket?.close(1001, "Android: Socket closed.")
    }

    fun createUser(login: String, mail: String, password: String) {
        try {
            webSocket?.send(JSONObject().apply {
                put("event", AUTH_REGISTRATION_REQUEST)
                put("data", CreateUserRequest(login, mail, password, password).toJSONObject())
            }.toString())
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun loginByCredentials(login: String, password: String) {
        try {
            webSocket?.send(JSONObject().apply {
                put("event", AUTH_LOGIN_REQUEST)
                put("data", LoginRequest(login, password).toJSONObject())
            }.toString())
        } catch (ex: Exception) {
            ex.printStackTrace()
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
        try {
            webSocket?.send(JSONObject().apply {
                put("event", CHAT_CREATE_REQUEST)
            }.toString())
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun joinToChat(chatId: String) {
        try {
            webSocket?.send(JSONObject().apply {
                put("event", CHAT_JOIN_REQUEST)
                put("data", JoinRoomRequest(chatId).toJSONObject())
            }.toString())
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun loadMessagesByChatId(chatId: String) {
        try {
            webSocket?.send(JSONObject().apply {
                put("event", CHAT_GET_MESSAGES_REQUEST)
                put("data", ChatMessagesRequest(chatId).toJSONObject())
            }.toString())
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun sendMessage(message: String, chatId: String) {
        try {
            webSocket?.send(JSONObject().apply {
                put("event", CHAT_SEND_MESSAGE_REQUEST)
                put("data", SendMessageRequest(chatId, message).toJSONObject())
            }.toString())
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun loadChatUsers(chatId: String) {
        try {
            webSocket?.send(JSONObject().apply {
                put("event", CHAT_GET_USERS_REQUEST)
                put("data", GetChatUsersRequest(chatId).toJSONObject())
            }.toString())
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
}