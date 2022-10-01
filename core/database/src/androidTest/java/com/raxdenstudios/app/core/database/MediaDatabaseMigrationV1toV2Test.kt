package com.raxdenstudios.app.core.database

import androidx.room.migration.Migration
import androidx.room.testing.MigrationTestHelper
import androidx.sqlite.db.SupportSQLiteDatabase
import androidx.test.platform.app.InstrumentationRegistry
import com.raxdenstudios.app.core.database.migration.HomeModuleDatabaseMigrationV1toV2
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MediaDatabaseMigrationV1toV2Test {

    companion object {
        private const val FROM_VERSION = 1
        private const val TO_VERSION = 2
    }

    @get:Rule
    val testHelper = MigrationTestHelper(
        InstrumentationRegistry.getInstrumentation(),
        MediaDatabase::class.java,
    )

    @Test
    fun migrate1To2() {
        testHelper.createDatabase().use {
            testHelper.runMigrationAndValidate()
        }
    }

    private fun MigrationTestHelper.createDatabase(
        name: String = MediaDatabase.NAME,
        version: Int = FROM_VERSION,
    ): SupportSQLiteDatabase = createDatabase(name, version)

    private fun MigrationTestHelper.runMigrationAndValidate(
        name: String = MediaDatabase.NAME,
        version: Int = TO_VERSION,
        migration: Migration = HomeModuleDatabaseMigrationV1toV2()
    ) {
        runMigrationsAndValidate(
            name,
            version,
            true,
            migration
        )
    }
}
