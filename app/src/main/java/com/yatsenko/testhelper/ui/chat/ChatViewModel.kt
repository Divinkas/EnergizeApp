package com.yatsenko.testhelper.ui.chat

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.yatsenko.core.bean.Message
import com.yatsenko.core.bean.response.EnergizeResponse
import com.yatsenko.core.utils.log
import com.yatsenko.testhelper.base.BaseViewModel
import com.yatsenko.testhelper.ui.auth.model.AuthState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ChatViewModel : BaseViewModel() {

    private val _userChatsLiveData = MutableLiveData<List<String>>()
    val userChatsLiveData: LiveData<List<String>>
        get() = _userChatsLiveData

    private val _chatMessagesLiveData = MutableLiveData<List<Message>>()
    val chatMessagesLiveData: LiveData<List<Message>>
        get() = _chatMessagesLiveData

    var authLiveData = MutableLiveData<AuthState>()

    fun observeAuthState(owner: LifecycleOwner) {
        coreSdk.getLoginByCredentialLiveData().observe(owner) { response ->
            when (response) {
                is EnergizeResponse.Success -> {
                    if (response.data.token != null) {
                        saveToken(response.data.token)
                    }
                    if (response.data.user != null) {
                        saveUser(response.data.user)
                    }
                    authLiveData.postValue(AuthState.AuthSuccess)
                }
                is EnergizeResponse.Error -> {
                    authLiveData.postValue(AuthState.AuthError(response.meta.message))
                }
            }
        }
    }

    fun observeGetChatMessagesResponse(viewLifecycleOwner: LifecycleOwner) {
        viewModelScope.launch(Dispatchers.Main) {
            coreSdk.getChatMessagesLiveData().observe(viewLifecycleOwner) { messagesResponse ->
                when (messagesResponse) {
                    is EnergizeResponse.Success -> {
                        _chatMessagesLiveData.postValue(messagesResponse.data ?: listOf())
                    }
                    is EnergizeResponse.Error -> {
                        log(messagesResponse.meta.message ?: "[observeChatCreateResponse] - error")
                    }
                }
            }
        }
    }

    fun observeSendMessageResponse(viewLifecycleOwner: LifecycleOwner) {
        viewModelScope.launch(Dispatchers.Main) {
            coreSdk.getSendMessageLiveData().observe(viewLifecycleOwner) { messageResponse ->
                when (messageResponse) {
                    is EnergizeResponse.Success -> {
                        _chatMessagesLiveData.postValue(listOf(messageResponse.data))
                    }
                    is EnergizeResponse.Error -> {
                        log(messageResponse.meta.message ?: "[observeChatCreateResponse] - error")
                    }
                }
            }
        }
    }

    fun observeChatCreateResponse(viewLifecycleOwner: LifecycleOwner) {
        viewModelScope.launch(Dispatchers.Main) {
            coreSdk.getCreateRoomLiveData().observe(viewLifecycleOwner) { createChatResponse ->
                when (createChatResponse) {
                    is EnergizeResponse.Success -> {
                        joinToChat(createChatResponse.data.roomId)
                    }
                    is EnergizeResponse.Error -> {
                        log(createChatResponse.meta.message ?: "[observeChatCreateResponse] - error")
                    }
                }
            }
        }
    }

    fun observeJoinChatResponse(viewLifecycleOwner: LifecycleOwner) {
        viewModelScope.launch(Dispatchers.Main) {
            coreSdk.getJoinRoomLiveData().observe(viewLifecycleOwner) { joinChatResponse ->
                when (joinChatResponse) {
                    is EnergizeResponse.Success -> {
                        appSettings.saveCreatedChat(joinChatResponse.data.roomId)
                        loadChatList()
                    }
                    is EnergizeResponse.Error -> {
                        log(joinChatResponse.meta.message ?: "[observeJoinChatResponse] - error")
                    }
                }
            }
        }
    }

    fun joinToChat(chatId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            coreSdk.joinToChat(chatId)
        }
    }

    fun createChat() {
        viewModelScope.launch(Dispatchers.IO) {
            coreSdk.createChat("")
        }
    }

    fun loadChatList() {
        viewModelScope.launch(Dispatchers.IO) {
            _userChatsLiveData.postValue(appSettings.getSavedChatList())
        }
    }

    fun loadMessagesByChatId(chatId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            coreSdk.loadMessagesByChatId(chatId)
        }
    }

    fun sendMessage(message: String, chatId: String) {
        viewModelScope.launch(Dispatchers.IO) {
            coreSdk.sendMessage(message, chatId)
        }
    }

    fun connectAuthSocket() {
        viewModelScope.launch(Dispatchers.IO) {
            coreSdk.connectToAuthSocket(appSettings.getAuthToken())
        }
    }
}