package com.yatsenko.core.bean.response

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Meta(
    val status: String?,
    val message: String?
) : Parcelable {

    fun isAuthorizeError(): Boolean {
        return message == "Unauthorized"
    }
}
