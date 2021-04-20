package com.raxdenstudios.app.movie.domain

import com.raxdenstudios.app.movie.data.repository.MovieRepository
import com.raxdenstudios.commons.ResultData

internal class AddMovieToWatchListUseCaseImpl(
  private val movieRepository: MovieRepository
) : AddMovieToWatchListUseCase {

  override suspend fun execute(params: AddMovieToWatchListUseCase.Params): ResultData<Boolean> =
    movieRepository.addMovieToWatchList(params.movieId, params.mediaType)
}
