package com.raxdenstudios.app.media.domain

import com.raxdenstudios.app.media.domain.model.Media
import com.raxdenstudios.app.media.domain.model.MediaFilter
import com.raxdenstudios.commons.ResultData
import com.raxdenstudios.commons.pagination.model.Page
import com.raxdenstudios.commons.pagination.model.PageList
import com.raxdenstudios.commons.pagination.model.PageSize

interface GetMediasUseCase {
  suspend operator fun invoke(params: Params): ResultData<PageList<Media>>

  data class Params(
    val mediaFilter: MediaFilter,
    val page: Page = Page(1),
    val pageSize: PageSize = PageSize.defaultSize
  )
}
