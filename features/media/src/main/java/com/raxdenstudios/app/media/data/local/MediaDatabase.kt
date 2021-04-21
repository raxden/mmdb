package com.raxdenstudios.app.media.data.local

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.raxdenstudios.app.media.data.local.model.WatchListEntity

@Database(
  entities = [WatchListEntity::class],
  version = 1,
)
abstract class MediaDatabase : RoomDatabase() {

  abstract fun watchListDao(): WatchListDao

  companion object {

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
        .allowMainThreadQueries()
        .build()
    }

    private fun buildDatabase(context: Context): MediaDatabase {
      return Room.databaseBuilder(context, MediaDatabase::class.java, "media.db")
        .fallbackToDestructiveMigration()
        .build()
    }
  }
}
