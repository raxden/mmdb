package com.raxdenstudios.app.feature.error.di

import com.raxdenstudios.core.ErrorManager
import com.raxdenstudios.app.feature.error.ErrorManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface ErrorFeatureModule {

    @Binds
    fun bindErrorManager(useCase: ErrorManagerImpl): ErrorManager
}
