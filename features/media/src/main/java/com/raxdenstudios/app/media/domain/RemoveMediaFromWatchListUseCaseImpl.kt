package com.raxdenstudios.app.media.domain

import com.raxdenstudios.app.media.data.repository.MediaRepository
import com.raxdenstudios.commons.ResultData

internal class RemoveMediaFromWatchListUseCaseImpl(
  private val mediaRepository: MediaRepository
) : RemoveMediaFromWatchListUseCase {

  override suspend fun execute(params: RemoveMediaFromWatchListUseCase.Params): ResultData<Boolean> =
    mediaRepository.removeMediaFromWatchList(params.movieId, params.mediaType)
}
