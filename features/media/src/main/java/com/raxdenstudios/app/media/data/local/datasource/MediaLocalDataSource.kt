package com.raxdenstudios.app.media.data.local.datasource

import com.raxdenstudios.app.media.data.local.WatchListDao
import com.raxdenstudios.app.media.data.local.model.WatchListEntity
import com.raxdenstudios.app.media.domain.model.Media

internal class MediaLocalDataSource(
  private val watchListDao: WatchListDao
) {

  suspend fun addToWatchList(mediaList: List<Media>) {
    val entityList = mediaList.map { media -> WatchListEntity(media.id) }
    watchListDao.insert(entityList)
  }

  suspend fun addToWatchList(media: Media) {
    watchListDao.insert(WatchListEntity(media.id))
  }

  suspend fun removeFromWatchList(mediaId: Long) {
    watchListDao.remove(mediaId = mediaId)
  }

  suspend fun containsInWatchList(mediaId: Long): Boolean =
    watchListDao.find(mediaId) != null
}