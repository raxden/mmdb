package com.raxdenstudios.app.movie.domain

import com.raxdenstudios.app.movie.data.repository.MediaRepository
import com.raxdenstudios.app.movie.domain.model.Media
import com.raxdenstudios.commons.ResultData
import com.raxdenstudios.commons.pagination.model.PageList

internal class GetMediasUseCaseImpl(
  private val mediaRepository: MediaRepository
) : GetMediasUseCase {

  override suspend fun execute(params: GetMediasUseCase.Params): ResultData<PageList<Media>> =
    mediaRepository.medias(params.mediaFilter, params.page, params.pageSize)
}