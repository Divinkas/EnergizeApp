package com.yatsenko.testhelper.di

import android.app.Application
import com.yatsenko.core.CoreApiBuilder
import com.yatsenko.testhelper.EnergizerApplication
import com.yatsenko.testhelper.local.AppSettings
import com.yatsenko.testhelper.ui.auth.AuthViewModel
import com.yatsenko.testhelper.ui.chat.ChatViewModel
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.startKoin
import org.koin.dsl.module

class KoinInstaller {

    private val appModule = module {
        single { androidApplication() as EnergizerApplication }
        single { AppSettings(get()) }
        single { CoreApiBuilder.build() }
    }

    private val viewModelModule = module {
        viewModel { AuthViewModel() }
        viewModel { ChatViewModel() }
    }

    fun install(application: Application) {
        startKoin {
            androidLogger()
            androidContext(application)
            modules(appModule)
            modules(viewModelModule)
        }
    }
}