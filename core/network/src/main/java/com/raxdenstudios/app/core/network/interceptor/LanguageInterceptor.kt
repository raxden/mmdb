package com.raxdenstudios.app.core.network.interceptor

import com.raxdenstudios.app.core.network.ConfigProvider
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject

class LanguageInterceptor @Inject constructor(
    private val configProvider: ConfigProvider
) : Interceptor {

    companion object {

        private const val LANGUAGE = "language"
        private const val REGION = "region"
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val url = request.url.newBuilder()
            .addQueryParameter(LANGUAGE, configProvider.language)
            .addQueryParameter(REGION, configProvider.region)
            .build()
        request = request.newBuilder().url(url).build()
        return chain.proceed(request)
    }
}
