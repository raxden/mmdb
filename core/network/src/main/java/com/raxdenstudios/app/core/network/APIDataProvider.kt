package com.raxdenstudios.app.core.network

interface APIDataProvider {

    fun getDomain(): String
    fun getImageDomain(): String
    fun getToken(): String
}
