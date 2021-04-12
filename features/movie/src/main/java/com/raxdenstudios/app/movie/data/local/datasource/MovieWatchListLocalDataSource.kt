package com.raxdenstudios.app.movie.data.local.datasource

import com.raxdenstudios.app.movie.data.local.MovieWatchListDao
import com.raxdenstudios.app.movie.data.local.model.MovieWatchListEntity

internal class MovieWatchListLocalDataSource(
  private val dao: MovieWatchListDao,
) {

  suspend fun insert(movies: List<MovieWatchListEntity>) =
    dao.insert(movies)

  suspend fun insert(movie: MovieWatchListEntity) =
    dao.insert(movie)

  suspend fun contains(movieId: Long): Boolean =
    dao.find(movieId) != null
}