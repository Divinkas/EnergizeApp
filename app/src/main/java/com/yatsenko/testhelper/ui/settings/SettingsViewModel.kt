package com.yatsenko.testhelper.ui.settings

import com.yatsenko.testhelper.base.BaseViewModel

class SettingsViewModel : BaseViewModel() {

    fun getUserName(): String {
        return appSettings.getUserData()?.name ?: ""
    }
}