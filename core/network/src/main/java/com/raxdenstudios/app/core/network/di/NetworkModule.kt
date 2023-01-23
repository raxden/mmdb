package com.raxdenstudios.app.core.network.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.haroldadmin.cnradapter.NetworkResponseAdapterFactory
import com.raxdenstudios.app.core.network.APIDataProvider
import com.raxdenstudios.app.core.network.APIDataProviderFactory
import com.raxdenstudios.app.core.network.HttpClientFactory
import com.raxdenstudios.app.core.network.interceptor.AuthorizationBearerInterceptor
import com.raxdenstudios.app.core.network.model.APIVersion
import com.raxdenstudios.app.core.network.service.AuthenticationV3Service
import com.raxdenstudios.app.core.network.service.AuthenticationV4Service
import com.raxdenstudios.app.core.network.service.MediaV3Service
import com.raxdenstudios.app.core.network.service.MediaV4Service
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

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
    fun httpClientV3(
        factory: HttpClientFactory,
    ): OkHttpClient = factory.create(APIVersion.V3)

    @Provides
    @APIVersionV4
    fun httpClientV4(
        factory: HttpClientFactory,
    ): OkHttpClient = factory.create(APIVersion.V4)

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
}
