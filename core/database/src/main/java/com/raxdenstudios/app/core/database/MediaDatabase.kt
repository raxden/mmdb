package com.raxdenstudios.app.core.database

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.raxdenstudios.app.core.database.dao.MediaDao
import com.raxdenstudios.app.core.database.dao.WatchlistDao
import com.raxdenstudios.app.core.database.migration.MediaDatabaseMigrationV1toV2
import com.raxdenstudios.app.core.database.model.MediaEntity
import com.raxdenstudios.app.core.database.model.WatchlistEntity

@Database(
    entities = [
        MediaEntity::class,
        WatchlistEntity::class,
    ],
    version = 2,
)
abstract class MediaDatabase : RoomDatabase() {

    abstract fun mediaDao(): MediaDao
    abstract fun watchlistDao(): WatchlistDao

    companion object {

        const val NAME = "media.db"

        @Volatile
        private var INSTANCE: MediaDatabase? = null

        fun getInstance(context: Context): MediaDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
        }

        @VisibleForTesting
        fun switchToInMemory(context: Context): MediaDatabase {
            return Room.inMemoryDatabaseBuilder(context, MediaDatabase::class.java)
                // allowing main thread queries, just for testing
                .addMigrations(
                    MediaDatabaseMigrationV1toV2()
                )
                .allowMainThreadQueries()
                .build()
        }

        private fun buildDatabase(context: Context): MediaDatabase {
            return Room.databaseBuilder(context, MediaDatabase::class.java, NAME)
                .addMigrations(
                    MediaDatabaseMigrationV1toV2()
                )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}
