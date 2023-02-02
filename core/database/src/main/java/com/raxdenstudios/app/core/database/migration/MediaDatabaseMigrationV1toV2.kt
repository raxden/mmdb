package com.raxdenstudios.app.core.database.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

@Suppress("MagicNumber")
internal class  MediaDatabaseMigrationV1toV2 : Migration(1, 2) {

    override fun migrate(database: SupportSQLiteDatabase) {
        database.execSQL("ALTER TABLE media ADD COLUMN overview TEXT NOT NULL DEFAULT ''")
    }
}
