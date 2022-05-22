package com.yatsenko.testhelper.utils

import android.app.Activity
import android.content.Intent
import com.yatsenko.testhelper.ui.auth.LoginActivity
import com.yatsenko.testhelper.ui.auth.registration.RegistrationActivity
import com.yatsenko.testhelper.ui.chat.MainActivity

fun Activity.openMainScreen() {
    val intent = Intent(this, MainActivity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    startActivity(intent)
}

fun Activity.openLoginScreen() {
    val intent = Intent(this, LoginActivity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    startActivity(intent)
}

fun Activity.openRegistrationScreen() {
    val intent = Intent(this, RegistrationActivity::class.java)
    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
    startActivity(intent)
}