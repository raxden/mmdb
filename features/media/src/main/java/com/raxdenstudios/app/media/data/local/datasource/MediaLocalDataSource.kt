package com.raxdenstudios.app.media.data.local.datasource

import com.raxdenstudios.app.media.data.local.WatchListDao
import com.raxdenstudios.app.media.data.local.model.WatchListEntity
import com.raxdenstudios.app.media.domain.model.Media
import com.raxdenstudios.commons.ResultData

internal class MediaLocalDataSource(
  private val watchListDao: WatchListDao
) {

  suspend fun addToWatchList(mediaList: List<Media>): ResultData<Boolean> {
    return try {
      val entityList = mediaList.map { media -> WatchListEntity(media.id) }
      watchListDao.insert(entityList)
      ResultData.Success(true)
    } catch (e: Exception) {
      ResultData.Error(e)
    }
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