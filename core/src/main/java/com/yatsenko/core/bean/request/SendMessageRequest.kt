package com.yatsenko.core.bean.request

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.json.JSONObject

@Parcelize
data class SendMessageRequest(
    val chatId: String,
    val message: String
) : Parcelable {

    fun toJSONObject(): JSONObject {
        return JSONObject().apply {
            put("chatId", chatId)
            put("message", message)
        }
    }
}
