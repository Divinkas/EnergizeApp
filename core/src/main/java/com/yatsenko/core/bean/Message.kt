package com.yatsenko.core.bean

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Message(
    val chatId: String,
    val userId: String,
    val message: String,
    val timestamp: Long,
) : Parcelable