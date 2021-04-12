package com.raxdenstudios.app.movie.data.local.datasource

import com.raxdenstudios.app.movie.data.local.MovieWatchListDao
import com.raxdenstudios.app.movie.data.local.mapper.MovieToMovieWatchListDataMapper
import com.raxdenstudios.app.movie.domain.Movie

internal class MovieWatchListLocalDataSource(
  private val dao: MovieWatchListDao,
  private val movieToMovieWatchListDataMapper: MovieToMovieWatchListDataMapper
) {

  suspend fun insert(movies: List<Movie>) {
    val entityList = movieToMovieWatchListDataMapper.transform(movies)
    dao.insert(entityList)
  }

  suspend fun insert(movie: Movie) {
    val entity = movieToMovieWatchListDataMapper.transform(movie)
    dao.insert(entity)
  }

  suspend fun remove(movie: Movie) {
    val entity = movieToMovieWatchListDataMapper.transform(movie)
    dao.delete(entity.movieId)
  }

  suspend fun contains(movieId: Long): Boolean =
    dao.find(movieId) != null
}