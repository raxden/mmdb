package com.raxdenstudios.app.media.domain

import com.raxdenstudios.app.media.data.repository.MediaRepository
import com.raxdenstudios.app.media.domain.model.Media
import com.raxdenstudios.commons.ResultData

internal class AddMediaToWatchListUseCaseImpl(
  private val mediaRepository: MediaRepository
) : AddMediaToWatchListUseCase {

  override suspend fun execute(params: AddMediaToWatchListUseCase.Params): ResultData<Media> =
    mediaRepository.addMediaToWatchList(params.mediaId, params.mediaType)
}
