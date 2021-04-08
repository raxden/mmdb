package com.raxdenstudios.app.account.data.local.datasource

import com.raxdenstudios.app.account.domain.model.Account

interface AccountLocalDataSource {
  suspend fun getAccount(): Account
  suspend fun createAccount(account: Account)
}
