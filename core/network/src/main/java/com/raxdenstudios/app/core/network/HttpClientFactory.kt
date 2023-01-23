package com.raxdenstudios.app.core.network

import com.raxdenstudios.app.core.network.interceptor.AccessTokenInterceptor
import com.raxdenstudios.app.core.network.interceptor.LanguageInterceptor
import com.raxdenstudios.app.core.network.interceptor.SessionInterceptor
import com.raxdenstudios.app.core.network.interceptor.TokenInterceptor
import com.raxdenstudios.app.core.network.model.APIVersion
import com.raxdenstudios.commons.okhttp3.CacheLoggerInterceptor
import com.raxdenstudios.commons.okhttp3.CacheNetworkInterceptor
import com.raxdenstudios.commons.okhttp3.CacheOfflineInterceptor
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@Suppress("LongParameterList")
class HttpClientFactory @Inject constructor(
    private val cacheLoggerInterceptor: CacheLoggerInterceptor,
    private val cacheNetworkInterceptor: CacheNetworkInterceptor,
    private val cacheOfflineInterceptor: CacheOfflineInterceptor,
    private val httpLoggingInterceptor: HttpLoggingInterceptor,
    private val languageInterceptor: LanguageInterceptor,
    private val tokenInterceptor: TokenInterceptor,
    private val sessionInterceptor: SessionInterceptor,
    private val accessTokenInterceptor: AccessTokenInterceptor,
    private val cache: Cache,
) {

    fun create(version: APIVersion): OkHttpClient = when (version) {
        APIVersion.V3 -> OkHttpClient.Builder()
            .cache(cache)
            .addNetworkInterceptor(httpLoggingInterceptor)
            .addInterceptor(tokenInterceptor)
            .addInterceptor(sessionInterceptor)
            .addInterceptor(languageInterceptor)
            .addInterceptor(cacheOfflineInterceptor)
            .addNetworkInterceptor(cacheNetworkInterceptor)
            .addInterceptor(cacheLoggerInterceptor)
//            .addInterceptor(ganderInterceptor)
            .retryOnConnectionFailure(true)
            .readTimeout(timeout.toLong(), TimeUnit.SECONDS)
            .writeTimeout(timeout.toLong(), TimeUnit.SECONDS)
            .connectTimeout(connectionTimeout.toLong(), TimeUnit.SECONDS)
            .build()
        APIVersion.V4 -> OkHttpClient.Builder()
            .cache(cache)
            .addNetworkInterceptor(httpLoggingInterceptor)
            .addInterceptor(accessTokenInterceptor)
            .addInterceptor(languageInterceptor)
            .addInterceptor(cacheOfflineInterceptor)
            .addNetworkInterceptor(cacheNetworkInterceptor)
            .addInterceptor(cacheLoggerInterceptor)
//            .addInterceptor(ganderInterceptor)
            .retryOnConnectionFailure(true)
            .readTimeout(timeout.toLong(), TimeUnit.SECONDS)
            .writeTimeout(timeout.toLong(), TimeUnit.SECONDS)
            .connectTimeout(connectionTimeout.toLong(), TimeUnit.SECONDS)
            .build()
    }

    companion object {

        private const val timeout = 35                  // 35 sec
        private const val connectionTimeout = 15        // 15 sec
    }
}
