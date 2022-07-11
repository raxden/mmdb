package com.raxdenstudios.app.tmdb.di

import com.google.gson.Gson
import com.haroldadmin.cnradapter.NetworkResponseAdapterFactory
import com.raxdenstudios.app.network.APIDataProvider
import com.raxdenstudios.app.network.di.APIVersionV3
import com.raxdenstudios.app.network.di.APIVersionV4
import com.raxdenstudios.app.tmdb.TMDBConnect
import com.raxdenstudios.app.tmdb.TMDBConnectImpl
import com.raxdenstudios.app.tmdb.data.remote.AuthorizationBearerInterceptor
import com.raxdenstudios.app.tmdb.data.remote.service.AuthenticationV3Service
import com.raxdenstudios.app.tmdb.data.remote.service.AuthenticationV4Service
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
object TMDBFeatureModule {

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
    gson: Gson
  ): Retrofit = Retrofit.Builder()
    .baseUrl(apiDataProvider.getDomain())
    .client(okHttpClient)
    .addCallAdapterFactory(NetworkResponseAdapterFactory())
    .addConverterFactory(GsonConverterFactory.create(gson))
    .build()

  @Provides
  fun provideAuthenticationServiceV3(
    @APIVersionV3 retrofit: Retrofit
  ): AuthenticationV3Service = retrofit.create(AuthenticationV3Service::class.java)

  @Provides
  fun provideAuthenticationServiceV4(
    @Auth retrofit: Retrofit
  ): AuthenticationV4Service = retrofit.create(AuthenticationV4Service::class.java)
}


@Module
@InstallIn(ActivityComponent::class)
abstract class TMDBFeatureBindsModule {

  @Binds
  internal abstract fun bindTMDBConnect(useCase: TMDBConnectImpl): TMDBConnect
}
