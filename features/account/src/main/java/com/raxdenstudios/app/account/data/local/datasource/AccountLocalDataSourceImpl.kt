package com.raxdenstudios.app.account.data.local.datasource

import com.raxdenstudios.app.account.data.local.AccountDao
import com.raxdenstudios.app.account.data.local.mapper.AccountEntityToDomainMapper
import com.raxdenstudios.app.account.data.local.mapper.AccountToEntityMapper
import com.raxdenstudios.app.account.domain.model.Account

internal class AccountLocalDataSourceImpl(
  private val dao: AccountDao,
  private val accountEntityToDomainMapper: AccountEntityToDomainMapper,
  private val accountToEntityMapper: AccountToEntityMapper,
) : AccountLocalDataSource {

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
