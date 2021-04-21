package com.raxdenstudios.app.movie.domain

import com.raxdenstudios.app.movie.data.repository.MediaRepository
import com.raxdenstudios.commons.ResultData

internal class RemoveMediaFromWatchListUseCaseImpl(
  private val mediaRepository: MediaRepository
) : RemoveMediaFromWatchListUseCase {

  override suspend fun execute(params: RemoveMediaFromWatchListUseCase.Params): ResultData<Boolean> =
    mediaRepository.removeMediaFromWatchList(params.movieId, params.mediaType)
}
