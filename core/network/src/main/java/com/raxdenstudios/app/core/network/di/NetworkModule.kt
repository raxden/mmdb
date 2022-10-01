package com.raxdenstudios.app.core.network.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.haroldadmin.cnradapter.NetworkResponseAdapterFactory
import com.raxdenstudios.app.core.network.APIDataProvider
import com.raxdenstudios.app.core.network.APIDataProviderFactory
import com.raxdenstudios.app.core.network.interceptor.AccessTokenInterceptor
import com.raxdenstudios.app.core.network.interceptor.AuthorizationBearerInterceptor
import com.raxdenstudios.app.core.network.interceptor.LanguageInterceptor
import com.raxdenstudios.app.core.network.interceptor.SessionInterceptor
import com.raxdenstudios.app.core.network.interceptor.TokenInterceptor
import com.raxdenstudios.app.core.network.model.APIVersion
import com.raxdenstudios.app.core.network.service.AuthenticationV3Service
import com.raxdenstudios.app.core.network.service.AuthenticationV4Service
import com.raxdenstudios.app.core.network.service.MediaV3Service
import com.raxdenstudios.app.core.network.service.MediaV4Service
import com.raxdenstudios.commons.okhttp3.CacheLoggerInterceptor
import com.raxdenstudios.commons.okhttp3.CacheNetworkInterceptor
import com.raxdenstudios.commons.okhttp3.CacheOfflineInterceptor
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {

    @Provides
    @APIVersionV3
    fun apiV3DataProvider(factory: APIDataProviderFactory): APIDataProvider =
        factory.create(APIVersion.V3)

    @Provides
    @APIVersionV4
    fun apiV4DataProvider(factory: APIDataProviderFactory): APIDataProvider =
        factory.create(APIVersion.V4)

    @Provides
    @APIVersionV3
    @Suppress("LongParameterList")
    fun httpClientV3(
        cacheLoggerInterceptor: CacheLoggerInterceptor,
        cacheNetworkInterceptor: CacheNetworkInterceptor,
        cacheOfflineInterceptor: CacheOfflineInterceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor,
        languageInterceptor: LanguageInterceptor,
        tokenInterceptor: TokenInterceptor,
        sessionInterceptor: SessionInterceptor,
//    ganderInterceptor: GanderInterceptor,
        cache: Cache,
    ): OkHttpClient = OkHttpClient.Builder()
        .cache(cache)
        .addNetworkInterceptor(httpLoggingInterceptor)
        .addInterceptor(tokenInterceptor)
        .addInterceptor(sessionInterceptor)
        .addInterceptor(languageInterceptor)
        .addInterceptor(cacheOfflineInterceptor)
        .addNetworkInterceptor(cacheNetworkInterceptor)
        .addInterceptor(cacheLoggerInterceptor)
//    .addInterceptor(ganderInterceptor)
        .retryOnConnectionFailure(true)
        .readTimeout(timeout.toLong(), TimeUnit.SECONDS)
        .writeTimeout(timeout.toLong(), TimeUnit.SECONDS)
        .connectTimeout(connectionTimeout.toLong(), TimeUnit.SECONDS)
        .build()

    @Provides
    @APIVersionV4
    @Suppress("LongParameterList")
    fun httpClientV4(
        cacheLoggerInterceptor: CacheLoggerInterceptor,
        cacheNetworkInterceptor: CacheNetworkInterceptor,
        cacheOfflineInterceptor: CacheOfflineInterceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor,
        languageInterceptor: LanguageInterceptor,
        accessTokenInterceptor: AccessTokenInterceptor,
//    ganderInterceptor: GanderInterceptor,
        cache: Cache,
    ): OkHttpClient = OkHttpClient.Builder()
        .cache(cache)
        .addNetworkInterceptor(httpLoggingInterceptor)
        .addInterceptor(accessTokenInterceptor)
        .addInterceptor(languageInterceptor)
        .addInterceptor(cacheOfflineInterceptor)
        .addNetworkInterceptor(cacheNetworkInterceptor)
        .addInterceptor(cacheLoggerInterceptor)
//    .addInterceptor(ganderInterceptor)
        .retryOnConnectionFailure(true)
        .readTimeout(timeout.toLong(), TimeUnit.SECONDS)
        .writeTimeout(timeout.toLong(), TimeUnit.SECONDS)
        .connectTimeout(connectionTimeout.toLong(), TimeUnit.SECONDS)
        .build()

    @Provides
    fun gson(): Gson =
        GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()

    @Provides
    @APIVersionV3
    fun provideRetrofitV3(
        @APIVersionV3 apiDataProvider: APIDataProvider,
        @APIVersionV3 okHttpClient: OkHttpClient,
        gson: Gson,
    ): Retrofit = Retrofit.Builder()
        .baseUrl(apiDataProvider.getDomain())
        .client(okHttpClient)
        .addCallAdapterFactory(NetworkResponseAdapterFactory())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    @Provides
    @APIVersionV4
    fun provideRetrofitV4(
        @APIVersionV4 apiDataProvider: APIDataProvider,
        @APIVersionV4 okHttpClient: OkHttpClient,
        gson: Gson,
    ): Retrofit = Retrofit.Builder()
        .baseUrl(apiDataProvider.getDomain())
        .client(okHttpClient)
        .addCallAdapterFactory(NetworkResponseAdapterFactory())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()


    @Provides
    fun provideMediaV3Service(@APIVersionV3 retrofit: Retrofit): MediaV3Service =
        retrofit.create(MediaV3Service::class.java)

    @Provides
    fun provideMediaV4Service(@APIVersionV4 retrofit: Retrofit): MediaV4Service =
        retrofit.create(MediaV4Service::class.java)

    @Provides
    @Auth
    fun provideAuthOkHttpClient(
        authorizationBearerInterceptor: AuthorizationBearerInterceptor,
        httpLoggingInterceptor: HttpLoggingInterceptor,
    ): OkHttpClient = OkHttpClient.Builder()
        .addNetworkInterceptor(httpLoggingInterceptor)
        .addInterceptor(authorizationBearerInterceptor)
        .retryOnConnectionFailure(true)
        .build()

    @Provides
    @Auth
    fun provideRetrofitAuth(
        @APIVersionV4 apiDataProvider: APIDataProvider,
        @Auth okHttpClient: OkHttpClient,
        gson: Gson,
    ): Retrofit = Retrofit.Builder()
        .baseUrl(apiDataProvider.getDomain())
        .client(okHttpClient)
        .addCallAdapterFactory(NetworkResponseAdapterFactory())
        .addConverterFactory(GsonConverterFactory.create(gson))
        .build()

    @Provides
    fun provideAuthenticationServiceV3(
        @APIVersionV3 retrofit: Retrofit,
    ): AuthenticationV3Service = retrofit.create(AuthenticationV3Service::class.java)

    @Provides
    fun provideAuthenticationServiceV4(
        @Auth retrofit: Retrofit,
    ): AuthenticationV4Service = retrofit.create(AuthenticationV4Service::class.java)

    private const val timeout = 35                  // 35 sec
    private const val connectionTimeout = 15        // 15 sec
}
