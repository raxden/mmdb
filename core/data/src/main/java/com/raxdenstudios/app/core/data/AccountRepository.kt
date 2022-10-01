package com.raxdenstudios.app.core.data

import com.raxdenstudios.app.core.data.local.datasource.AccountLocalDataSource
import com.raxdenstudios.core.model.Account
import com.raxdenstudios.core.model.Credentials
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class AccountRepository @Inject constructor(
    private val accountLocalDataSource: AccountLocalDataSource,
) {

    fun observeAccount(): Flow<Account> =
        accountLocalDataSource.observeAccount()

    suspend fun getAccount(): Account =
        accountLocalDataSource.getAccount()

    suspend fun createAccountWithCredentials(credentials: Credentials): Account {
        val account = Account.Logged.withCredentials(credentials)
        accountLocalDataSource.createAccount(account)
        return account
    }
}
