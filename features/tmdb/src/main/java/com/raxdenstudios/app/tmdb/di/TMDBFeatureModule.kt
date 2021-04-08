package com.raxdenstudios.app.tmdb.di

import androidx.fragment.app.FragmentActivity
import com.google.gson.Gson
import com.haroldadmin.cnradapter.NetworkResponseAdapterFactory
import com.raxdenstudios.app.network.model.APIVersion
import com.raxdenstudios.app.tmdb.TMDBConnect
import com.raxdenstudios.app.tmdb.TMDBConnectImpl
import com.raxdenstudios.app.tmdb.data.remote.AuthenticationGateway
import com.raxdenstudios.app.tmdb.data.remote.AuthorizationBearerInterceptor
import com.raxdenstudios.app.tmdb.data.remote.datasource.AuthenticationRemoteDataSource
import com.raxdenstudios.app.tmdb.data.remote.service.AuthenticationV3Service
import com.raxdenstudios.app.tmdb.data.remote.service.AuthenticationV4Service
import com.raxdenstudios.app.tmdb.data.repository.AuthenticationRepository
import com.raxdenstudios.app.tmdb.domain.ConnectUseCase
import com.raxdenstudios.app.tmdb.domain.RequestTokenUseCase
import com.raxdenstudios.app.tmdb.view.viewmodel.TMDBViewModel
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val tmdbFeatureModule = module {

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

  single(named("auth")) {
    OkHttpClient.Builder()
      .addNetworkInterceptor(get<HttpLoggingInterceptor>())
      .addInterceptor(AuthorizationBearerInterceptor(get(named(APIVersion.V4))))
      .retryOnConnectionFailure(true)
      .build()
  }

  single(named("auth")) {
    provideRetrofit(
      baseUrl = get(named(APIVersion.V4)),
      okHttpClient = get(named("auth")),
      gson = get(),
    )
  }

  factory { get<Retrofit>(named("auth")).create(AuthenticationV4Service::class.java) }
  factory { get<Retrofit>(named(APIVersion.V3)).create(AuthenticationV3Service::class.java) }

  factory { AuthenticationGateway(get(), get()) }

  factory { AuthenticationRemoteDataSource(get()) }

  factory { AuthenticationRepository(get()) }

  factory { RequestTokenUseCase(get()) }
  factory { ConnectUseCase(get()) }

  viewModel { TMDBViewModel(get(), get()) }

  factory<TMDBConnect> { (activity: FragmentActivity) -> TMDBConnectImpl(activity) }
}
