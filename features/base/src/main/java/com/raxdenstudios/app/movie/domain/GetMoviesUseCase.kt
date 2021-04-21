package com.raxdenstudios.app.movie.domain

import com.raxdenstudios.app.movie.domain.model.Media
import com.raxdenstudios.app.movie.domain.model.MediaFilter
import com.raxdenstudios.commons.ResultData
import com.raxdenstudios.commons.pagination.model.Page
import com.raxdenstudios.commons.pagination.model.PageList
import com.raxdenstudios.commons.pagination.model.PageSize

interface GetMoviesUseCase {
  suspend fun execute(params: Params): ResultData<PageList<Media>>

  data class Params(
    val mediaFilter: MediaFilter,
    val page: Page = Page(1),
    val pageSize: PageSize = PageSize.defaultSize
  )
}