package com.raxdenstudios.app.core.data

import com.raxdenstudios.app.core.data.local.datasource.AccountLocalDataSource
import com.raxdenstudios.app.core.data.local.datasource.WatchlistLocalDataSource
import com.raxdenstudios.app.core.data.remote.datasource.MediaRemoteDataSource
import com.raxdenstudios.app.core.model.Media
import com.raxdenstudios.app.core.model.MediaId
import com.raxdenstudios.app.core.model.MediaType
import com.raxdenstudios.commons.ResultData
import com.raxdenstudios.commons.ext.coFlatMap
import com.raxdenstudios.commons.ext.getValueOrDefault
import com.raxdenstudios.commons.ext.map
import com.raxdenstudios.commons.ext.onCoSuccess
import com.raxdenstudios.core.model.Account
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

@Suppress("TooManyFunctions")
class WatchlistDataSource @Inject constructor(
    private val watchListLocalDataSource: WatchlistLocalDataSource,
    private val mediaRemoteDataSource: MediaRemoteDataSource,
    private val accountLocalDataSource: AccountLocalDataSource,
) {

    fun observe(): Flow<ResultData<List<Media>>> = flow {
        emitAll(watchListLocalDataSource.observe())
        updateLocalWatchlistFromRemoteIfNecessary()
    }.distinctUntilChanged()

    fun observe(mediaType: MediaType) = flow {
        emitAll(watchListLocalDataSource.observe(mediaType))
        updateLocalWatchlistFromRemoteIfNecessary(mediaType)
    }.distinctUntilChanged()

    suspend fun fetch(): ResultData<List<Media>> {
        updateLocalWatchlistFromRemoteIfNecessary()
        return watchListLocalDataSource.list()
    }

    suspend fun fetch(mediaType: MediaType): ResultData<List<Media>> {
        updateLocalWatchlistFromRemoteIfNecessary(mediaType)
        return watchListLocalDataSource.list(mediaType)
    }

    suspend fun add(
        mediaId: MediaId,
        mediaType: MediaType
    ): ResultData<Media> = when (val account = accountLocalDataSource.getAccount()) {
        is Account.Guest -> addToLocal(mediaId, mediaType)
        is Account.Logged -> add(mediaId, mediaType, account)
    }

    suspend fun remove(
        mediaId: MediaId,
        mediaType: MediaType
    ): ResultData<Boolean> = when (val account = accountLocalDataSource.getAccount()) {
        is Account.Guest -> removeFromLocal(mediaId)
        is Account.Logged -> remove(mediaId, mediaType, account)
    }

    private suspend fun updateLocalWatchlistFromRemoteIfNecessary() {
        updateLocalWatchlistFromRemoteIfNecessary(MediaType.Movie)
        updateLocalWatchlistFromRemoteIfNecessary(MediaType.TvShow)
    }

    private suspend fun updateLocalWatchlistFromRemoteIfNecessary(mediaType: MediaType) {
        val currentList = watchListLocalDataSource.list(mediaType).getValueOrDefault(emptyList())
        if (currentList.isEmpty()) {
            updateLocalWatchlistFromRemote(mediaType)
        }
    }

    private suspend fun updateLocalWatchlistFromRemote(
        mediaType: MediaType,
    ): ResultData<List<Media>> = when (val account = accountLocalDataSource.getAccount()) {
        is Account.Guest -> ResultData.Success(emptyList())
        is Account.Logged -> updateLocalWatchlistFromRemote(mediaType, account)
    }

    private suspend fun updateLocalWatchlistFromRemote(
        mediaType: MediaType,
        account: Account.Logged,
    ): ResultData<List<Media>> =
        mediaRemoteDataSource.fetchWatchlist(mediaType, account)
            .onCoSuccess { medias -> watchListLocalDataSource.init(medias) }

    private suspend fun add(
        mediaId: MediaId,
        mediaType: MediaType,
        account: Account.Logged,
    ): ResultData<Media> = mediaRemoteDataSource.addToWatchlist(mediaId, mediaType, account)
        .coFlatMap { addToLocal(mediaId, mediaType) }

    private suspend fun addToLocal(
        mediaId: MediaId,
        mediaType: MediaType,
    ): ResultData<Media> = mediaRemoteDataSource.fetchById(mediaId, mediaType)
        .map { media -> media.copyWith(watchList = true) }
        .onCoSuccess { media -> watchListLocalDataSource.add(media) }

    private suspend fun remove(
        mediaId: MediaId,
        mediaType: MediaType,
        account: Account.Logged,
    ): ResultData<Boolean> =
        mediaRemoteDataSource.removeFromWatchlist(mediaId, mediaType, account)
            .onCoSuccess { removeFromLocal(mediaId) }

    private suspend fun removeFromLocal(mediaId: MediaId): ResultData<Boolean> =
        watchListLocalDataSource.remove(mediaId)
}
