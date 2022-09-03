package com.raxdenstudios.app.account.di

import android.content.Context
import com.raxdenstudios.app.account.data.local.AccountDao
import com.raxdenstudios.app.account.data.local.AccountDatabase
import com.raxdenstudios.app.account.data.local.datasource.AccountLocalDataSource
import com.raxdenstudios.app.account.data.local.datasource.AccountLocalDataSourceImpl
import com.raxdenstudios.app.account.data.repository.AccountRepository
import com.raxdenstudios.app.account.data.repository.AccountRepositoryImpl
import com.raxdenstudios.app.account.domain.GetAccountUseCase
import com.raxdenstudios.app.account.domain.GetAccountUseCaseImpl
import com.raxdenstudios.app.account.domain.IsAccountLoggedUseCase
import com.raxdenstudios.app.account.domain.IsAccountLoggedUseCaseImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
object AccountDataModule {

    @Provides
    fun provideAccountDatabase(@ApplicationContext context: Context): AccountDatabase =
        AccountDatabase.getInstance(context)

    @Provides
    fun provideAccountDAO(accountDatabase: AccountDatabase): AccountDao =
        accountDatabase.dao()
}

@Module
@InstallIn(SingletonComponent::class)
abstract class AccountDataBindsModule {

    @Binds
    internal abstract fun bindGetAccountUseCase(useCase: GetAccountUseCaseImpl): GetAccountUseCase

    @Binds
    internal abstract fun bindIsAccountLoggedUseCase(useCase: IsAccountLoggedUseCaseImpl): IsAccountLoggedUseCase

    @Binds
    internal abstract fun bindAccountRepository(useCase: AccountRepositoryImpl): AccountRepository

    @Binds
    internal abstract fun bindAccountLocalDataSource(useCase: AccountLocalDataSourceImpl): AccountLocalDataSource
}
