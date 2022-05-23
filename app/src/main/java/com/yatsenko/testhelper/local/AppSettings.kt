package com.yatsenko.testhelper.local

import android.content.Context
import com.google.gson.Gson
import com.yatsenko.core.bean.User

class AppSettings(context: Context) {

    private val preferences = Preferences(context)

    fun setupAuthToken(token: String) {
        preferences.putString(KEY_AUTH_TOKEN, token)
    }

    fun getAuthToken(): String {
        return preferences.getString(KEY_AUTH_TOKEN, "") ?: ""
    }

    fun setupUserData(user: User) {
        val userStr = Gson().toJson(user)
        preferences.putString(KEY_USER_DATA, userStr)
    }

    fun getUserData(): User? {
        val userStr = preferences.getString(KEY_USER_DATA, null)
        return if (userStr != null) Gson().fromJson(userStr, User::class.java) else null
    }

    fun clear() {
        preferences.wipe()
    }

    fun saveCreatedChat(chatId: String) {
        val savedChatList = preferences.getStringSet( KEY_USER_CHATS, setOf())?.toMutableSet() ?: mutableSetOf()
        savedChatList.add(chatId)
        preferences.putStringSet(KEY_USER_CHATS, savedChatList)
    }

    fun getSavedChatList(): List<String> {
        return preferences.getStringSet( KEY_USER_CHATS, setOf())?.toList() ?: listOf()
    }

    companion object {

        const val KEY_AUTH_TOKEN = "key_application_auth_token"
        const val KEY_USER_DATA = "key_user_data"
        const val KEY_USER_CHATS = "key_user_chats"
    }
}