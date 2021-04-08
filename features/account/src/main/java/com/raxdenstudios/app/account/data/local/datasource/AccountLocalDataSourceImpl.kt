package com.raxdenstudios.app.account.data.local.datasource

import com.raxdenstudios.app.account.data.local.AccountDatabase
import com.raxdenstudios.app.account.data.local.mapper.AccountEntityToDomainMapper
import com.raxdenstudios.app.account.data.local.mapper.AccountToEntityMapper
import com.raxdenstudios.app.account.domain.model.Account

internal class AccountLocalDataSourceImpl(
  accountDatabase: AccountDatabase,
  private val accountEntityToDomainMapper: AccountEntityToDomainMapper,
  private val accountToEntityMapper: AccountToEntityMapper,
) : AccountLocalDataSource {

  private val dao = accountDatabase.dao()

  override suspend fun getAccount(): Account {
    val accountEntity = dao.get()
    return if (accountEntity == null) {
      createGuestAccount()
    } else {
      accountEntityToDomainMapper.transform(accountEntity)
    }
  }

  override suspend fun createAccount(account: Account) {
    dao.insert(accountToEntityMapper.transform(account))
  }

  suspend fun clearAccount() {
    dao.clear()
  }

  private suspend fun createGuestAccount(): Account {
    val account = Account.Guest.default
    createAccount(account)
    return getAccount()
  }
}
