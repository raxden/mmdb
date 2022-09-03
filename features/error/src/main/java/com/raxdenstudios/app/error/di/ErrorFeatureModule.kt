package com.raxdenstudios.app.error.di

import com.raxdenstudios.app.error.ErrorManager
import com.raxdenstudios.app.error.ErrorManagerImpl
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
abstract class ErrorFeatureModule {

    @Binds
    internal abstract fun bindErrorManager(useCase: ErrorManagerImpl): ErrorManager
}
