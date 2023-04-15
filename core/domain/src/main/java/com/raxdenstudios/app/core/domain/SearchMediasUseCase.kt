package com.raxdenstudios.app.core.domain

import com.raxdenstudios.app.core.data.MediaRepository
import com.raxdenstudios.app.core.data.RecentSearchRepository
import com.raxdenstudios.app.core.model.ErrorDomain
import com.raxdenstudios.app.core.model.Media
import com.raxdenstudios.commons.ResultData
import com.raxdenstudios.commons.ext.getValueOrDefault
import com.raxdenstudios.commons.ext.map
import com.raxdenstudios.commons.ext.onCoSuccess
import com.raxdenstudios.commons.pagination.model.Page
import com.raxdenstudios.commons.pagination.model.PageList
import com.raxdenstudios.commons.pagination.model.PageSize
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class SearchMediasUseCase @Inject constructor(
    private val mediaRepository: MediaRepository,
    private val recentSearchRepository: RecentSearchRepository,
) {

    operator fun invoke(params: Params): Flow<ResultData<PageList<Media>, ErrorDomain>> = flow {
        val searchResult = mediaRepository.search(params.query, params.page, params.pageSize)
            .onCoSuccess { recentSearchRepository.save(params.query) }
        val flow = mediaRepository.observeWatchlist()
            .map { result -> result.getValueOrDefault(emptyList()) }
            .map { watchlist -> watchlist.map { it.id } }
            .map { watchlistIds ->
                searchResult.map { pageList ->
                    pageList.map { medias ->
                        medias.map { media -> media.copyWith(watchList = watchlistIds.contains(media.id)) }
                    }
                }
            }
        emitAll(flow)
    }

    data class Params(
        val query: String,
        val page: Page = Page(1),
        val pageSize: PageSize = PageSize.defaultSize,
    )
}
