package com.raxdenstudios.app.account.data.local.datasource

import com.raxdenstudios.app.account.data.local.AccountDao
import com.raxdenstudios.app.account.data.local.mapper.AccountEntityToDomainMapper
import com.raxdenstudios.app.account.data.local.mapper.AccountToEntityMapper
import com.raxdenstudios.app.account.domain.model.Account
import com.raxdenstudios.app.test.BaseTest
import io.mockk.mockk
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Test

class AccountLocalDataSourceImplTest : BaseTest() {

    private val accountDao: AccountDao = mockk()
    private val accountEntityToDomainMapper: AccountEntityToDomainMapper = mockk()
    private val accountToEntityMapper: AccountToEntityMapper = mockk()
    private val dataSource: AccountLocalDataSource by lazy {
        AccountLocalDataSourceImpl(
            accountDao = accountDao,
            accountEntityToDomainMapper = accountEntityToDomainMapper,
            accountToEntityMapper = accountToEntityMapper,
        )
    }

    @Test
    fun `Given an empty database, When observe is called, Then create guest account`() =
        testDispatcher.runBlockingTest {
            val flow = dataSource.observeAccount()

            Assert.assertEquals(Account.Guest.default, flow.first())
        }

    @Test
    fun `Given an empty database, When getAccount is called, Then create guest account`() =
        testDispatcher.runBlockingTest {
            val account = dataSource.getAccount()

            Assert.assertEquals(Account.Guest.default, account)
        }
}
