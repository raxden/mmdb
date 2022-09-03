package com.raxdenstudios.app.account.data.local.datasource

import com.raxdenstudios.app.account.data.local.AccountDao
import com.raxdenstudios.app.account.data.local.mapper.AccountEntityToDomainMapper
import com.raxdenstudios.app.account.data.local.mapper.AccountToEntityMapper
import com.raxdenstudios.app.account.domain.model.Account
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

internal class AccountLocalDataSourceImpl @Inject constructor(
    private val accountDao: AccountDao,
    private val accountEntityToDomainMapper: AccountEntityToDomainMapper,
    private val accountToEntityMapper: AccountToEntityMapper,
) : AccountLocalDataSource {

    override fun observeAccount(): Flow<Account> = flow {
        val accountEntity = accountDao.get()
        if (accountEntity == null) createGuestAccount()
        emitAll(
            accountDao.observe().map { entity ->
                accountEntityToDomainMapper.transform(entity)
            }
        )
    }

    override suspend fun getAccount(): Account {
        val accountEntity = accountDao.get()
        return if (accountEntity == null) createGuestAccount()
        else accountEntityToDomainMapper.transform(accountEntity)
    }

    override suspend fun createAccount(account: Account) {
        accountDao.insert(accountToEntityMapper.transform(account))
    }

    suspend fun clearAccount() {
        accountDao.clear()
    }

    private suspend fun createGuestAccount(): Account {
        val account = Account.Guest.default
        createAccount(account)
        return getAccount()
    }
}
