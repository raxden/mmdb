package com.raxdenstudios.app.core.database

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.raxdenstudios.app.core.database.dao.RecentSearchDao
import com.raxdenstudios.app.core.database.migration.MediaDatabaseMigrationV1toV2
import com.raxdenstudios.app.core.database.model.RecentSearchEntity

@Database(
    entities = [
        RecentSearchEntity::class,
    ],
    version = 1,
)
abstract class RecentSearchDatabase : RoomDatabase() {

    abstract fun recentSearchDao(): RecentSearchDao

    companion object {

        private const val NAME = "recent_search.db"

        @Volatile
        private var INSTANCE: RecentSearchDatabase? = null

        fun getInstance(context: Context): RecentSearchDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
        }

        @VisibleForTesting
        fun switchToInMemory(context: Context): RecentSearchDatabase {
            return Room.inMemoryDatabaseBuilder(context, RecentSearchDatabase::class.java)
                // allowing main thread queries, just for testing
                .addMigrations(
                    MediaDatabaseMigrationV1toV2()
                )
                .allowMainThreadQueries()
                .build()
        }

        private fun buildDatabase(context: Context): RecentSearchDatabase {
            return Room.databaseBuilder(context, RecentSearchDatabase::class.java, NAME)
                .addMigrations(
                    MediaDatabaseMigrationV1toV2()
                )
                .fallbackToDestructiveMigration()
                .build()
        }
    }
}
