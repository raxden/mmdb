package com.raxdenstudios.app.movie.domain

import com.raxdenstudios.app.movie.data.repository.MovieRepository
import com.raxdenstudios.commons.ResultData

internal class RemoveMovieFromWatchListImpl(
  private val movieRepository: MovieRepository
) : RemoveMovieFromWatchList {

  override suspend fun execute(movieId: Long): ResultData<Boolean> =
    movieRepository.removeMovieFromWatchList(movieId)
}
