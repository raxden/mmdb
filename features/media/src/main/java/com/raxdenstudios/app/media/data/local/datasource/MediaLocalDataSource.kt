package com.raxdenstudios.app.media.data.local.datasource

import androidx.room.Transaction
import com.raxdenstudios.app.media.data.local.MediaDao
import com.raxdenstudios.app.media.data.local.WatchListDao
import com.raxdenstudios.app.media.data.local.mapper.MediaEntityToDomainMapper
import com.raxdenstudios.app.media.data.local.mapper.MediaToEntityMapper
import com.raxdenstudios.app.media.data.local.mapper.MediaToWatchListEntityMapper
import com.raxdenstudios.app.media.domain.model.Media
import com.raxdenstudios.app.media.domain.model.MediaType
import com.raxdenstudios.commons.ResultData
import com.raxdenstudios.commons.runCatching
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class MediaLocalDataSource(
  private val mediaDao: MediaDao,
  private val watchListDao: WatchListDao,
  private val mediaToEntityMapper: MediaToEntityMapper,
  private val mediaEntityToDomainMapper: MediaEntityToDomainMapper,
  private val mediaToWatchListEntityMapper: MediaToWatchListEntityMapper,
) {

  fun watchList(mediaType: MediaType): Flow<ResultData<List<Media>>> =
    mediaDao.watchList(mediaType.ordinal)
      .map { entityList -> mediaEntityToDomainMapper.transform(entityList) }
      .map { medias -> medias.map { media -> media.copyWith(watchList = true) } }
      .map { medias -> ResultData.Success(medias) }

  @Transaction
  suspend fun addToWatchList(media: Media): ResultData<Boolean> = runCatching {
    mediaDao.insert(mediaToEntityMapper.transform(media))
    watchListDao.insert(mediaToWatchListEntityMapper.transform(media))
    true
  }

  @Transaction
  suspend fun addToWatchList(medias: List<Media>): ResultData<Boolean> = runCatching {
    mediaDao.insert(mediaToEntityMapper.transform(medias))
    watchListDao.insert(mediaToWatchListEntityMapper.transform(medias))
    true
  }

  suspend fun clearWatchList(): ResultData<Boolean> = runCatching {
    watchListDao.clear()
    true
  }

  suspend fun removeFromWatchList(mediaId: Long) {
    watchListDao.delete(mediaId)
  }

  suspend fun containsInWatchList(mediaId: Long): Boolean =
    watchListDao.find(mediaId) != null
}