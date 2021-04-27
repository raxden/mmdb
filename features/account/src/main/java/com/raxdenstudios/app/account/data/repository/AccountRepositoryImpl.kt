package com.raxdenstudios.app.account.data.repository

import com.raxdenstudios.app.account.data.local.datasource.AccountLocalDataSource
import com.raxdenstudios.app.account.domain.model.Account
import com.raxdenstudios.app.account.domain.model.Credentials
import kotlinx.coroutines.flow.Flow

internal class AccountRepositoryImpl(
  private val accountLocalDataSource: AccountLocalDataSource
) : AccountRepository {

  override fun observeAccount(): Flow<Account> =
    accountLocalDataSource.observe()

  override suspend fun getAccount(): Account =
    accountLocalDataSource.getAccount()

  override suspend fun createAccountWithCredentials(credentials: Credentials): Account {
    val account = Account.Logged.withCredentials(credentials)
    accountLocalDataSource.createAccount(account)
    return account
  }
}