package com.raxdenstudios.app.movie.data.local.model

import androidx.annotation.VisibleForTesting
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "watchlist")
data class MovieWatchListEntity(
  @PrimaryKey @ColumnInfo(name = "movie_id") val movieId: Long,
) {

  companion object {
    @VisibleForTesting
    val empty = MovieWatchListEntity(
      movieId = 1L
    )
  }
}