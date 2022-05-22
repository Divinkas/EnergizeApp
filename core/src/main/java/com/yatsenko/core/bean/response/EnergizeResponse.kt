package com.yatsenko.core.bean.response

sealed class EnergizeResponse<out T> {

    class Success<T>(val data: T) : EnergizeResponse<T>()
    class Error(val meta: Meta) : EnergizeResponse<Nothing>()
}
