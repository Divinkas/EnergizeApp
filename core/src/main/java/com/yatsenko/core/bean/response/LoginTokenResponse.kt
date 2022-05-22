package com.yatsenko.core.bean.response

import android.os.Parcelable
import com.yatsenko.core.bean.User
import kotlinx.parcelize.Parcelize

@Parcelize
data class LoginTokenResponse(
    val token: String?,
    val auth: Boolean?,
    val user: User?
) : Parcelable
