package com.yatsenko.core.bean.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class CreateRoomResponse(
    val roomId: String
) : Parcelable