package com.raxdenstudios.app.list.view.mapper

import com.raxdenstudios.app.list.view.model.MediaListParams
import com.raxdenstudios.app.media.domain.GetMediasUseCase
import com.raxdenstudios.app.media.domain.model.MediaFilter
import com.raxdenstudios.commons.pagination.model.Page
import com.raxdenstudios.commons.pagination.model.PageSize
import javax.inject.Inject

internal class GetMediasUseCaseParamsMapper @Inject constructor() {

  fun transform(source: MediaListParams, page: Page, pageSize: PageSize): GetMediasUseCase.Params {
    val mediaFilter = when (source) {
      is MediaListParams.NowPlaying -> MediaFilter.NowPlaying
      is MediaListParams.Popular -> MediaFilter.Popular(source.mediaType)
      is MediaListParams.TopRated -> MediaFilter.TopRated(source.mediaType)
      is MediaListParams.Upcoming -> MediaFilter.Upcoming
      is MediaListParams.WatchList -> MediaFilter.WatchList(source.mediaType)
    }
    return GetMediasUseCase.Params(mediaFilter, page, pageSize)
  }
}
