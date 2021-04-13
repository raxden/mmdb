package com.raxdenstudios.app.movie.data.local

import android.content.Context
import androidx.annotation.VisibleForTesting
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.raxdenstudios.app.movie.data.local.model.MovieEntity

@Database(
  entities = [MovieEntity::class],
  version = 2,
)
abstract class MovieDatabase : RoomDatabase() {

  abstract fun watchListDao(): MovieDao

  companion object {

    @Volatile
    private var INSTANCE: MovieDatabase? = null

    fun getInstance(context: Context): MovieDatabase {
      return INSTANCE ?: synchronized(this) {
        INSTANCE ?: buildDatabase(context).also { INSTANCE = it }
      }
    }

    @VisibleForTesting
    fun switchToInMemory(context: Context): MovieDatabase {
      return Room.inMemoryDatabaseBuilder(context, MovieDatabase::class.java)
        // allowing main thread queries, just for testing
        .allowMainThreadQueries()
        .build()
    }

    private fun buildDatabase(context: Context): MovieDatabase {
      return Room.databaseBuilder(context, MovieDatabase::class.java, "movie.db")
        .fallbackToDestructiveMigration()
        .build()
    }
  }
}
