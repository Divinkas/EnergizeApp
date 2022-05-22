package com.yatsenko.core

import androidx.lifecycle.LiveData
import com.yatsenko.core.bean.Message
import com.yatsenko.core.bean.response.CreateRoomResponse
import com.yatsenko.core.bean.response.EnergizeResponse
import com.yatsenko.core.bean.response.JoinRoomResponse
import com.yatsenko.core.bean.response.LoginTokenResponse
import org.koin.core.KoinComponent
import org.koin.core.inject

class CoreApi : KoinComponent {

    private val coreSDK: CoreSDK by inject()

    fun registerUser(login: String, mail: String, password: String) {
        coreSDK.createUser(login, mail, password)
    }

    fun connectToAuthSocket() {
        coreSDK.connectToAuthSocket()
    }

    fun closeAuthSocketConnect() {
        coreSDK.closeAuthSocketConnect()
    }

    fun loginByCredentials(login: String, password: String) {
        coreSDK.loginByCredentials(login, password)
    }

    fun loginByToken(token: String) {
        coreSDK.loginByToken(token)
    }

    fun getLoginByCredentialLiveData(): LiveData<EnergizeResponse<LoginTokenResponse>> {
        return coreSDK.getLoginByCredentialLiveData()
    }

    fun getCreateUserLiveData(): LiveData<EnergizeResponse<String>> {
        return coreSDK.getCreateUserLiveData()
    }

    fun connectToChatSocket() {
        coreSDK.connectToChatSocket()
    }

    fun loadMessagesByChatId(chatId: String) {}

    fun createChat(chatName: String) {
        coreSDK.createChat(chatName)
    }

    fun sendMessage(message: String, chatId: String) {}

    fun getCreateRoomLiveData(): LiveData<EnergizeResponse<CreateRoomResponse>> {
        return coreSDK.getCreateRoomLiveData()
    }

    fun getJoinRoomLiveData(): LiveData<EnergizeResponse<JoinRoomResponse>> {
        return coreSDK.getJoinRoomLiveData()
    }

    fun getChatMessagesLiveData(): LiveData<EnergizeResponse<List<Message>>> {
        return coreSDK.getChatMessagesLiveData()
    }

    fun getSendMessageLiveData(): LiveData<EnergizeResponse<Message>> {
        return coreSDK.getSendMessageLiveData()
    }

    fun joinToChat(roomId: String) {
        coreSDK.joinToChat(roomId)
    }
}