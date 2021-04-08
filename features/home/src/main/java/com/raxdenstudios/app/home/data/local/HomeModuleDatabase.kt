package com.raxdenstudios.app.home.data.local

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.raxdenstudios.app.home.data.local.model.HomeModuleEntity

@Database(
  entities = [HomeModuleEntity::class],
  version = 2,
)
abstract class HomeModuleDatabase : RoomDatabase() {

  abstract fun dao(): HomeModuleDao

  companion object {

    @Volatile
    private var INSTANCE: HomeModuleDatabase? = null

    fun getInstance(context: Context): HomeModuleDatabase {
      return INSTANCE ?: synchronized(this) {
        INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
      }
    }

    @VisibleForTesting
    fun switchToInMemory(context: Context): HomeModuleDatabase {
      return Room.inMemoryDatabaseBuilder(context, HomeModuleDatabase::class.java)
        // allowing main thread queries, just for testing
        .allowMainThreadQueries()
        .addMigrations(HomeModuleDatabaseMigrationV1toV2())
        .build()
    }

    private fun buildDatabase(context: Context): HomeModuleDatabase {
      return Room.databaseBuilder(context, HomeModuleDatabase::class.java, "home_module.db")
        .addMigrations(HomeModuleDatabaseMigrationV1toV2())
        .build()
    }
  }
}
