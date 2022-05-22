package com.yatsenko.core.bean.request

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.json.JSONObject

@Parcelize
data class ChatMessagesRequest(
    val chatId: String
) : Parcelable {

    fun toJSONObject(): JSONObject {
        return JSONObject().apply {
            put("chatId", chatId)
        }
    }
}