package com.raxdenstudios.app.core.domain

import com.raxdenstudios.app.core.data.MediaRepository
import com.raxdenstudios.app.core.model.ErrorDomain
import com.raxdenstudios.app.core.model.Media
import com.raxdenstudios.app.core.model.MediaId
import com.raxdenstudios.app.core.model.MediaType
import com.raxdenstudios.commons.ResultData
import com.raxdenstudios.commons.ext.getValueOrDefault
import com.raxdenstudios.commons.ext.map
import com.raxdenstudios.commons.pagination.model.Page
import com.raxdenstudios.commons.pagination.model.PageList
import com.raxdenstudios.commons.pagination.model.PageSize
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flowOf
import javax.inject.Inject

@OptIn(FlowPreview::class)
class GetRelatedMediasUseCase @Inject constructor(
    private val mediaRepository: MediaRepository,
) {

    suspend operator fun invoke(
        params: Params,
    ): Flow<ResultData<PageList<Media>, ErrorDomain>> {
        val mediasResult = mediaRepository.related(params.mediaId, params.mediaType, params.page, params.pageSize)
        val observeWatchlist = mediaRepository.observeWatchlist()

        return observeWatchlist.flatMapConcat { resultData ->
            val watchlist = resultData.getValueOrDefault(emptyList())
            val result = mediasResult.map { pageList -> pageList.markMediasAsWatched(watchlist) }
            flowOf(result)
        }
    }

    private fun PageList<Media>.markMediasAsWatched(watchlist: List<Media>): PageList<Media> =
        map { medias ->
            medias.map { media ->
                media.copyWith(watchList = watchlist.any { it.id == media.id })
            }
        }

    data class Params(
        val mediaId: MediaId,
        val mediaType: MediaType,
        val page: Page = firstPage,
        val pageSize: PageSize = defaultPageSize,
    )

    companion object {

        private val firstPage = Page(1)
        private val defaultPageSize = PageSize(20)
    }
}
