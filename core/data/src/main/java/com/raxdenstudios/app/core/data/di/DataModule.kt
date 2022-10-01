package com.raxdenstudios.app.core.data.di

import com.raxdenstudios.app.core.data.HomeModuleRepository
import com.raxdenstudios.app.core.data.HomeModuleRepositoryImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DataModule {

    @Binds
    fun bind(repository: HomeModuleRepositoryImpl): HomeModuleRepository
}
