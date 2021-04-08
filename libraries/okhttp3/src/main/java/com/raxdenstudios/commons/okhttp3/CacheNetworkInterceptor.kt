package com.raxdenstudios.commons.okhttp3

import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException
import java.util.concurrent.TimeUnit

class CacheNetworkInterceptor(
  private val cacheControl: CacheControl,
) : Interceptor {

  companion object {
    private const val HEADER_PRAGMA = "Pragma"
    private const val HEADER_CACHE_CONTROL = "Cache-Control"
    private const val MAX_AGE = 5

    val default = CacheNetworkInterceptor(
      initCacheControl(MAX_AGE)
    )

    fun withMaxAge(seconds: Int) = CacheNetworkInterceptor(
      initCacheControl(seconds)
    )

    private fun initCacheControl(seconds: Int) = CacheControl.Builder()
      .maxAge(seconds, TimeUnit.SECONDS)
      .build()
  }

  @Throws(IOException::class)
  override fun intercept(chain: Interceptor.Chain): Response =
    chain.proceed(chain.request()).newBuilder()
      .removeHeader(HEADER_PRAGMA)
      .removeHeader(HEADER_CACHE_CONTROL)
      .header(HEADER_CACHE_CONTROL, cacheControl.toString())
      .build()
}

