package com.raxdenstudios.app.account.data.local.datasource

import com.raxdenstudios.app.account.domain.model.Account
import kotlinx.coroutines.flow.Flow

interface AccountLocalDataSource {

    fun observeAccount(): Flow<Account>
    suspend fun getAccount(): Account
    suspend fun createAccount(account: Account)
}
