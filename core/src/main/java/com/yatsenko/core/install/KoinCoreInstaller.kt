package com.yatsenko.core.install

import com.google.gson.Gson
import com.yatsenko.core.CoreSDK
import com.yatsenko.core.components.SocketComponent
import com.yatsenko.core.components.ChatComponent
import org.koin.core.context.loadKoinModules
import org.koin.dsl.koinApplication
import org.koin.dsl.module

class KoinCoreInstaller {

    private val componentModule = module {
        single { SocketComponent() }
        single { ChatComponent() }
        single { CoreSDK(get(), get()) }
    }

    private val supportComponents = module {
        single { Gson() }
    }

    fun install() {
        koinApplication {
            loadKoinModules(listOf(componentModule, supportComponents))
        }
    }
}