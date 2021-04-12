package com.raxdenstudios.app.movie.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.raxdenstudios.app.movie.data.local.model.MovieWatchListEntity

@Dao
interface MovieWatchListDao {

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(data: List<MovieWatchListEntity>)

  @Insert(onConflict = OnConflictStrategy.REPLACE)
  suspend fun insert(movie: MovieWatchListEntity)

  @Query("SELECT * FROM watchlist WHERE movie_id == :movieId")
  suspend fun find(movieId: Long): MovieWatchListEntity?
}