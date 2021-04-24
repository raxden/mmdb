package com.raxdenstudios.app.media.data.repository

import com.raxdenstudios.app.account.data.local.datasource.AccountLocalDataSource
import com.raxdenstudios.app.account.domain.model.Account
import com.raxdenstudios.app.media.data.local.datasource.MediaLocalDataSource
import com.raxdenstudios.app.media.data.remote.datasource.MediaRemoteDataSource
import com.raxdenstudios.app.media.data.remote.exception.UserNotLoggedException
import com.raxdenstudios.app.media.domain.model.Media
import com.raxdenstudios.app.media.domain.model.MediaFilter
import com.raxdenstudios.app.media.domain.model.MediaType
import com.raxdenstudios.commons.ResultData
import com.raxdenstudios.commons.coMap
import com.raxdenstudios.commons.map
import com.raxdenstudios.commons.onCoSuccess
import com.raxdenstudios.commons.pagination.model.Page
import com.raxdenstudios.commons.pagination.model.PageList
import com.raxdenstudios.commons.pagination.model.PageSize

internal class MediaRepositoryImpl(
  private val mediaRemoteDataSource: MediaRemoteDataSource,
  private val mediaLocalDataSource: MediaLocalDataSource,
  private val accountLocalDataSource: AccountLocalDataSource,
) : MediaRepository {

  override suspend fun addMediaToWatchList(
    mediaId: Long,
    mediaType: MediaType
  ): ResultData<Media> =
    when (val account = accountLocalDataSource.getAccount()) {
      is Account.Guest -> ResultData.Error(UserNotLoggedException())
      is Account.Logged -> addMediaToWatchList(account, mediaType, mediaId)
    }

  private suspend fun addMediaToWatchList(
    account: Account.Logged,
    mediaType: MediaType,
    mediaId: Long
  ): ResultData<Media> =
    mediaRemoteDataSource.addMediaToWatchList(account, mediaType, mediaId)
      .onCoSuccess { media -> mediaLocalDataSource.addToWatchList(media) }

  override suspend fun removeMediaFromWatchList(
    mediaId: Long,
    mediaType: MediaType
  ): ResultData<Boolean> =
    when (val account = accountLocalDataSource.getAccount()) {
      is Account.Guest -> ResultData.Error(UserNotLoggedException())
      is Account.Logged -> removeMovieFromWatchList(account, mediaType, mediaId)
    }

  private suspend fun removeMovieFromWatchList(
    account: Account.Logged,
    mediaType: MediaType,
    mediaId: Long
  ): ResultData<Boolean> =
    mediaRemoteDataSource.removeMediaFromWatchList(account, mediaType, mediaId)
      .onCoSuccess { mediaLocalDataSource.removeFromWatchList(mediaId) }

  override suspend fun medias(
    mediaFilter: MediaFilter,
    page: Page,
    pageSize: PageSize
  ): ResultData<PageList<Media>> =
    mediaRemoteDataSource.medias(
      mediaFilter = mediaFilter,
      account = accountLocalDataSource.getAccount(),
      page = page
    )
      .coMap { pageList -> markMediasAsWatchedIfNecessary(pageList) }

  private suspend fun markMediasAsWatchedIfNecessary(pageList: PageList<Media>) =
    pageList.copy(
      items = pageList.items.map { media ->
        media.copyWith(watchList = mediaLocalDataSource.containsInWatchList(media.id))
      }
    )

  override suspend fun loadWatchListFromRemoteAndPersistInLocal(
    mediaType: MediaType
  ): ResultData<Boolean> =
    when (val account = accountLocalDataSource.getAccount()) {
      is Account.Guest -> ResultData.Error(UserNotLoggedException())
      is Account.Logged -> loadWatchListFromRemoteAndPersistInLocal(account, mediaType)
    }

  private suspend fun loadWatchListFromRemoteAndPersistInLocal(
    account: Account.Logged,
    mediaType: MediaType,
  ): ResultData<Boolean> =
    mediaRemoteDataSource.watchList(account, mediaType)
      .coMap { medias -> mediaLocalDataSource.addToWatchList(medias) }
      .map { true }
}
