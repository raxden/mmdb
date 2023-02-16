package com.raxdenstudios.app.core.network.interceptor

import com.raxdenstudios.app.core.network.APIDataProvider
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject

class TokenInterceptor @Inject constructor(
    private val apiDataProvider: APIDataProvider,
) : Interceptor {

    companion object {

        private const val API_KEY = "api_key"
    }

    private val apiToken: String
        get() = apiDataProvider.token

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        val url = request.url.newBuilder()
            .addQueryParameter(API_KEY, apiToken)
            .build()
        request = request.newBuilder().url(url).build()
        return chain.proceed(request)
    }
}
