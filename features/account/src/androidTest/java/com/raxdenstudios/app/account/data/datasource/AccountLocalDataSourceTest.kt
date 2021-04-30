package com.raxdenstudios.app.account.data.datasource

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.raxdenstudios.app.account.data.local.AccountDatabase
import com.raxdenstudios.app.account.data.local.datasource.AccountLocalDataSource
import com.raxdenstudios.app.account.di.accountDataModule
import com.raxdenstudios.app.account.domain.model.Account
import com.raxdenstudios.app.test.BaseAndroidTest
import com.raxdenstudios.commons.test.rules.CoroutineTestRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.inject

@ExperimentalCoroutinesApi
internal class AccountLocalDataSourceTest : BaseAndroidTest() {

  private val testDispatcher = TestCoroutineDispatcher()

  @get:Rule
  val instantExecutorRule = InstantTaskExecutorRule()

  @get:Rule
  val coroutineTestRule = CoroutineTestRule(testDispatcher)

  private val database: AccountDatabase = AccountDatabase.switchToInMemory(context)

  override val modules: List<Module>
    get() = listOf(
      accountDataModule,
      module {
        single(override = true) { database }
      }
    )

  private val dataSource: AccountLocalDataSource by inject()

  @After
  fun tearDown() {
    database.close()
  }

  @Test
  fun given_an_empty2_database_When_getAccount_is_called_Then_create_guest_account_and_return() =
    testDispatcher.runBlockingTest {
      val flow = dataSource.observe()

      assertEquals(Account.Guest.default, flow.first())
    }

  @Test
  fun given_an_empty_database_When_getAccount_is_called_Then_create_guest_account_and_return() =
    testDispatcher.runBlockingTest {
      val account = dataSource.getAccount()

      assertEquals(Account.Guest.default, account)
    }
}
