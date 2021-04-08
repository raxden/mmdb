package com.raxdenstudios.app.network

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

internal class LanguageInterceptor : Interceptor {

  companion object {
    private const val LANGUAGE = "language"
  }

  private val language: String
    get() = "en-US"

  @Throws(IOException::class)
  override fun intercept(chain: Interceptor.Chain): Response {
    var request = chain.request()
    val url = request.url.newBuilder()
      .addQueryParameter(LANGUAGE, language)
      .build()
    request = request.newBuilder().url(url).build()
    return chain.proceed(request)
  }
}
