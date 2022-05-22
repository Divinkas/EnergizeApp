package com.yatsenko.core.bean.request

import android.os.Parcelable
import com.yatsenko.core.bean.Message
import kotlinx.parcelize.Parcelize

@Parcelize
data class ChatMessagesResponse(
    val messages: List<Message>
) : Parcelable
