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
import okhttp3.Cache
import okhttp3.HttpUrl
import okhttp3.HttpUrl.Companion.toHttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import timber.log.Timber
import java.io.File
import java.util.concurrent.TimeUnit

val networkLibraryModule = module {

  val cacheDirectory = "responses"
  val cacheSize = 10 * 1024 * 1024  // 10 MiB;
  val timeout = 35                  // 35 sec
  val connectionTimeout = 15        // 15 sec

  @Suppress("LongParameterList")
  fun provideHttpClientV3Builder(
    cacheLoggerInterceptor: CacheLoggerInterceptor,
    cacheNetworkInterceptor: CacheNetworkInterceptor,
    cacheOfflineInterceptor: CacheOfflineInterceptor,
    httpLoggingInterceptor: HttpLoggingInterceptor,
    languageInterceptor: LanguageInterceptor,
    tokenInterceptor: TokenInterceptor,
    sessionInterceptor: SessionInterceptor,
//    ganderInterceptor: GanderInterceptor,
    cache: Cache
  ): OkHttpClient.Builder = OkHttpClient.Builder()
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

  fun provideHttpClientV4Builder(
    cacheLoggerInterceptor: CacheLoggerInterceptor,
    cacheNetworkInterceptor: CacheNetworkInterceptor,
    cacheOfflineInterceptor: CacheOfflineInterceptor,
    httpLoggingInterceptor: HttpLoggingInterceptor,
    languageInterceptor: LanguageInterceptor,
    accessTokenInterceptor: AccessTokenInterceptor,
//    ganderInterceptor: GanderInterceptor,
    cache: Cache
  ): OkHttpClient.Builder = OkHttpClient.Builder()
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

  fun provideRetrofit(
    baseUrl: HttpUrl,
    okHttpClient: OkHttpClient,
    gson: Gson
  ): Retrofit = Retrofit.Builder()
    .baseUrl(baseUrl)
    .client(okHttpClient)
    .addCallAdapterFactory(NetworkResponseAdapterFactory())
    .addConverterFactory(GsonConverterFactory.create(gson))
    .build()

  // Cache =========================================================================================

  single { Cache(File(get<Context>().cacheDir, cacheDirectory), cacheSize.toLong()) }

  // Interceptors ==================================================================================

  single<HttpLoggingInterceptor.Logger> {
    object : HttpLoggingInterceptor.Logger {
      override fun log(message: String) {
        Timber.tag("OkHttp").d(message)
      }
    }
  }
  single { HttpLoggingInterceptor(get()).apply { level = HttpLoggingInterceptor.Level.BODY } }
  single { CacheLoggerInterceptor { message -> Timber.tag("OkHttp").d(message) } }
  single { CacheNetworkInterceptor.default }
  single { CacheOfflineInterceptor.default { Network.isNetworkConnected(get()) } }
  single { TokenInterceptor(get(named(APIVersion.V3))) }
  single { SessionInterceptor(get()) }
  single { AccessTokenInterceptor(get()) }
  single { LanguageInterceptor() }
  single { GanderInterceptor(get()).apply { showNotification(true) } }

  // OKHttpClient ==================================================================================

  single(named(APIVersion.V3)) {
    provideHttpClientV3Builder(
      languageInterceptor = get(),
      tokenInterceptor = get(),
      sessionInterceptor = get(),
      cacheOfflineInterceptor = get(),
      cacheNetworkInterceptor = get(),
      httpLoggingInterceptor = get(),
      cacheLoggerInterceptor = get(),
//      ganderInterceptor = get(),
      cache = get(),
    )
  }

  single(named(APIVersion.V4)) {
    provideHttpClientV4Builder(
      languageInterceptor = get(),
      accessTokenInterceptor = get(),
      cacheOfflineInterceptor = get(),
      cacheNetworkInterceptor = get(),
      httpLoggingInterceptor = get(),
      cacheLoggerInterceptor = get(),
//      ganderInterceptor = get(),
      cache = get(),
    )
  }

  single(named(APIVersion.V3)) { get<OkHttpClient.Builder>(named(APIVersion.V3)).build() }
  single(named(APIVersion.V4)) { get<OkHttpClient.Builder>(named(APIVersion.V4)).build() }

  // Gson ==========================================================================================

  factory { GsonBuilder().excludeFieldsWithoutExposeAnnotation().create() }


  // APIDataProvider ===============================================================================

  single(named(APIVersion.V3)) { APIDataProviderFactory.create(APIVersion.V3) }
  single(named(APIVersion.V4)) { APIDataProviderFactory.create(APIVersion.V4) }

  // BaseUrl =======================================================================================

  factory(named(APIVersion.V3)) {
    get<APIDataProvider>(named(APIVersion.V3)).getDomain().toHttpUrl()
  }

  factory(named(APIVersion.V4)) {
    get<APIDataProvider>(named(APIVersion.V4)).getDomain().toHttpUrl()
  }

  // Retrofit ======================================================================================

  single(named(APIVersion.V3)) {
    provideRetrofit(
      baseUrl = get(named(APIVersion.V3)),
      okHttpClient = get(named(APIVersion.V3)),
      gson = get(),
    )
  }

  single(named(APIVersion.V4)) {
    provideRetrofit(
      baseUrl = get(named(APIVersion.V4)),
      okHttpClient = get(named(APIVersion.V4)),
      gson = get(),
    )
  }
}
