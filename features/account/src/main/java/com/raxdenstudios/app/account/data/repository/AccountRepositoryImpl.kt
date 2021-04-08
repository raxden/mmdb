package com.raxdenstudios.app.account.data.repository

import com.raxdenstudios.app.account.data.local.datasource.AccountLocalDataSource
import com.raxdenstudios.app.account.domain.model.Account
import com.raxdenstudios.app.account.domain.model.Credentials

internal class AccountRepositoryImpl(
  private val accountLocalDataSource: AccountLocalDataSource
) : AccountRepository {

  override suspend fun getAccount(): Account =
    accountLocalDataSource.getAccount()

  override suspend fun createAccountWithCredentials(credentials: Credentials) {
    val account = Account.Logged.withCredentials(credentials)
    accountLocalDataSource.createAccount(account)
  }
}
