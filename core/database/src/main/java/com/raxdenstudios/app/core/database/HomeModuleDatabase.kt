package com.raxdenstudios.app.core.database

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.raxdenstudios.app.core.database.dao.HomeModuleDao
import com.raxdenstudios.app.core.database.model.HomeModuleEntity

@Database(
    entities = [HomeModuleEntity::class],
    version = 1,
)
abstract class HomeModuleDatabase : RoomDatabase() {

    abstract fun dao(): HomeModuleDao

    companion object {

        private const val NAME = "home_module.db"

        @Volatile
        private var INSTANCE: HomeModuleDatabase? = null

        fun getInstance(
            context: Context,
        ): HomeModuleDatabase {
            return INSTANCE ?: synchronized(this) {
                INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
            }
        }

        @VisibleForTesting
        fun switchToInMemory(
            context: Context,
        ): HomeModuleDatabase {
            return Room.inMemoryDatabaseBuilder(context, HomeModuleDatabase::class.java)
                // allowing main thread queries, just for testing
                .allowMainThreadQueries()
                .fallbackToDestructiveMigrationOnDowngrade()
                .build()
        }

        private fun buildDatabase(
            context: Context,
        ): HomeModuleDatabase {
            return Room.databaseBuilder(context, HomeModuleDatabase::class.java, NAME)
                .fallbackToDestructiveMigrationOnDowngrade()
                .build()
        }
    }
}
