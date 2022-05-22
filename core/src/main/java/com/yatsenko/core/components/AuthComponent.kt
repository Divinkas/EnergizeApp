package com.yatsenko.core.components

import com.google.gson.Gson
import com.yatsenko.core.bean.request.CreateUserRequest
import com.yatsenko.core.bean.request.LoginRequest
import com.yatsenko.core.bean.request.TokenRequest
import com.yatsenko.core.bean.response.EnergizeResponse
import com.yatsenko.core.bean.response.LoginTokenResponse
import com.yatsenko.core.bean.response.Meta
import com.yatsenko.core.utils.*
import okhttp3.*
import org.json.JSONObject
import org.koin.core.KoinComponent
import org.koin.core.inject
import timber.log.Timber


class AuthComponent : KoinComponent {

    private val gson: Gson by inject()

    private var authSocket: WebSocket? = null

    var authLiveData = SingleLiveEvent<EnergizeResponse<LoginTokenResponse>>()

    var createUSerLiveData = SingleLiveEvent<EnergizeResponse<String>>()

    fun connectToAuthSocket() {
        try {
            val client = OkHttpClient().newBuilder().build()
            val request: Request = Request.Builder().url(SERVER_API).build()

            authSocket = client.newWebSocket(request, object : WebSocketListener() {
                override fun onMessage(webSocket: WebSocket, text: String) {
                    super.onMessage(webSocket, text)
                    parseResponse(text)
                }

                override fun onFailure(webSocket: WebSocket, t: Throwable, response: Response?) {
                    super.onFailure(webSocket, t, response)
                    t.printStackTrace()
                }
            })
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
            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun closeAuthSocket() {
        authSocket?.close(1001, "Android: User exited.")
    }

    fun createUser(login: String, mail: String, password: String) {
        try {
            authSocket?.send(JSONObject().apply {
                put("event", AUTH_REGISTRATION_REQUEST)
                put("data", CreateUserRequest(login, mail, password, password).toJSONObject())
            }.toString())
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun loginByCredentials(login: String, password: String) {
        try {
            authSocket?.send(JSONObject().apply {
                put("event", AUTH_LOGIN_REQUEST)
                put("data", LoginRequest(login, password).toJSONObject())
            }.toString())
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    fun loginByToken(token: String) {
        try {
            authSocket?.send(JSONObject().apply {
                put("event", AUTH_LOGIN_BY_TOKEN_REQUEST)
                put("data", TokenRequest(token).toJSONObject())
            }.toString())
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }
}