package com.yatsenko.core

import androidx.lifecycle.LiveData
import com.yatsenko.core.bean.Message
import com.yatsenko.core.bean.User
import com.yatsenko.core.bean.response.CreateRoomResponse
import com.yatsenko.core.bean.response.EnergizeResponse
import com.yatsenko.core.bean.response.JoinRoomResponse
import com.yatsenko.core.bean.response.LoginTokenResponse
import com.yatsenko.core.components.SocketComponent
import com.yatsenko.core.components.ChatComponent
import org.koin.core.KoinComponent

class CoreSDK(
    private val socketComponent: SocketComponent,
    private val chatComponent: ChatComponent
) : KoinComponent {

    fun createUser(login: String, mail: String, password: String) {
        socketComponent.createUser(login, mail, password)
    }

    fun loginByCredentials(login: String, password: String) {
        socketComponent.loginByCredentials(login, password)
    }

    fun getCreateUserLiveData(): LiveData<EnergizeResponse<String>> {
        return socketComponent.createUSerLiveData
    }

    fun getLoginByCredentialLiveData(): LiveData<EnergizeResponse<LoginTokenResponse>> {
        return socketComponent.authLiveData
    }

    fun connectToAuthSocket(token: String) {
        socketComponent.connectToSocket(token)
    }

    fun closeAuthSocketConnect() {
        socketComponent.closeAuthSocket()
    }

    fun loginByToken(token: String) {
        socketComponent.loginByToken(token)
    }

    fun getCreateRoomLiveData(): LiveData<EnergizeResponse<CreateRoomResponse>> {
        return socketComponent.createChatLiveData
    }

    fun getJoinRoomLiveData(): LiveData<EnergizeResponse<JoinRoomResponse>> {
        return socketComponent.joinChatLiveData
    }

    fun getChatMessagesLiveData(): LiveData<EnergizeResponse<List<Message>>> {
        return socketComponent.loadMessagesLiveData
    }

    fun getSendMessageLiveData(): LiveData<EnergizeResponse<Message>> {
        return socketComponent.sendMessageLiveData
    }

    fun getChatUsers(): LiveData<EnergizeResponse<List<User>>> {
        return socketComponent.chatUsersLiveData
    }

    fun loadMessagesByChatId(chatId: String) {
        socketComponent.loadMessagesByChatId(chatId)
    }

    fun createChat(chatName: String) {
        socketComponent.createChat()
    }

    fun sendMessage(message: String, chatId: String) {
        socketComponent.sendMessage(message, chatId)
    }

    fun connectToChatSocket() {
        chatComponent.connectToChatWebSocket()
    }

    fun joinToChat(roomId: String) {
        socketComponent.joinToChat(chatId = roomId)
    }

    fun getChatUsers(chatId: String) {
        socketComponent.loadChatUsers(chatId)
    }
}