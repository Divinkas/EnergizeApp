package com.yatsenko.testhelper.ui.auth

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.yatsenko.core.bean.response.EnergizeResponse
import com.yatsenko.testhelper.base.BaseViewModel
import com.yatsenko.testhelper.ui.auth.model.AuthState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AuthViewModel : BaseViewModel() {

    var authLiveData = MutableLiveData<AuthState>()

    fun connectAuthSocket() {
        viewModelScope.launch(Dispatchers.IO) {
            coreSdk.connectToAuthSocket(appSettings.getAuthToken())
        }
    }

    fun observeAuthState(owner: LifecycleOwner) {
        coreSdk.getLoginByCredentialLiveData().observe(owner) { response ->
            when (response) {
                is EnergizeResponse.Success -> {
                    saveToken(response.data.token)
                    saveUser(response.data.user)
                    authLiveData.postValue(AuthState.AuthSuccess)
                }
                is EnergizeResponse.Error -> {
                    authLiveData.postValue(AuthState.AuthError(response.meta.message))
                }
            }
        }
    }

    fun observeCreateUserState(owner: LifecycleOwner) {
        coreSdk.getCreateUserLiveData().observe(owner) { response ->
            when (response) {
                is EnergizeResponse.Success -> {
                    authLiveData.postValue(AuthState.AuthSuccess)
                }
                is EnergizeResponse.Error -> {
                    authLiveData.postValue(AuthState.AuthError(response.meta.message))
                }
            }
        }
    }

    fun loginByCredentials(login: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            coreSdk.loginByCredentials(login, password)
        }
    }

    private fun loginByToken() {
        viewModelScope.launch(Dispatchers.IO) {
            // coreSdk.loginByToken(appSettings.authToken)
        }
    }

    fun isAuthorizedUser(): Boolean {
        return appSettings.getAuthToken().isNotEmpty()
    }

    fun closeSocketConnect() {
        coreSdk.closeAuthSocketConnect()
    }

    fun registerUser(login: String, mail: String, password: String) {
        viewModelScope.launch(Dispatchers.IO) {
            coreSdk.registerUser(login, mail, password)
        }
    }
}