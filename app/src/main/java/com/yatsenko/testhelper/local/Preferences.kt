package com.yatsenko.testhelper.local

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson

class Preferences(private val context: Context) {

    internal fun getLong(key: String, defaultValue: Long): Long? {
        return getDefaultPreferences(context).getLong(key, defaultValue)
    }

    internal fun putLong(key: String, value: Long) {
        getDefaultPreferences(context).edit().putLong(key, value).apply()
    }

    internal fun getBoolean(key: String, defaultValue: Boolean): Boolean {
        return getDefaultPreferences(context).getBoolean(key, defaultValue)
    }

    internal fun putBoolean(key: String, value: Boolean) {
        getDefaultPreferences(context).edit().putBoolean(key, value).apply()
    }

    internal fun getString(key: String, defaultValue: String?): String? {
        return getDefaultPreferences(context).getString(key, defaultValue)
    }

    internal fun putString(key: String, value: String?) {
        getDefaultPreferences(context).edit().putString(key, value).apply()
    }

    internal fun getStringSet(key: String, defaultValue: Set<String>): Set<String>? {
        return getDefaultPreferences(context).getStringSet(key, defaultValue)
    }

    internal fun putStringSet(key: String, value: Set<String>) {
        getDefaultPreferences(context).edit().putStringSet(key, value).apply()
    }

    internal fun getStringArray(key: String, defaultValue: Array<String>): Array<String> {
        val jsonString = getDefaultPreferences(context)
            .getString(key, "[]")
        return Gson().fromJson(jsonString, Array<String>::class.java) ?: defaultValue
    }

    internal fun putStringArray(key: String, value: Array<String>) {
        getDefaultPreferences(context).edit().putString(key, Gson().toJson(value)).apply()
    }

    internal fun getInt(key: String, defaultValue: Int): Int {
        return getDefaultPreferences(context).getInt(key, defaultValue)
    }

    internal fun putInt(key: String, value: Int) {
        getDefaultPreferences(context).edit().putInt(key, value).apply()
    }

    internal fun wipe() {
        getDefaultPreferences(context).edit().clear().apply()
    }

    companion object {

        private const val PREFS = "instance_prefs"

        private fun getDefaultPreferences(context: Context): SharedPreferences {
            return context.getSharedPreferences(PREFS, Context.MODE_PRIVATE)
        }
    }
}
