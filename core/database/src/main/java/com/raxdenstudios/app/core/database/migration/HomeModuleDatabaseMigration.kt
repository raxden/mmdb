package com.raxdenstudios.app.core.database.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Suppress("MagicNumber")
internal class HomeModuleDatabaseMigrationV1toV2 : Migration(1, 2) {

    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("INSERT INTO home_module (type, subtype, 'order') VALUES (1, 5, 2)")
        database.execSQL("UPDATE home_module SET 'order' = 3 WHERE type = 1 AND subtype = 2")
        database.execSQL("UPDATE home_module SET 'order' = 4 WHERE type = 1 AND subtype = 3")
        database.execSQL("UPDATE home_module SET 'order' = 5 WHERE type = 1 AND subtype = 4")
    }
}

@Suppress("MagicNumber")
internal class HomeModuleDatabaseMigrationV2toV3 : Migration(2, 3) {

    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE home_module RENAME COLUMN subtype TO mediaCategory")
        database.execSQL("ALTER TABLE home_module ADD COLUMN mediaType INTEGER")
    }
}
