package com.raxdenstudios.app.network

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject

class LanguageInterceptor @Inject constructor() : Interceptor {

    companion object {

        private const val LANGUAGE = "language"
        private const val REGION = "region"
    }

    private val language: String
        get() = "es-ES"
    private val region: String
        get() = "ES"

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val url = request.url.newBuilder()
            .addQueryParameter(LANGUAGE, language)
            .addQueryParameter(REGION, region)
            .build()
        request = request.newBuilder().url(url).build()
        return chain.proceed(request)
    }
}
