package com.raxdenstudios.app.account.data.repository

import com.raxdenstudios.app.account.domain.model.Account
import com.raxdenstudios.app.account.domain.model.Credentials

interface AccountRepository {
  suspend fun getAccount(): Account
  suspend fun createAccountWithCredentials(credentials: Credentials)
}
