package com.raxdenstudios.app.home.data.local

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

internal class HomeModuleDatabaseMigrationV1toV2 : Migration(1, 2) {

    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("INSERT INTO home_module (type, subtype, 'order') VALUES (1, 5, 2)")
        database.execSQL("UPDATE home_module SET 'order' = 3 WHERE type = 1 AND subtype = 2")
        database.execSQL("UPDATE home_module SET 'order' = 4 WHERE type = 1 AND subtype = 3")
        database.execSQL("UPDATE home_module SET 'order' = 5 WHERE type = 1 AND subtype = 4")
    }
}
