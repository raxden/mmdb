package com.raxdenstudios.app.core.network.gateway

import com.raxdenstudios.app.core.network.model.MediaDto
import com.raxdenstudios.app.core.network.model.WatchlistDto
import com.raxdenstudios.app.core.network.service.MediaV3Service
import com.raxdenstudios.app.core.network.service.MediaV4Service
import com.raxdenstudios.app.core.model.MediaId
import com.raxdenstudios.app.core.model.MediaType
import com.raxdenstudios.app.core.network.model.PageDto
import com.raxdenstudios.commons.DispatcherProvider
import com.raxdenstudios.commons.ResultData
import com.raxdenstudios.commons.ext.coMap
import com.raxdenstudios.commons.ext.getValueOrNull
import com.raxdenstudios.commons.pagination.model.Page
import com.raxdenstudios.commons.retrofit.toResultData
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import javax.inject.Inject

class WatchlistGateway @Inject constructor(
    private val dispatcher: DispatcherProvider,
    private val mediaV3Service: MediaV3Service,
    private val mediaV4Service: MediaV4Service,
) {

    companion object {

        private val FIRST_PAGE = Page(1)
    }

    suspend fun fetchAll(
        mediaType: MediaType,
        accountId: String
    ): ResultData<List<MediaDto>> =
        fetchByPage(mediaType, FIRST_PAGE, accountId)
            .coMap { pageDto ->
                withContext(dispatcher.io) {
                    val allMedias = pageDto.results.toMutableList()
                    val totalPages = pageDto.total_pages
                    val movies = (FIRST_PAGE.value + 1..totalPages)
                        .map { page -> async { fetchByPage(mediaType, Page(page), accountId) } }
                        .mapNotNull { deferred -> deferred.await().getValueOrNull() }
                        .map { resultData -> resultData.results }
                        .flatten()
                    allMedias.addAll(movies)
                    allMedias
                }
            }

    suspend fun fetchByPage(
        mediaType: MediaType,
        page: Page,
        accountId: String,
    ): ResultData<PageDto<MediaDto>> = when (mediaType) {
        MediaType.Movie -> mediaV4Service.watchListMovies(accountId, page.value)
            .toResultData("Error occurred during fetching watch list movies")
        MediaType.TvShow -> mediaV4Service.watchListTVShows(accountId, page.value)
            .toResultData("Error occurred during fetching watch list tv shows")
    }

    suspend fun add(
        mediaId: MediaId,
        mediaType: String,
        accountId: String,
    ): ResultData<Boolean> =
        mediaV3Service.watchList(
            accountId,
            WatchlistDto.Request.Add(mediaId.value, mediaType)
        ).toResultData("Error occurred during adding movie to watch list") { true }

    suspend fun remove(
        mediaId: MediaId,
        mediaType: String,
        accountId: String,
    ): ResultData<Boolean> =
        mediaV3Service.watchList(
            accountId,
            WatchlistDto.Request.Remove(mediaId.value, mediaType)
        ).toResultData("Error occurred during adding movie to watch list") { true }
}
