package com.raxdenstudios.app.core.network.interceptor

import com.raxdenstudios.app.core.network.APIDataProvider
import com.raxdenstudios.app.core.network.di.APIVersionV4
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject

class AuthorizationBearerInterceptor @Inject constructor(
    @APIVersionV4 private val apiDataProvider: APIDataProvider,
) : Interceptor {

    companion object {

        private const val AUTHORIZATION = "Authorization"
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request().newBuilder()
            .addHeader(AUTHORIZATION, "Bearer ${apiDataProvider.token}")
            .build()
        return chain.proceed(request)
    }
}
