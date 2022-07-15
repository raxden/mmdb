package com.raxdenstudios.app.media.domain

import com.raxdenstudios.app.media.data.repository.MediaRepository
import com.raxdenstudios.app.media.domain.model.Media
import com.raxdenstudios.commons.ResultData
import com.raxdenstudios.commons.pagination.model.PageList
import javax.inject.Inject

internal class GetMediasUseCaseImpl @Inject constructor(
  private val mediaRepository: MediaRepository
) : GetMediasUseCase {

  override suspend operator fun invoke(params: GetMediasUseCase.Params): ResultData<PageList<Media>> =
    mediaRepository.medias(params.mediaFilter, params.page, params.pageSize)
}
