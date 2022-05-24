package com.yatsenko.testhelper.ui.auth.registration

import android.os.Bundle
import android.widget.EditText
import androidx.core.widget.doAfterTextChanged
import com.google.android.material.textfield.TextInputLayout
import com.yatsenko.testhelper.R
import com.yatsenko.testhelper.base.BaseActivity
import com.yatsenko.testhelper.ui.auth.AuthViewModel
import com.yatsenko.testhelper.ui.auth.model.AuthState
import com.yatsenko.testhelper.utils.openLoginScreen
import com.yatsenko.testhelper.utils.openMainScreen
import com.yatsenko.testhelper.utils.toast
import kotlinx.android.synthetic.main.activity_registration.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegistrationActivity : BaseActivity() {

    private val viewModel: AuthViewModel by viewModel()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)

        setupListeners()
        observeData()

        viewModel.connectAuthSocket()

        // setup mock data for test
        // setupDefaultData()
    }

    private fun setupDefaultData() {
        etRegLogin?.setText("andrew")
        etRegMail?.setText("divinkas@gmail.com")
        etRegPassword?.setText("qwerty")
        etRegPassword2?.setText("qwerty")
    }

    private fun setupListeners() {
        btnRegister?.setOnClickListener {
            if (isValidEnteredData()) {
                viewModel.registerUser(login = getLogin(), mail = getMail(), password = getPassword())
            } else {
                showEnterErrors()
            }
        }

        tvToLogin?.setOnClickListener {
            openLoginScreen()
        }
    }

    private fun observeData() {
        viewModel.observeCreateUserState(this)

        viewModel.authLiveData.observe(this) { state ->
            when (state) {
                is AuthState.AuthSuccess -> {
                    openLoginScreen()
                }
                is AuthState.AuthError -> {
                    toast(state.message ?: "")
                }
            }
        }
    }

    private fun isValidEnteredData(): Boolean {
        return getLogin().isNotEmpty() &&
            getMail().isNotEmpty() &&
            getPassword().isNotEmpty() &&
            getPassword2().isNotEmpty() &&
            getPassword() == getPassword2()
    }

    private fun showEnterErrors() {
        if (getLogin().isEmpty()) {
            setupEmptyError(tiRegLogin, etRegLogin)
        }

        if (getMail().isEmpty()) {
            setupEmptyError(tiRegMail, etRegMail)
        }

        if (getPassword().isEmpty()) {
            setupEmptyError(tiRegPassword, etRegPassword)
        }

        if (getPassword2().isEmpty()) {
            setupEmptyError(tiRegPassword2, etRegPassword2)
        }

        if (getPassword() != getPassword2()) {
            setupDoNotMatchPasswordError()
        }
    }

    private fun setupDoNotMatchPasswordError() {
        tiRegPassword2?.error = getString(R.string.error_message_passwords_no_not_match)

        etRegPassword2?.doAfterTextChanged {
            tiRegPassword2?.error = null
        }
    }

    private fun setupEmptyError(inputLayout: TextInputLayout?, editText: EditText?) {
        inputLayout?.error = getString(R.string.error_message_empty_field)

        editText?.doAfterTextChanged {
            inputLayout?.error = null
        }
    }

    private fun getLogin(): String = etRegLogin?.text?.toString() ?: ""

    private fun getPassword(): String = etRegPassword?.text?.toString() ?: ""

    private fun getPassword2(): String = etRegPassword2?.text?.toString() ?: ""

    private fun getMail(): String = etRegMail?.text?.toString() ?: ""
}