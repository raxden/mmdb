package com.raxdenstudios.app.network

import com.raxdenstudios.app.account.domain.GetAccountUseCase
import com.raxdenstudios.app.account.domain.model.Account
import kotlinx.coroutines.runBlocking
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import javax.inject.Inject

class AccessTokenInterceptor @Inject constructor(
    private val getAccountUseCase: GetAccountUseCase,
) : Interceptor {

    companion object {

        private const val AUTHORIZATION = "Authorization"
    }

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        var request = chain.request()
        runBlocking {
            val accessToken = getAccessToken()
            if (accessToken != null) {
                request = request.newBuilder()
                    .addHeader(AUTHORIZATION, "Bearer $accessToken")
                    .build()
            }
        }
        return chain.proceed(request)
    }

    private suspend fun getAccessToken(): String? {
        return when (val account = getAccountUseCase()) {
            is Account.Logged -> account.credentials.accessToken
            is Account.Guest -> null
        }
    }
}
