package com.yatsenko.testhelper.ui.splash

import android.annotation.SuppressLint
import android.os.Bundle
import com.yatsenko.testhelper.base.BaseActivity
import com.yatsenko.testhelper.ui.auth.AuthViewModel
import com.yatsenko.testhelper.utils.openLoginScreen
import com.yatsenko.testhelper.utils.openMainScreen
import org.koin.androidx.viewmodel.ext.android.viewModel

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity() {

    private val viewModel: AuthViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (viewModel.isAuthorizedUser())
            openMainScreen()
        else
            openLoginScreen()
    }
}