package com.raxdenstudios.app.media.data.local.datasource

import com.raxdenstudios.app.media.data.local.MediaDao
import com.raxdenstudios.app.media.data.local.WatchListDao
import com.raxdenstudios.app.media.data.local.mapper.MediaEntityToDomainMapper
import com.raxdenstudios.app.media.data.local.mapper.MediaToEntityMapper
import com.raxdenstudios.app.media.data.local.model.WatchListEntity
import com.raxdenstudios.app.media.domain.model.Media

internal class MediaLocalDataSource(
  private val mediaDao: MediaDao,
  private val watchListDao: WatchListDao,
  private val mediaToEntityMapper: MediaToEntityMapper,
  private val mediaEntityToDomainMapper: MediaEntityToDomainMapper,
) {

  suspend fun addToWatchList(media: Media) {
    watchListDao.insert(WatchListEntity(media.id))
  }

  suspend fun removeFromWatchList(mediaId: Long) {
    watchListDao.remove(mediaId = mediaId)
  }

  suspend fun containsInWatchList(mediaId: Long): Boolean =
    watchListDao.find(mediaId) != null

  suspend fun insert(media: List<Media>) {
    val entityList = mediaToEntityMapper.transform(media)
    mediaDao.insert(entityList)
  }

  suspend fun insert(media: Media) {
    val entity = mediaToEntityMapper.transform(media)
    mediaDao.insert(entity)
  }
}