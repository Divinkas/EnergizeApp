package com.yatsenko.core.bean.request

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.json.JSONObject

@Parcelize
data class CreateUserRequest(
    val login: String,
    val email: String,
    val pass: String,
    val repass: String
) : Parcelable {

    fun toJSONObject(): JSONObject {
        return JSONObject().apply {
            put("login", login)
            put("email", email)
            put("pass", pass)
            put("repass", repass)
        }
    }
}