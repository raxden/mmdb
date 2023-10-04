package com.raxdenstudios.app.core.data

import com.raxdenstudios.app.core.data.local.datasource.AccountLocalDataSource
import com.raxdenstudios.app.core.data.local.datasource.WatchlistLocalDataSource
import com.raxdenstudios.app.core.data.remote.datasource.MediaRemoteDataSource
import com.raxdenstudios.app.core.model.ErrorDomain
import com.raxdenstudios.app.core.model.Media
import com.raxdenstudios.app.core.model.MediaId
import com.raxdenstudios.app.core.model.MediaType
import com.raxdenstudios.commons.core.ResultData
import com.raxdenstudios.commons.core.ext.coFlatMap
import com.raxdenstudios.commons.core.ext.getValueOrDefault
import com.raxdenstudios.commons.core.ext.map
import com.raxdenstudios.commons.core.ext.onCoSuccess
import com.raxdenstudios.core.model.Account
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

interface WatchlistDataSource {

    fun observe(): Flow<ResultData<List<Media>, ErrorDomain>>
    fun observe(mediaType: MediaType): Flow<ResultData<List<Media>, ErrorDomain>>
    fun observe(mediaId: MediaId, mediaType: MediaType): Flow<ResultData<Media, ErrorDomain>>
    suspend fun add(mediaId: MediaId, mediaType: MediaType): ResultData<Media, ErrorDomain>
    suspend fun remove(mediaId: MediaId, mediaType: MediaType): ResultData<Boolean, ErrorDomain>
}

@Suppress("TooManyFunctions")
class WatchlistDataSourceImpl @Inject constructor(
    private val watchListLocalDataSource: WatchlistLocalDataSource,
    private val mediaRemoteDataSource: MediaRemoteDataSource,
    private val accountLocalDataSource: AccountLocalDataSource,
) : WatchlistDataSource {

    override fun observe(): Flow<ResultData<List<Media>, ErrorDomain>> = flow {
        emitAll(watchListLocalDataSource.observe())
        updateLocalWatchlistFromRemoteIfNecessary()
    }.distinctUntilChanged()

    override fun observe(mediaType: MediaType): Flow<ResultData<List<Media>, ErrorDomain>> = flow {
        emitAll(watchListLocalDataSource.observe(mediaType))
        updateLocalWatchlistFromRemoteIfNecessary(mediaType)
    }.distinctUntilChanged()

    override fun observe(mediaId: MediaId, mediaType: MediaType): Flow<ResultData<Media, ErrorDomain>> =
        watchListLocalDataSource.observe(mediaId)

    override suspend fun add(
        mediaId: MediaId,
        mediaType: MediaType
    ): ResultData<Media, ErrorDomain> = when (val account = accountLocalDataSource.getAccount()) {
        is Account.Guest -> addToLocal(mediaId, mediaType)
        is Account.Logged -> add(mediaId, mediaType, account)
    }

    override suspend fun remove(
        mediaId: MediaId,
        mediaType: MediaType
    ): ResultData<Boolean, ErrorDomain> = when (val account = accountLocalDataSource.getAccount()) {
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
    ): ResultData<List<Media>, ErrorDomain> = when (val account = accountLocalDataSource.getAccount()) {
        is Account.Guest -> ResultData.Success(emptyList())
        is Account.Logged -> updateLocalWatchlistFromRemote(mediaType, account)
    }

    private suspend fun updateLocalWatchlistFromRemote(
        mediaType: MediaType,
        account: Account.Logged,
    ): ResultData<List<Media>, ErrorDomain> =
        mediaRemoteDataSource.fetchWatchlist(mediaType, account)
            .onCoSuccess { medias -> watchListLocalDataSource.init(medias) }

    private suspend fun add(
        mediaId: MediaId,
        mediaType: MediaType,
        account: Account.Logged,
    ): ResultData<Media, ErrorDomain> = mediaRemoteDataSource.addToWatchlist(mediaId, mediaType, account)
        .coFlatMap { addToLocal(mediaId, mediaType) }

    private suspend fun addToLocal(
        mediaId: MediaId,
        mediaType: MediaType,
    ): ResultData<Media, ErrorDomain> = mediaRemoteDataSource.fetchById(mediaId, mediaType)
        .map { media -> media.copyWith(watchList = true) }
        .onCoSuccess { media -> watchListLocalDataSource.add(media) }

    private suspend fun remove(
        mediaId: MediaId,
        mediaType: MediaType,
        account: Account.Logged,
    ): ResultData<Boolean, ErrorDomain> =
        mediaRemoteDataSource.removeFromWatchlist(mediaId, mediaType, account)
            .onCoSuccess { removeFromLocal(mediaId) }

    private suspend fun removeFromLocal(mediaId: MediaId): ResultData<Boolean, ErrorDomain> =
        watchListLocalDataSource.remove(mediaId)
}
