package com.raxdenstudios.app.core.data

import com.raxdenstudios.app.core.model.ErrorDomain
import com.raxdenstudios.app.core.model.Media
import com.raxdenstudios.app.core.model.MediaFilter
import com.raxdenstudios.app.core.model.MediaId
import com.raxdenstudios.app.core.model.MediaType
import com.raxdenstudios.app.core.model.Video
import com.raxdenstudios.commons.coroutines.DispatcherProvider
import com.raxdenstudios.commons.core.ResultData
import com.raxdenstudios.commons.core.ext.getValueOrDefault
import com.raxdenstudios.commons.core.ext.getValueOrNull
import com.raxdenstudios.commons.core.ext.map
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

class MediaRepositoryImpl @Inject constructor(
    private val dispatcher: DispatcherProvider,
    private val mediaDataSource: MediaDataSource,
    private val watchlistDataSource: WatchlistDataSource,
) : MediaRepository {

    override suspend fun medias(
        mediaFilter: MediaFilter,
        page: Page,
        pageSize: PageSize,
    ): ResultData<PageList<Media>, ErrorDomain> = withContext(dispatcher.io) {
        val watchlistDeferred = async { watchlistDataSource.observe().first() }
        val pageListDeferred = async { mediaDataSource.fetch(mediaFilter, page, pageSize) }

        val watchlist = watchlistDeferred.await().getValueOrDefault(emptyList())
        val result = pageListDeferred.await()

        result.map { pageList -> pageList.markMediasAsWatched(watchlist) }
    }

    override fun fetchById(
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

    override fun observeWatchlist(): Flow<ResultData<List<Media>, ErrorDomain>> =
        watchlistDataSource.observe()

    override suspend fun addToWatchlist(
        mediaId: MediaId,
        mediaType: MediaType
    ): ResultData<Media, ErrorDomain> = watchlistDataSource.add(mediaId, mediaType)

    override suspend fun removeFromWatchlist(
        mediaId: MediaId,
        mediaType: MediaType
    ): ResultData<Boolean, ErrorDomain> = watchlistDataSource.remove(mediaId, mediaType)

    override suspend fun videos(
        mediaId: MediaId,
        mediaType: MediaType,
    ): ResultData<List<Video>, ErrorDomain> =
        mediaDataSource.videos(mediaId, mediaType)

    override suspend fun related(
        mediaId: MediaId,
        mediaType: MediaType,
        page: Page,
        pageSize: PageSize,
    ): ResultData<PageList<Media>, ErrorDomain> = withContext(dispatcher.io) {
        val watchlistDeferred = async { watchlistDataSource.observe().first() }
        val pageListDeferred = async { mediaDataSource.related(mediaId, mediaType, page, pageSize) }

        val watchlist = watchlistDeferred.await().getValueOrDefault(emptyList())
        val medias = pageListDeferred.await()

        medias.map { pageList -> pageList.markMediasAsWatched(watchlist) }
    }

    override suspend fun search(
        query: String,
        page: Page,
        pageSize: PageSize
    ): ResultData<PageList<Media>, ErrorDomain> = withContext(dispatcher.io) {
        val watchlistDeferred = async { watchlistDataSource.observe().first() }
        val pageListDeferred = async { mediaDataSource.search(query, page, pageSize) }

        val watchlist = watchlistDeferred.await().getValueOrDefault(emptyList())
        val result = pageListDeferred.await()

        result.map { pageList -> pageList.markMediasAsWatched(watchlist) }
    }

    private fun PageList<Media>.markMediasAsWatched(watchlist: List<Media>): PageList<Media> =
        map { medias ->
            medias.map { media ->
                media.copyWith(watchList = watchlist.any { it.id == media.id })
            }
        }
}
