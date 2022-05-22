package com.yatsenko.core.bean.request

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import org.json.JSONObject

@Parcelize
data class JoinRoomRequest(
    val roomId: String
) : Parcelable {

    fun toJSONObject(): JSONObject {
        return JSONObject().apply {
            put("roomId", roomId)
        }
    }
}
