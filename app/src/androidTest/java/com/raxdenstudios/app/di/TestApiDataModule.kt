package com.raxdenstudios.app.di

import com.raxdenstudios.app.core.network.APIDataProvider
import com.raxdenstudios.app.core.network.di.APIDataModule
import com.raxdenstudios.app.core.network.di.APIVersionV3
import com.raxdenstudios.app.core.network.di.APIVersionV4
import dagger.Module
import dagger.Provides
import dagger.hilt.components.SingletonComponent
import dagger.hilt.testing.TestInstallIn
import io.appflate.restmock.RESTMockServer

@Module
@TestInstallIn(
    components = [SingletonComponent::class],
    replaces = [APIDataModule::class]
)
object TestApiDataModule {

    @Provides
    @APIVersionV3
    fun apiV3DataProvider(): APIDataProvider =
        object : APIDataProvider {
            override val baseUrl: String
                get() = RESTMockServer.getUrl()
            override val baseImageUrl: String
                get() = "https://image.tmdb.org/t/p/"
            override val token: String
                get() = ""
        }

    @Provides
    @APIVersionV4
    fun apiV4DataProvider(): APIDataProvider =
        object : APIDataProvider {
            override val baseUrl: String
                get() = RESTMockServer.getUrl()
            override val baseImageUrl: String
                get() = "https://image.tmdb.org/t/p/"
            override val token: String
                get() = ""
        }
}
