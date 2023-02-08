package com.raxdenstudios.app.core.data

import com.raxdenstudios.app.core.model.ErrorDomain
import com.raxdenstudios.app.core.model.Media
import com.raxdenstudios.app.core.model.MediaFilter
import com.raxdenstudios.app.core.model.MediaId
import com.raxdenstudios.app.core.model.MediaType
import com.raxdenstudios.app.core.model.Video
import com.raxdenstudios.commons.DispatcherProvider
import com.raxdenstudios.commons.ResultData
import com.raxdenstudios.commons.ext.getValueOrDefault
import com.raxdenstudios.commons.ext.getValueOrNull
import com.raxdenstudios.commons.ext.map
import com.raxdenstudios.commons.pagination.model.Page
import com.raxdenstudios.commons.pagination.model.PageList
import com.raxdenstudios.commons.pagination.model.PageSize
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject

class MediaRepository @Inject constructor(
    private val dispatcher: DispatcherProvider,
    private val mediaDataSource: MediaDataSource,
    private val watchlistDataSource: WatchlistDataSource,
) {

    suspend fun medias(
        mediaFilter: MediaFilter,
        page: Page,
        pageSize: PageSize,
    ): ResultData<PageList<Media>, ErrorDomain> = withContext(dispatcher.io) {
        val watchlistDeferred = async { watchlistDataSource.observe().first() }
        val pageListDeferred = async { mediaDataSource.fetch(mediaFilter, page, pageSize) }

        val watchlist = watchlistDeferred.await().getValueOrDefault(emptyList())
        val medias = pageListDeferred.await()

        medias.map { pageList -> pageList.markMediasAsWatched(watchlist) }
    }

    suspend fun fetchById(
        mediaId: MediaId,
        mediaType: MediaType,
    ): Flow<ResultData<Media, ErrorDomain>> = flow {
        val mediaResult = mediaDataSource.fetchById(mediaId, mediaType)
        val flow = watchlistDataSource.observe(mediaId, mediaType)
            .map { result -> result.getValueOrNull() }
            .map { watchlistMedia ->
                mediaResult.map { media -> media.copyWith(watchList = watchlistMedia != null) }
            }
        emitAll(flow)
    }

    fun observeWatchlist(): Flow<ResultData<List<Media>, ErrorDomain>> =
        watchlistDataSource.observe()

    suspend fun addToWatchlist(
        mediaId: MediaId,
        mediaType: MediaType
    ): ResultData<Media, ErrorDomain> = watchlistDataSource.add(mediaId, mediaType)

    suspend fun removeFromWatchlist(
        mediaId: MediaId,
        mediaType: MediaType
    ): ResultData<Boolean, ErrorDomain> = watchlistDataSource.remove(mediaId, mediaType)

    suspend fun videos(
        mediaId: MediaId,
        mediaType: MediaType,
    ): ResultData<List<Video>, ErrorDomain> =
        mediaDataSource.videos(mediaId, mediaType)

    private fun PageList<Media>.markMediasAsWatched(watchlist: List<Media>): PageList<Media> =
        map { medias ->
            medias.map { media ->
                media.copyWith(watchList = watchlist.any { it.id == media.id })
            }
        }
}
