package com.raxdenstudios.app.core.domain

import com.raxdenstudios.app.core.data.MediaRepository
import com.raxdenstudios.app.core.model.Media
import com.raxdenstudios.app.core.model.MediaFilter
import com.raxdenstudios.commons.ResultData
import com.raxdenstudios.commons.pagination.model.Page
import com.raxdenstudios.commons.pagination.model.PageList
import com.raxdenstudios.commons.pagination.model.PageSize
import javax.inject.Inject

class GetMediasUseCase @Inject constructor(
    private val mediaRepository: MediaRepository,
) {

    suspend operator fun invoke(params: Params): ResultData<PageList<Media>> =
        mediaRepository.medias(params.mediaFilter, params.page, params.pageSize)

    data class Params(
        val mediaFilter: MediaFilter,
        val page: Page = Page(1),
        val pageSize: PageSize = PageSize.defaultSize,
    )
}
