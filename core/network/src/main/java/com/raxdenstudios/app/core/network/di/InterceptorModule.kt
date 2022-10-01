package com.raxdenstudios.app.core.network.di

import android.content.Context
import com.ashokvarma.gander.GanderInterceptor
import com.raxdenstudios.app.core.network.APIDataProvider
import com.raxdenstudios.app.core.network.interceptor.TokenInterceptor
import com.raxdenstudios.commons.okhttp3.CacheLoggerInterceptor
import com.raxdenstudios.commons.okhttp3.CacheNetworkInterceptor
import com.raxdenstudios.commons.okhttp3.CacheOfflineInterceptor
import com.raxdenstudios.commons.util.Network
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.logging.HttpLoggingInterceptor
import timber.log.Timber
import java.io.File

@Module
@InstallIn(SingletonComponent::class)
object InterceptorModule {

    @Provides
    fun cache(
        @ApplicationContext context: Context
    ): Cache =
        Cache(File(context.cacheDir, cacheDirectory), cacheSize.toLong())

    @Provides
    fun cacheLoggerInterceptor(): CacheLoggerInterceptor =
        CacheLoggerInterceptor { message -> Timber.tag("OkHttp").d(message) }

    @Provides
    fun cacheNetworkInterceptor(): CacheNetworkInterceptor =
        CacheNetworkInterceptor.default

    @Provides
    fun cacheOfflineInterceptor(
        @ApplicationContext context: Context
    ): CacheOfflineInterceptor =
        CacheOfflineInterceptor.default { Network.isNetworkConnected(context) }

    @Provides
    fun httpLoggingInterceptorLogger(): HttpLoggingInterceptor.Logger =
        HttpLoggingInterceptor.Logger { message -> Timber.tag("OkHttp").d(message) }

    @Provides
    fun httpLoggingInterceptor(
        logger: HttpLoggingInterceptor.Logger
    ): HttpLoggingInterceptor =
        HttpLoggingInterceptor(logger)
            .apply { level = HttpLoggingInterceptor.Level.BODY }

    @Provides
    fun tokenInterceptor(
        @APIVersionV3 apiDataProvider: APIDataProvider
    ): TokenInterceptor = TokenInterceptor(apiDataProvider)

    @Provides
    fun genderInterceptor(
        @ApplicationContext context: Context
    ): GanderInterceptor =
        GanderInterceptor(context)
            .apply { showNotification(true) }

    private const val cacheDirectory = "responses"
    private const val cacheSize = 10 * 1024 * 1024  // 10 MiB;
}
