package com.raxdenstudios.app.core.database

import android.content.ContentValues
import android.database.sqlite.SQLiteDatabase
import androidx.room.migration.Migration
import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.test.platform.app.InstrumentationRegistry
import com.raxdenstudios.app.core.database.migration.HomeModuleDatabaseMigrationV1toV2
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test

//@ExperimentalCoroutinesApi
//class HomeModuleDatabaseMigrationV1toV2Test {
//
//    companion object {
//        private const val TEST_DB_NAME = "home_module.db"
//        private const val TEST_TABLE_NAME = "home_module"
//        private const val FROM_VERSION = 1
//        private const val TO_VERSION = 2
//    }
//
//    @get:Rule
//    val testHelper = MigrationTestHelper(
//        InstrumentationRegistry.getInstrumentation(),
//        HomeModuleDatabase::class.java,
//    )
//
//    @Test
//    fun migrate1To2() {
//        testHelper.createDatabase().use { database ->
//            database.insertInitialData()
//            testHelper.runMigrationAndValidate()
//            database.validateData()
//        }
//    }
//
//    private fun SupportSQLiteDatabase.validateData() {
//        val query = "SELECT * FROM $TEST_TABLE_NAME WHERE type = ? AND subtype = ? AND \"order\" = ?"
//        val args = arrayOf(1, 5, 2)
//        query(query, args).use { cursor ->
//            assertTrue(cursor.moveToFirst())
//        }
//    }
//
//    private fun SupportSQLiteDatabase.insertInitialData() {
//        insertHomeModule(1, 1, 1)
//        insertHomeModule(1, 2, 2)
//        insertHomeModule(1, 3, 3)
//        insertHomeModule(1, 4, 4)
//    }
//
//    private fun MigrationTestHelper.createDatabase(
//        name: String = TEST_DB_NAME,
//        version: Int = FROM_VERSION,
//    ): SupportSQLiteDatabase = createDatabase(name, version)
//
//    private fun MigrationTestHelper.runMigrationAndValidate(
//        name: String = TEST_DB_NAME,
//        version: Int = TO_VERSION,
//        migration: Migration = HomeModuleDatabaseMigrationV1toV2()
//    ) {
//        runMigrationsAndValidate(
//            name,
//            version,
//            true,
//            migration
//        )
//    }
//
//    private fun SupportSQLiteDatabase.insertHomeModule(type: Int, subType: Int, order: Int) {
//        val contentValues = ContentValues().apply {
//            put("type", type)
//            put("subtype", subType)
//            put("'order'", order)
//        }
//        insert(TEST_TABLE_NAME, SQLiteDatabase.CONFLICT_REPLACE, contentValues)
//    }
//}
