package com.raxdenstudios.app.account.di

import com.raxdenstudios.app.account.data.local.AccountDatabase
import com.raxdenstudios.app.account.data.local.datasource.AccountLocalDataSource
import com.raxdenstudios.app.account.data.local.datasource.AccountLocalDataSourceImpl
import com.raxdenstudios.app.account.data.local.mapper.AccountEntityToDomainMapper
import com.raxdenstudios.app.account.data.local.mapper.AccountToEntityMapper
import com.raxdenstudios.app.account.data.repository.AccountRepository
import com.raxdenstudios.app.account.data.repository.AccountRepositoryImpl
import com.raxdenstudios.app.account.domain.IsAccountLogged
import com.raxdenstudios.app.account.domain.IsAccountLoggedImpl
import org.koin.dsl.module

val accountDataModule = module {

  single { AccountDatabase.getInstance(get()) }

  factory { AccountEntityToDomainMapper() }
  factory { AccountToEntityMapper() }

  factory<AccountLocalDataSource> { AccountLocalDataSourceImpl(get(), get(), get()) }

  factory<AccountRepository> { AccountRepositoryImpl(get()) }

  factory<IsAccountLogged> { IsAccountLoggedImpl(get()) }
}
