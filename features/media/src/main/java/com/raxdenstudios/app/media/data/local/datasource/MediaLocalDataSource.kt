package com.raxdenstudios.app.media.data.local.datasource

import com.raxdenstudios.app.media.data.local.MediaDao
import com.raxdenstudios.app.media.data.local.WatchListDao
import com.raxdenstudios.app.media.data.local.mapper.MediaEntityToDomainMapper
import com.raxdenstudios.app.media.data.local.mapper.MediaToEntityMapper
import com.raxdenstudios.app.media.data.local.model.WatchListEntity
import com.raxdenstudios.app.media.domain.model.Media
import com.raxdenstudios.commons.pagination.model.Page
import com.raxdenstudios.commons.pagination.model.PageList
import com.raxdenstudios.commons.pagination.model.PageSize

internal class MediaLocalDataSource(
  private val mediaDao: MediaDao,
  private val watchListDao: WatchListDao,
  private val mediaToEntityMapper: MediaToEntityMapper,
  private val mediaEntityToDomainMapper: MediaEntityToDomainMapper,
) {

  suspend fun addToWatchList(media: Media) {
    watchListDao.insert(WatchListEntity(media.id))
  }

  suspend fun watchList(): List<Media> {
    val entityList = mediaDao.watchList()
    return mediaEntityToDomainMapper.transform(entityList)
  }

  suspend fun watchList(page: Page, pageSize: PageSize): PageList<Media> {
    val dtoList = mediaDao.watchList()
    val startIndex = (page.value - 1) * pageSize.value
    val endIndex = when {
      startIndex + pageSize.value > dtoList.size -> dtoList.size
      else -> startIndex + pageSize.value
    }
    return try {
      val dtoPageList = dtoList.subList(startIndex, endIndex)
      val movies = mediaEntityToDomainMapper.transform(dtoPageList)
      PageList(movies, page)
    } catch (throwable: Throwable) {
      PageList(emptyList(), page)
    }
  }

  suspend fun insert(media: List<Media>) {
    val entityList = mediaToEntityMapper.transform(media)
    mediaDao.insert(entityList)
  }

  suspend fun insert(media: Media) {
    val entity = mediaToEntityMapper.transform(media)
    mediaDao.insert(entity)
  }

  suspend fun isWatchList(movieId: Long): Boolean =
    mediaDao.find(movieId)?.watchList ?: false
}