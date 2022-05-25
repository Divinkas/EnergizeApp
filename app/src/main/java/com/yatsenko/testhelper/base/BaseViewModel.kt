package com.yatsenko.testhelper.base

import androidx.lifecycle.ViewModel
import com.yatsenko.core.CoreApi
import com.yatsenko.core.bean.User
import com.yatsenko.core.utils.log
import com.yatsenko.testhelper.local.AppSettings
import org.koin.core.KoinComponent
import org.koin.core.inject

open class BaseViewModel : ViewModel(), KoinComponent {

    protected val appSettings: AppSettings by inject()

    protected val coreSdk: CoreApi by inject()

    protected fun saveToken(token: String?) {
        if (token != null) appSettings.setupAuthToken(token)
        else log("[saveToken] token is null")
    }

    protected fun saveUser(user: User?) {
        if (user != null) appSettings.setupUserData(user)
        else log("[saveUser] user is null")
    }

    fun logOut() {
        appSettings.clear()
    }

    fun getCurrentUserId(): String {
        return appSettings.getUserData()?.id ?: ""
    }
}