package com.raxdenstudios.app.account.data.repository

import com.raxdenstudios.app.account.domain.model.Account
import com.raxdenstudios.app.account.domain.model.Credentials
import kotlinx.coroutines.flow.Flow

interface AccountRepository {

    fun observeAccount(): Flow<Account>
    suspend fun getAccount(): Account
    suspend fun createAccountWithCredentials(credentials: Credentials): Account
}
