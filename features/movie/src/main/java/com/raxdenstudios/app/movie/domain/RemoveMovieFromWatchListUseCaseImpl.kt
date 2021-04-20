package com.raxdenstudios.app.movie.domain

import com.raxdenstudios.app.movie.data.repository.MovieRepository
import com.raxdenstudios.commons.ResultData

internal class RemoveMovieFromWatchListUseCaseImpl(
  private val movieRepository: MovieRepository
) : RemoveMovieFromWatchListUseCase {

  override suspend fun execute(params: RemoveMovieFromWatchListUseCase.Params): ResultData<Boolean> =
    movieRepository.removeMovieFromWatchList(params.movieId, params.mediaType)
}
