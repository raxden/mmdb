package com.raxdenstudios.app.tmdb.data.remote

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

internal class AuthorizationBearerInterceptor(
  private val apiDataProvider: com.raxdenstudios.app.network.APIDataProvider
) : Interceptor {

  companion object {
    private const val AUTHORIZATION = "Authorization"
  }

  @Throws(IOException::class)
  override fun intercept(chain: Interceptor.Chain): Response {
    val request = chain.request().newBuilder()
      .addHeader(AUTHORIZATION, "Bearer ${apiDataProvider.getToken()}")
      .build()
    return chain.proceed(request)
  }
}
