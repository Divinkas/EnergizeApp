package com.yatsenko.core.bean.request

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.json.JSONObject

@Parcelize
data class LoginRequest(
    val login: String,
    val pass: String
): Parcelable {

    fun toJSONObject(): JSONObject {
        return JSONObject().apply {
            put("login", login)
            put("pass", pass)
        }
    }
}