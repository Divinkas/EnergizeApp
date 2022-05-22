package com.yatsenko.testhelper.ui.auth.model

sealed class AuthState {

    object AuthSuccess : AuthState()
    class AuthError(val message: String?) : AuthState()
}
