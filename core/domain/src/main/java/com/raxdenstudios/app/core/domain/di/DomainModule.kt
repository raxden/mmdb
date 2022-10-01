package com.raxdenstudios.app.core.domain.di

import com.raxdenstudios.app.core.domain.GetAccountUseCaseImpl
import com.raxdenstudios.core.domain.GetAccountUseCase
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface DomainModule {

    @Binds
    fun bindGetAccountUseCase(useCase: GetAccountUseCaseImpl): GetAccountUseCase
}
