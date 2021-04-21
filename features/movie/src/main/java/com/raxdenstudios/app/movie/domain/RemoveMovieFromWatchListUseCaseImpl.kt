package com.raxdenstudios.app.movie.domain

import com.raxdenstudios.app.movie.data.repository.MediaRepository
import com.raxdenstudios.commons.ResultData

internal class RemoveMovieFromWatchListUseCaseImpl(
  private val mediaRepository: MediaRepository
) : RemoveMovieFromWatchListUseCase {

  override suspend fun execute(params: RemoveMovieFromWatchListUseCase.Params): ResultData<Boolean> =
    mediaRepository.removeMediaFromWatchList(params.movieId, params.mediaType)
}
