package com.raxdenstudios.app.core.data.di

import com.raxdenstudios.app.core.data.HomeModuleRepository
import com.raxdenstudios.app.core.data.HomeModuleRepositoryImpl
import com.raxdenstudios.app.core.data.WatchlistDataSource
import com.raxdenstudios.app.core.data.WatchlistDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    fun bindHomeModuleRepository(repository: HomeModuleRepositoryImpl): HomeModuleRepository

    @Binds
    fun bindWatchlistDataSource(dataSource: WatchlistDataSourceImpl): WatchlistDataSource
}
