package com.raxdenstudios.app.network

interface APIDataProvider {
    fun getDomain(): String
    fun getImageDomain(): String
    fun getToken(): String
}
