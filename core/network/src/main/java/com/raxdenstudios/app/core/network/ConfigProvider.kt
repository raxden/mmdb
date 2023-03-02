package com.raxdenstudios.app.core.network

import javax.inject.Inject

class ConfigProvider @Inject constructor() {

    val language: String
        get() = "es-ES"
    val region: String
        get() = "ES"
}
