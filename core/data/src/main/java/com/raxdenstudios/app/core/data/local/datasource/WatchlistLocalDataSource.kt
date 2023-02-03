package com.raxdenstudios.app.core.data.local.datasource

import androidx.room.Transaction
import com.raxdenstudios.app.core.data.local.mapper.MediaEntityToDomainMapper
import com.raxdenstudios.app.core.data.local.mapper.MediaToEntityMapper
import com.raxdenstudios.app.core.data.local.mapper.MediaToWatchlistEntityMapper
import com.raxdenstudios.app.core.database.MediaDatabase
import com.raxdenstudios.app.core.database.dao.MediaDao
import com.raxdenstudios.app.core.database.dao.WatchlistDao
import com.raxdenstudios.app.core.model.Media
import com.raxdenstudios.app.core.model.MediaId
import com.raxdenstudios.app.core.model.MediaType
import com.raxdenstudios.commons.ResultData
import com.raxdenstudios.commons.ext.runCatching
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class WatchlistLocalDataSource @Inject constructor(
    private val database: MediaDatabase,
    private val mediaToEntityMapper: MediaToEntityMapper,
    private val mediaEntityToDomainMapper: MediaEntityToDomainMapper,
    private val mediaToWatchlistEntityMapper: MediaToWatchlistEntityMapper,
) {

    private val mediaDao: MediaDao
        get() = database.mediaDao()

    private val watchListDao: WatchlistDao
        get() = database.watchlistDao()

    fun observe(): Flow<ResultData<List<Media>>> =
        mediaDao.observe()
            .map { entityList -> mediaEntityToDomainMapper.transform(entityList) }
            .map { medias -> medias.map { media -> media.copyWith(watchList = true) } }
            .map { medias -> ResultData.Success(medias) }

    fun observe(mediaType: MediaType): Flow<ResultData<List<Media>>> =
        mediaDao.observe(mediaType.value)
            .map { entityList -> mediaEntityToDomainMapper.transform(entityList) }
            .map { medias -> medias.map { media -> media.copyWith(watchList = true) } }
            .map { medias -> ResultData.Success(medias) }

    fun observe(id: MediaId): Flow<ResultData<Media>> =
        mediaDao.observe(id.value)
            .map { entity -> mediaEntityToDomainMapper.transform(entity) }
            .map { media -> media.copyWith(watchList = true) }
            .map { media -> ResultData.Success(media) }

    suspend fun list(mediaType: MediaType): ResultData<List<Media>> =
        observe(mediaType).first()

    suspend fun list(): ResultData<List<Media>> =
        observe().first()

    @Transaction
    suspend fun add(media: Media): ResultData<Boolean> = runCatching {
        mediaDao.insert(mediaToEntityMapper.transform(media))
        watchListDao.insert(mediaToWatchlistEntityMapper.transform(media))
        true
    }

    @Transaction
    suspend fun add(medias: List<Media>): ResultData<Boolean> = runCatching {
        mediaDao.insert(mediaToEntityMapper.transform(medias))
        watchListDao.insert(mediaToWatchlistEntityMapper.transform(medias))
        true
    }

    @Transaction
    suspend fun init(medias: List<Media>): ResultData<Boolean> = runCatching {
        watchListDao.clear()
        mediaDao.insert(mediaToEntityMapper.transform(medias))
        watchListDao.insert(mediaToWatchlistEntityMapper.transform(medias))
        true
    }

    suspend fun clear(): ResultData<Boolean> = runCatching {
        watchListDao.clear()
        true
    }

    suspend fun remove(mediaId: MediaId): ResultData<Boolean> = runCatching {
        watchListDao.delete(mediaId.value)
        true
    }

    suspend fun contains(mediaId: MediaId): Boolean =
        watchListDao.find(mediaId.value) != null
}
