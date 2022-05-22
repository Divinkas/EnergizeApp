package com.yatsenko.core

import androidx.lifecycle.LiveData
import com.yatsenko.core.bean.Message
import com.yatsenko.core.bean.response.CreateRoomResponse
import com.yatsenko.core.bean.response.EnergizeResponse
import com.yatsenko.core.bean.response.JoinRoomResponse
import com.yatsenko.core.bean.response.LoginTokenResponse
import com.yatsenko.core.components.AuthComponent
import com.yatsenko.core.components.ChatComponent
import org.koin.core.KoinComponent

class CoreSDK(
    private val authComponent: AuthComponent,
    private val chatComponent: ChatComponent
) : KoinComponent {

    fun createUser(login: String, mail: String, password: String) {
        authComponent.createUser(login, mail, password)
    }

    fun loginByCredentials(login: String, password: String) {
        authComponent.loginByCredentials(login, password)
    }

    fun getCreateUserLiveData(): LiveData<EnergizeResponse<String>> {
        return authComponent.createUSerLiveData
    }

    fun getLoginByCredentialLiveData(): LiveData<EnergizeResponse<LoginTokenResponse>> {
        return authComponent.authLiveData
    }

    fun connectToAuthSocket() {
        authComponent.connectToAuthSocket()
    }

    fun closeAuthSocketConnect() {
        authComponent.closeAuthSocket()
    }

    fun loginByToken(token: String) {
        authComponent.loginByToken(token)
    }

    fun getCreateRoomLiveData(): LiveData<EnergizeResponse<CreateRoomResponse>> {
        return chatComponent.createChatLiveData
    }

    fun getJoinRoomLiveData(): LiveData<EnergizeResponse<JoinRoomResponse>> {
        return chatComponent.joinChatLiveData
    }

    fun getChatMessagesLiveData(): LiveData<EnergizeResponse<List<Message>>> {
        return chatComponent.loadMessagesLiveData
    }

    fun getSendMessageLiveData(): LiveData<EnergizeResponse<Message>> {
        return chatComponent.sendMessageLiveData
    }

    fun loadMessagesByChatId(chatId: String) {
        chatComponent.loadMessagesByChatId(chatId)
    }

    fun createChat(chatName: String) {
        chatComponent.createChat()
    }

    fun sendMessage(message: String, chatId: String) {
        chatComponent.sendMessage(message, chatId)
    }

    fun connectToChatSocket() {
        chatComponent.connectToChatWebSocket()
    }

    fun joinToChat(roomId: String) {
        chatComponent.joinToChat(chatId = roomId)
    }
}