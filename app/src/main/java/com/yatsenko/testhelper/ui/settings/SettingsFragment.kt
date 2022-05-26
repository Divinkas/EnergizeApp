package com.yatsenko.testhelper.ui.settings

import android.os.Bundle
import android.view.View
import com.yatsenko.testhelper.R
import com.yatsenko.testhelper.base.BaseFragment
import com.yatsenko.testhelper.utils.openLoginScreen
import kotlinx.android.synthetic.main.fragment_settings.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class SettingsFragment : BaseFragment() {

    override var layoutRes: Int = R.layout.fragment_settings

    private val viewModel: SettingsViewModel by viewModel()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initView()
        setupListeners()
    }

    private fun initView() {
        setupNavigateUpView(ivSettingsBack)

        tvUserName?.text = viewModel.getUserName()
    }

    private fun setupListeners() {
        btnLogOut?.setOnClickListener {
            viewModel.logOut()
            activity?.openLoginScreen()
        }
    }
}