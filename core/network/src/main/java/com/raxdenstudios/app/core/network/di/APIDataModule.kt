package com.raxdenstudios.app.core.network.di

import com.raxdenstudios.app.core.network.APIDataProvider
import com.raxdenstudios.app.core.network.APIDataProviderFactory
import com.raxdenstudios.app.core.network.model.APIVersion
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object APIDataModule {

    @Provides
    @APIVersionV3
    fun apiV3DataProvider(factory: APIDataProviderFactory): APIDataProvider =
        factory.create(APIVersion.V3)

    @Provides
    @APIVersionV4
    fun apiV4DataProvider(factory: APIDataProviderFactory): APIDataProvider =
        factory.create(APIVersion.V4)
}
