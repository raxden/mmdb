package com.raxdenstudios.app.home.data.local

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.raxdenstudios.app.home.data.local.model.HomeModuleEntity
import kotlinx.coroutines.runBlocking

@Database(
  entities = [HomeModuleEntity::class],
  version = 1,
  exportSchema = false
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
        .build()
        .also { database -> populateInitialData(database.dao()) }
    }

    private fun buildDatabase(context: Context): HomeModuleDatabase {
      return Room.databaseBuilder(context, HomeModuleDatabase::class.java, "home_module.db")
        .fallbackToDestructiveMigration()
        .build()
        .also { database -> populateInitialData(database.dao()) }
    }

    private fun populateInitialData(dao: HomeModuleDao) {
      runBlocking {
        val elements = dao.getAll().size
        if (elements == 0) {
          val entities = listOf(
            HomeModuleEntity(type = 1, subtype = 1, order = 1),
            HomeModuleEntity(type = 1, subtype = 2, order = 2),
            HomeModuleEntity(type = 1, subtype = 3, order = 3),
            HomeModuleEntity(type = 1, subtype = 4, order = 4),
          )
          dao.insert(entities)
        }
      }
    }
  }
}
