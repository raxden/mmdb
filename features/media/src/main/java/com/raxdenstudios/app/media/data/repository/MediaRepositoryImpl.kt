package com.raxdenstudios.app.media.data.repository

import com.raxdenstudios.app.account.data.local.datasource.AccountLocalDataSource
import com.raxdenstudios.app.media.data.local.datasource.MediaLocalDataSource
import com.raxdenstudios.app.media.data.remote.datasource.MediaRemoteDataSource
import com.raxdenstudios.app.media.domain.model.Media
import com.raxdenstudios.app.media.domain.model.MediaFilter
import com.raxdenstudios.app.media.domain.model.MediaType
import com.raxdenstudios.commons.ResultData
import com.raxdenstudios.commons.coMap
import com.raxdenstudios.commons.onCoSuccess
import com.raxdenstudios.commons.pagination.model.Page
import com.raxdenstudios.commons.pagination.model.PageList
import com.raxdenstudios.commons.pagination.model.PageSize

internal class MediaRepositoryImpl(
  private val mediaRemoteDataSource: MediaRemoteDataSource,
  private val mediaLocalDataSource: MediaLocalDataSource,
  private val accountLocalDataSource: AccountLocalDataSource,
) : MediaRepository {

  override suspend fun addToWatchList(
    mediaId: Long,
    mediaType: MediaType
  ): ResultData<Media> =
    mediaRemoteDataSource.addToWatchList(
      account = accountLocalDataSource.getAccount(),
      mediaType = mediaType,
      mediaId = mediaId
    ).onCoSuccess { media -> mediaLocalDataSource.addToWatchList(media) }

  override suspend fun addToLocalWatchList(medias: List<Media>): ResultData<Boolean> =
    mediaLocalDataSource.addToWatchList(medias)

  override suspend fun removeFromWatchList(
    mediaId: Long,
    mediaType: MediaType
  ): ResultData<Boolean> =
    mediaRemoteDataSource.removeFromWatchList(
      account = accountLocalDataSource.getAccount(),
      mediaType = mediaType,
      mediaId = mediaId
    ).onCoSuccess { mediaLocalDataSource.removeFromWatchList(mediaId) }

  override suspend fun watchList(mediaType: MediaType): ResultData<List<Media>> =
    mediaRemoteDataSource.watchList(
      account = accountLocalDataSource.getAccount(),
      mediaType = mediaType,
    )

  override suspend fun medias(
    mediaFilter: MediaFilter,
    page: Page,
    pageSize: PageSize
  ): ResultData<PageList<Media>> =
    mediaRemoteDataSource.medias(
      mediaFilter = mediaFilter,
      account = accountLocalDataSource.getAccount(),
      page = page
    ).coMap { pageList -> markMediasAsWatchedIfNecessary(pageList) }

  private suspend fun markMediasAsWatchedIfNecessary(pageList: PageList<Media>) =
    pageList.copy(
      items = pageList.items.map { media ->
        media.copyWith(watchList = mediaLocalDataSource.containsInWatchList(media.id))
      }
    )
}
