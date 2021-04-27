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

internal class MediaLocalDataSource(
  private val mediaDao: MediaDao,
  private val watchListDao: WatchListDao,
  private val mediaToEntityMapper: MediaToEntityMapper,
  private val mediaEntityToDomainMapper: MediaEntityToDomainMapper,
  private val mediaToWatchListEntityMapper: MediaToWatchListEntityMapper,
) {

  suspend fun watchList(mediaType: MediaType): ResultData<List<Media>> =
    try {
      val entityList = mediaDao.watchList(mediaType.ordinal)
      ResultData.Success(mediaEntityToDomainMapper.transform(entityList))
    } catch (e: Exception) {
      ResultData.Error(e)
    }

  @Transaction
  suspend fun addToWatchList(media: Media): ResultData<Boolean> =
    try {
      mediaDao.insert(mediaToEntityMapper.transform(media))
      watchListDao.insert(mediaToWatchListEntityMapper.transform(media))
      ResultData.Success(true)
    } catch (e: Exception) {
      ResultData.Error(e)
    }

  @Transaction
  suspend fun addToWatchList(medias: List<Media>): ResultData<Boolean> {
    return try {
      mediaDao.insert(mediaToEntityMapper.transform(medias))
      watchListDao.insert(mediaToWatchListEntityMapper.transform(medias))
      ResultData.Success(true)
    } catch (e: Exception) {
      ResultData.Error(e)
    }
  }

  suspend fun removeFromWatchList(mediaId: Long) {
    watchListDao.delete(mediaId)
  }

  suspend fun containsInWatchList(mediaId: Long): Boolean =
    watchListDao.find(mediaId) != null
}