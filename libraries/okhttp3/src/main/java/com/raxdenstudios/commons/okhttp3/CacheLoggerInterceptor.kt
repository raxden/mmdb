package com.raxdenstudios.commons.okhttp3

import okhttp3.Interceptor
import okhttp3.Response

class CacheLoggerInterceptor(
  private val printMessage: (message: String) -> Unit
) : Interceptor {

  override fun intercept(chain: Interceptor.Chain): Response {
    val response = chain.proceed(chain.request())
    if (response.networkResponse != null)
      printMessage("Response from network")
    if (response.cacheResponse != null)
      printMessage("(HIT) Response from cache")
    return response
  }
}
