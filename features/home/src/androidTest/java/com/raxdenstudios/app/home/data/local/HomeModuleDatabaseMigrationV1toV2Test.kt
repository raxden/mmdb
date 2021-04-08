package com.raxdenstudios.app.home.data.local

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.sqlite.db.framework.FrameworkSQLiteOpenHelperFactory
import androidx.test.platform.app.InstrumentationRegistry
import com.raxdenstudios.app.home.data.local.model.HomeModuleEntity
import com.raxdenstudios.app.test.BaseAndroidTest
import com.raxdenstudios.commons.test.rules.CoroutineTestRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.koin.core.module.Module

@ExperimentalCoroutinesApi
internal class HomeModuleDatabaseMigrationV1toV2Test : BaseAndroidTest() {

  companion object {
    private const val TEST_DB_NAME = "home_module.db"
    private const val TEST_TABLE_NAME = "home_module"
  }

  override val modules: List<Module>
    get() = emptyList()

  @ExperimentalCoroutinesApi
  val testDispatcher = TestCoroutineDispatcher()

  @get:Rule
  val instantExecutorRule = InstantTaskExecutorRule()

  @ExperimentalCoroutinesApi
  @get:Rule
  val coroutineTestRule = CoroutineTestRule(testDispatcher)

  @get:Rule
  val testHelper = MigrationTestHelper(
    InstrumentationRegistry.getInstrumentation(),
    HomeModuleDatabase::class.java.canonicalName,
    FrameworkSQLiteOpenHelperFactory()
  )

  @Test
  fun migrateFrom1To2_containsCorrectData() {
    createOriginalDatabase()

    runMigration()

    validateMigratedDatabase()
  }

  private fun createOriginalDatabase() {
    testHelper.createDatabase(TEST_DB_NAME, 1).apply {
      insertHomeModule(1, 1, 1)
      insertHomeModule(1, 2, 2)
      insertHomeModule(1, 3, 3)
      insertHomeModule(1, 4, 4)
      close()
    }
  }

  private fun runMigration() {
    testHelper.runMigrationsAndValidate(
      TEST_DB_NAME,
      2,
      true,
      HomeModuleDatabaseMigrationV1toV2()
    )
  }

  private fun validateMigratedDatabase() {
    val database = getMigratedRoomDatabase()

    runBlockingTest {
      val modules = database.dao().getAll()
      assertEquals(
        listOf(
          HomeModuleEntity.popular.copy(id = 1),
          HomeModuleEntity.watchList.copy(id = 5),
          HomeModuleEntity.nowPlaying.copy(id = 2),
          HomeModuleEntity.topRated.copy(id = 3),
          HomeModuleEntity.upcoming.copy(id = 4),
        ),
        modules
      )
    }
  }

  private fun SupportSQLiteDatabase.insertHomeModule(type: Int, subType: Int, order: Int) {
    val contentValues = ContentValues().apply {
      put("type", type)
      put("subtype", subType)
      put("'order'", order)
    }
    insert(TEST_TABLE_NAME, SQLiteDatabase.CONFLICT_REPLACE, contentValues)
  }

  private fun getMigratedRoomDatabase() =
    HomeModuleDatabase.getInstance(context).apply {
      testHelper.closeWhenFinished(this)
    }
}
