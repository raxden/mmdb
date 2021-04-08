package com.raxdenstudios.app.movie.domain

import com.raxdenstudios.app.movie.data.repository.MovieRepository
import com.raxdenstudios.commons.ResultData

internal class AddMovieToWatchListImpl(
  private val movieRepository: MovieRepository
) : AddMovieToWatchList {

  override suspend fun execute(movieId: Long): ResultData<Boolean> =
    movieRepository.addMovieToWatchList(movieId)
}
