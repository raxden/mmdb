package com.raxdenstudios.app.account.di

import com.raxdenstudios.app.account.data.local.AccountDatabase
import com.raxdenstudios.app.account.data.local.datasource.AccountLocalDataSource
import com.raxdenstudios.app.account.data.local.datasource.AccountLocalDataSourceImpl
import com.raxdenstudios.app.account.data.local.mapper.AccountEntityToDomainMapper
import com.raxdenstudios.app.account.data.local.mapper.AccountToEntityMapper
import com.raxdenstudios.app.account.data.repository.AccountRepository
import com.raxdenstudios.app.account.data.repository.AccountRepositoryImpl
import com.raxdenstudios.app.account.domain.GetAccountUseCase
import com.raxdenstudios.app.account.domain.GetAccountUseCaseImpl
import com.raxdenstudios.app.account.domain.IsAccountLoggedUseCase
import com.raxdenstudios.app.account.domain.IsAccountLoggedUseCaseImpl
import org.koin.dsl.module

val accountDataModule = module {

  single { AccountDatabase.getInstance(get()) }

  factory { AccountEntityToDomainMapper() }
  factory { AccountToEntityMapper() }

  factory<AccountLocalDataSource> { AccountLocalDataSourceImpl(get(), get(), get()) }

  factory<AccountRepository> { AccountRepositoryImpl(get()) }

  factory<GetAccountUseCase> { GetAccountUseCaseImpl(get()) }
  factory<IsAccountLoggedUseCase> { IsAccountLoggedUseCaseImpl(get()) }
}
