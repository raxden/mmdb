package com.raxdenstudios.app.media.data.local.datasource

import com.raxdenstudios.app.media.data.local.MediaDao
import com.raxdenstudios.app.media.data.local.mapper.MediaEntityToDomainMapper
import com.raxdenstudios.app.media.data.local.mapper.MediaToEntityMapper
import com.raxdenstudios.app.media.domain.model.Media
import com.raxdenstudios.commons.pagination.model.Page
import com.raxdenstudios.commons.pagination.model.PageList
import com.raxdenstudios.commons.pagination.model.PageSize

internal class MediaLocalDataSource(
  private val dao: MediaDao,
  private val mediaToEntityMapper: MediaToEntityMapper,
  private val mediaEntityToDomainMapper: MediaEntityToDomainMapper,
) {

  suspend fun watchList(): List<Media> {
    val entityList = dao.watchList()
    return mediaEntityToDomainMapper.transform(entityList)
  }

  suspend fun watchList(page: Page, pageSize: PageSize): PageList<Media> {
    val dtoList = dao.watchList()
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
    dao.insert(entityList)
  }

  suspend fun insert(media: Media) {
    val entity = mediaToEntityMapper.transform(media)
    dao.insert(entity)
  }

  suspend fun isWatchList(movieId: Long): Boolean =
    dao.find(movieId)?.watchList ?: false
}