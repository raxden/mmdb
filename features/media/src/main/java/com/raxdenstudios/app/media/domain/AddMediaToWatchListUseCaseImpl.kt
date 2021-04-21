package com.raxdenstudios.app.media.domain

import com.raxdenstudios.app.media.data.repository.MediaRepository
import com.raxdenstudios.commons.ResultData

internal class AddMediaToWatchListUseCaseImpl(
  private val mediaRepository: MediaRepository
) : AddMediaToWatchListUseCase {

  override suspend fun execute(params: AddMediaToWatchListUseCase.Params): ResultData<Boolean> =
    mediaRepository.addMediaToWatchList(params.movieId, params.mediaType)
}
