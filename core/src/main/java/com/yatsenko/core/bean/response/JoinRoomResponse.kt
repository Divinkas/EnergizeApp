package com.yatsenko.core.bean.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class JoinRoomResponse(
    val roomId: String,
    val userId: String
) : Parcelable