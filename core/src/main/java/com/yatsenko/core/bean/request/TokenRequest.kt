package com.yatsenko.core.bean.request

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.json.JSONObject

@Parcelize
data class TokenRequest(
    val token: String
): Parcelable {

    fun toJSONObject(): JSONObject {
        return JSONObject().apply {
            put("token", token)
        }
    }
}