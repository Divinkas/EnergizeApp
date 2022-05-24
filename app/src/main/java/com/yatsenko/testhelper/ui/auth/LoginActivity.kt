package com.yatsenko.testhelper.ui.auth

import android.os.Bundle
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.textfield.TextInputLayout
import com.yatsenko.testhelper.R
import com.yatsenko.testhelper.base.BaseActivity
import com.yatsenko.testhelper.ui.auth.model.AuthState
import com.yatsenko.testhelper.utils.openMainScreen
import com.yatsenko.testhelper.utils.openRegistrationScreen
import com.yatsenko.testhelper.utils.toast
import kotlinx.android.synthetic.main.activity_login.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class LoginActivity : BaseActivity() {

    private val viewModel: AuthViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        setListeners()
        observeData()

        viewModel.connectAuthSocket()
    }

    private fun setListeners() {
        btnLogin?.setOnClickListener {
            if (isValidCredentials())
                viewModel.loginByCredentials(getLogin(), getPassword())
            else
                showEnteredDataError()
        }

        tvToRegister?.setOnClickListener {
            openRegistrationScreen()
        }
    }

    private fun observeData() {
        viewModel.observeAuthState(this)

        viewModel.authLiveData.observe(this) { state ->
            when (state) {
                is AuthState.AuthSuccess -> {
                    openMainScreen()
                }
                is AuthState.AuthError -> {
                    toast(state.message ?: "")
                }
            }
        }
    }

    private fun showEnteredDataError() {
        if (getLogin().isEmpty()) {
            setupEmptyError(tiLogin, etLogin)
        }

        if (getPassword().isEmpty()) {
            setupEmptyError(tiPassword, etPassword)
        }
    }

    private fun setupEmptyError(inputLayout: TextInputLayout?, editText: EditText?) {
        inputLayout?.error = getString(R.string.error_message_empty_field)

        editText?.doAfterTextChanged {
            inputLayout?.error = null
        }
    }

    private fun isValidCredentials(): Boolean = getLogin().isNotEmpty() && getPassword().isNotEmpty()

    private fun getLogin(): String = etLogin?.text?.toString() ?: ""

    private fun getPassword(): String = etPassword?.text?.toString() ?: ""
}