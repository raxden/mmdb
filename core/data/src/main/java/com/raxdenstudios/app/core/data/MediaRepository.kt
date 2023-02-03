package com.raxdenstudios.app.core.data

import com.raxdenstudios.app.core.model.Media
import com.raxdenstudios.app.core.model.MediaFilter
import com.raxdenstudios.app.core.model.MediaId
import com.raxdenstudios.app.core.model.MediaType
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
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.flow.flow
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
    ): ResultData<PageList<Media>> = withContext(dispatcher.io) {
        val watchlistDeferred = async { watchlistDataSource.fetch() }
        val pageListDeferred = async { mediaDataSource.fetch(mediaFilter, page, pageSize) }

        val watchlist = watchlistDeferred.await().getValueOrDefault(emptyList())
        val medias = pageListDeferred.await()

        medias.map { pageList -> pageList.markMediasAsWatched(watchlist) }
    }

    suspend fun fetchById(
        mediaId: MediaId,
        mediaType: MediaType,
    ): Flow<ResultData<Media>> = flow {
        val watchlistMedia = watchlistDataSource.observe(mediaId, mediaType).firstOrNull()?.getValueOrNull()
        val result = mediaDataSource.fetchById(mediaId, mediaType).map { media ->
            media.copyWith(watchList = watchlistMedia != null)
        }
        emit(result)
    }

    private fun PageList<Media>.markMediasAsWatched(watchlist: List<Media>): PageList<Media> =
        map { medias ->
            medias.map { media ->
                media.copyWith(watchList = watchlist.any { it.id == media.id })
            }
        }

    fun observeWatchlist(): Flow<ResultData<List<Media>>> =
        watchlistDataSource.observe()

    fun observeWatchlist(mediaType: MediaType) =
        watchlistDataSource.observe(mediaType)

    suspend fun watchlist(mediaType: MediaType): ResultData<List<Media>> =
        watchlistDataSource.fetch(mediaType)

    suspend fun addToWatchlist(
        mediaId: MediaId,
        mediaType: MediaType
    ): ResultData<Media> = watchlistDataSource.add(mediaId, mediaType)

    suspend fun removeFromWatchlist(
        mediaId: MediaId,
        mediaType: MediaType
    ): ResultData<Boolean> = watchlistDataSource.remove(mediaId, mediaType)
}
