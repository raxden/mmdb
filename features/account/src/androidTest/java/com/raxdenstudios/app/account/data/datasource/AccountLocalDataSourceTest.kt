package com.raxdenstudios.app.account.data.datasource

import com.raxdenstudios.app.account.data.local.AccountDatabase
import com.raxdenstudios.app.account.data.local.datasource.AccountLocalDataSource
import com.raxdenstudios.app.account.di.accountDataModule
import com.raxdenstudios.app.account.domain.model.Account
import com.raxdenstudios.app.test.BaseAndroidTest
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Test
import org.koin.core.inject
import org.koin.core.module.Module
import org.koin.dsl.module

internal class AccountLocalDataSourceTest : BaseAndroidTest() {

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
  fun given_an_empty_database_When_getAccount_is_called_Then_create_guest_account_and_return() {
    runBlocking {
      val account = dataSource.getAccount()

      assertEquals(Account.Guest.default, account)
    }
  }
}
