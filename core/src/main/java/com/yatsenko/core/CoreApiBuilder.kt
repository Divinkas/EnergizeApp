package com.yatsenko.core

import com.yatsenko.core.install.KoinCoreInstaller

object CoreApiBuilder {

    fun build(): CoreApi {
        KoinCoreInstaller().install()
        return CoreApi()
    }
}