package com.raxdenstudios.app.core.data.local.datasource

import androidx.test.filters.SmallTest
import androidx.test.platform.app.InstrumentationRegistry
import app.cash.turbine.test
import com.raxdenstudios.app.core.data.local.mapper.AccountEntityToDomainMapper
import com.raxdenstudios.app.core.data.local.mapper.AccountToEntityMapper
import com.raxdenstudios.app.core.database.AccountDatabase
import com.raxdenstudios.core.model.Account
import com.raxdenstudios.core.model.Credentials
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
@SmallTest
class AccountLocalDataSourceTest {

    private lateinit var database: AccountDatabase
    private val dataSource: AccountLocalDataSource by lazy {
        AccountLocalDataSource(
            database = database,
            accountEntityToDomainMapper = AccountEntityToDomainMapper(),
            accountToEntityMapper = AccountToEntityMapper(),
        )
    }

    @Before
    fun setUp() {
        val context = InstrumentationRegistry.getInstrumentation().context
        database = AccountDatabase.switchToInMemory(context)
    }

    @After
    fun cleanup() {
        database.close()
    }

    @Test
    fun given_an_empty_database_When_observe_is_called_Then_create_guest_account() =
        runTest {
            dataSource.observeAccount().test {
                val account = awaitItem()

                assertEquals(Account.Guest.default, account)
                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun given_an_empty_database_When_getAccount_is_called_Then_create_guest_account() =
        runTest {
            val account = dataSource.getAccount()

            assertEquals(Account.Guest.default, account)
        }

    @Test
    fun given_an_empty_database_When_observe_is_called_Then_create_account() =
        runTest {
            dataSource.observeAccount().test {
                val guestAccount = awaitItem()
                assertEquals(Account.Guest.default, guestAccount)

                dataSource.createAccount(Account.Logged.withCredentials(Credentials.empty))
                val loggedAccount = awaitItem()
                assertEquals(Account.Logged.withCredentials(Credentials.empty), loggedAccount)

                cancelAndIgnoreRemainingEvents()
            }
        }
}
