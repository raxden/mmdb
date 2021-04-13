package com.raxdenstudios.app.movie.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.raxdenstudios.app.movie.data.local.model.MovieEntity

@Dao
interface MovieDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(data: List<MovieEntity>)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(movie: MovieEntity)

  @Query("SELECT * FROM movie WHERE watch_list = 1")
  suspend fun watchList(): List<MovieEntity>

  @Query("SELECT * FROM movie WHERE id == :movieId")
  suspend fun find(movieId: Long): MovieEntity?
}