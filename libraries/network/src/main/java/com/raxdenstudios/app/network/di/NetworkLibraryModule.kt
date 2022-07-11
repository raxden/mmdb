package com.raxdenstudios.app.network.di

import android.content.Context
import com.ashokvarma.gander.GanderInterceptor
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.haroldadmin.cnradapter.NetworkResponseAdapterFactory
import com.raxdenstudios.app.network.APIDataProvider
import com.raxdenstudios.app.network.APIDataProviderFactory
import com.raxdenstudios.app.network.AccessTokenInterceptor
import com.raxdenstudios.app.network.LanguageInterceptor
import com.raxdenstudios.app.network.SessionInterceptor
import com.raxdenstudios.app.network.TokenInterceptor
import com.raxdenstudios.app.network.model.APIVersion
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
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.io.File
import java.util.concurrent.TimeUnit

@Module
@InstallIn(SingletonComponent::class)
object NetworkLibraryModule {

  @Provides
  fun cache(@ApplicationContext context: Context): Cache =
    Cache(File(context.cacheDir, cacheDirectory), cacheSize.toLong())

  @Provides
  fun cacheLoggerInterceptor(): CacheLoggerInterceptor =
    CacheLoggerInterceptor { message -> Timber.tag("OkHttp").d(message) }

  @Provides
  fun cacheNetworkInterceptor(): CacheNetworkInterceptor =
    CacheNetworkInterceptor.default

  @Provides
  fun cacheOfflineInterceptor(@ApplicationContext context: Context): CacheOfflineInterceptor =
    CacheOfflineInterceptor.default { Network.isNetworkConnected(context) }

  @Provides
  fun httpLoggingInterceptorLogger(): HttpLoggingInterceptor.Logger =
    HttpLoggingInterceptor.Logger { message -> Timber.tag("OkHttp").d(message) }

  @Provides
  fun httpLoggingInterceptor(logger: HttpLoggingInterceptor.Logger): HttpLoggingInterceptor =
    HttpLoggingInterceptor(logger)
      .apply { level = HttpLoggingInterceptor.Level.BODY }

  @Provides
  @APIVersionV3
  fun apiV3DataProvider(factory: APIDataProviderFactory): APIDataProvider =
    factory.create(APIVersion.V3)

  @Provides
  @APIVersionV4
  fun apiV4DataProvider(factory: APIDataProviderFactory): APIDataProvider =
    factory.create(APIVersion.V4)

  @Provides
  fun tokenInterceptor(@APIVersionV3 apiDataProvider: APIDataProvider): TokenInterceptor =
    TokenInterceptor(apiDataProvider)

  @Provides
  fun genderInterceptor(@ApplicationContext context: Context): GanderInterceptor =
    GanderInterceptor(context)
      .apply { showNotification(true) }

  @Provides
  @APIVersionV3
  fun httpClientV3(
    cacheLoggerInterceptor: CacheLoggerInterceptor,
    cacheNetworkInterceptor: CacheNetworkInterceptor,
    cacheOfflineInterceptor: CacheOfflineInterceptor,
    httpLoggingInterceptor: HttpLoggingInterceptor,
    languageInterceptor: LanguageInterceptor,
    tokenInterceptor: TokenInterceptor,
    sessionInterceptor: SessionInterceptor,
//    ganderInterceptor: GanderInterceptor,
    cache: Cache
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
  fun httpClientV4(
    cacheLoggerInterceptor: CacheLoggerInterceptor,
    cacheNetworkInterceptor: CacheNetworkInterceptor,
    cacheOfflineInterceptor: CacheOfflineInterceptor,
    httpLoggingInterceptor: HttpLoggingInterceptor,
    languageInterceptor: LanguageInterceptor,
    accessTokenInterceptor: AccessTokenInterceptor,
//    ganderInterceptor: GanderInterceptor,
    cache: Cache
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
    gson: Gson
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
    gson: Gson
  ): Retrofit = Retrofit.Builder()
    .baseUrl(apiDataProvider.getDomain())
    .client(okHttpClient)
    .addCallAdapterFactory(NetworkResponseAdapterFactory())
    .addConverterFactory(GsonConverterFactory.create(gson))
    .build()

  private const val cacheDirectory = "responses"
  private const val cacheSize = 10 * 1024 * 1024  // 10 MiB;
  private const val timeout = 35                  // 35 sec
  private const val connectionTimeout = 15        // 15 sec
}
