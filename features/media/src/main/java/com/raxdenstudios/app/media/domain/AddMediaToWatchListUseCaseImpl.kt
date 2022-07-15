package com.raxdenstudios.app.media.domain

import com.raxdenstudios.app.media.data.repository.MediaRepository
import com.raxdenstudios.app.media.domain.model.Media
import com.raxdenstudios.commons.ResultData
import javax.inject.Inject

internal class AddMediaToWatchListUseCaseImpl @Inject constructor(
  private val mediaRepository: MediaRepository
) : AddMediaToWatchListUseCase {

  override suspend fun invoke(params: AddMediaToWatchListUseCase.Params): ResultData<Media> =
    mediaRepository.addToWatchList(params.mediaId, params.mediaType)
}
