package com.yatsenko.core.bean.request

import org.json.JSONObject

data class LoginTokenRequest(
    val token: String
) {

    fun toJSONObject(): JSONObject {
        return JSONObject().apply {
            put("token", token)
        }
    }
}