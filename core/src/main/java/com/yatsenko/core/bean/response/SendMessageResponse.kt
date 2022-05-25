package com.yatsenko.core.bean.response

import android.os.Parcelable
import com.yatsenko.core.bean.Message
import kotlinx.parcelize.Parcelize

@Parcelize
data class SendMessageResponse(
    val message: Message
) : Parcelable
