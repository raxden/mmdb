package com.raxdenstudios.app.core.data.local.datasource

import com.raxdenstudios.app.core.data.local.mapper.AccountEntityToDomainMapper
import com.raxdenstudios.app.core.data.local.mapper.AccountToEntityMapper
import com.raxdenstudios.app.core.database.AccountDatabase
import com.raxdenstudios.app.core.database.dao.AccountDao
import com.raxdenstudios.core.model.Account
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class AccountLocalDataSource @Inject constructor(
    private val database: AccountDatabase,
    private val accountEntityToDomainMapper: AccountEntityToDomainMapper,
    private val accountToEntityMapper: AccountToEntityMapper,
) {

    private val dao: AccountDao
        get() = database.dao()

    fun observeAccount(): Flow<Account> = flow {
        val accountEntity = dao.get()
        if (accountEntity == null) createGuestAccount()
        emitAll(
            dao.observe().map { entity ->
                accountEntityToDomainMapper.transform(entity)
            }
        )
    }

    suspend fun getAccount(): Account {
        val accountEntity = dao.get()
        return if (accountEntity == null) createGuestAccount()
        else accountEntityToDomainMapper.transform(accountEntity)
    }

    suspend fun createAccount(account: Account) {
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
