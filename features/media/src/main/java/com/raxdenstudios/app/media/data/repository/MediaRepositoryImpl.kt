package com.raxdenstudios.app.media.data.repository

import com.raxdenstudios.app.account.data.local.datasource.AccountLocalDataSource
import com.raxdenstudios.app.account.domain.model.Account
import com.raxdenstudios.app.media.data.local.datasource.MediaLocalDataSource
import com.raxdenstudios.app.media.data.remote.datasource.MediaRemoteDataSource
import com.raxdenstudios.app.media.domain.model.Media
import com.raxdenstudios.app.media.domain.model.MediaFilter
import com.raxdenstudios.app.media.domain.model.MediaId
import com.raxdenstudios.app.media.domain.model.MediaType
import com.raxdenstudios.commons.ResultData
import com.raxdenstudios.commons.ext.coMap
import com.raxdenstudios.commons.ext.map
import com.raxdenstudios.commons.ext.onCoSuccess
import com.raxdenstudios.commons.pagination.model.Page
import com.raxdenstudios.commons.pagination.model.PageList
import com.raxdenstudios.commons.pagination.model.PageSize
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

internal class MediaRepositoryImpl @Inject constructor(
  private val mediaRemoteDataSource: MediaRemoteDataSource,
  private val mediaLocalDataSource: MediaLocalDataSource,
  private val accountLocalDataSource: AccountLocalDataSource,
) : MediaRepository {

  override fun observeWatchList(mediaType: MediaType): Flow<ResultData<List<Media>>> = flow {
    emit(mediaLocalDataSource.watchList(mediaType).first())
    emit(fetchWatchListFromRemoteAndUpdateLocalWatchList(mediaType))
    emitAll(mediaLocalDataSource.watchList(mediaType))
  }.distinctUntilChanged()

  override fun watchList(mediaType: MediaType): Flow<ResultData<List<Media>>> = flow {
    emit(mediaLocalDataSource.watchList(mediaType).first())
    emit(fetchWatchListFromRemoteAndUpdateLocalWatchList(mediaType))
  }.distinctUntilChanged()

  override suspend fun addToWatchList(
    mediaId: MediaId,
    mediaType: MediaType
  ): ResultData<Media> = when (val account = accountLocalDataSource.getAccount()) {
    is Account.Guest ->
      mediaRemoteDataSource.findById(mediaId, mediaType)
        .onCoSuccess { media -> mediaLocalDataSource.addToWatchList(media) }
    is Account.Logged ->
      mediaRemoteDataSource.addToWatchList(
        account = account,
        mediaType = mediaType,
        mediaId = mediaId
      ).onCoSuccess { media -> mediaLocalDataSource.addToWatchList(media) }
  }

  override suspend fun addToLocalWatchList(medias: List<Media>): ResultData<Boolean> =
    mediaLocalDataSource.addToWatchList(medias)

  override suspend fun removeFromWatchList(
    mediaId: MediaId,
    mediaType: MediaType
  ): ResultData<Boolean> = when (val account = accountLocalDataSource.getAccount()) {
    is Account.Guest -> {
      mediaLocalDataSource.removeFromWatchList(mediaId)
      ResultData.Success(true)
    }
    is Account.Logged ->
      mediaRemoteDataSource.removeFromWatchList(
        account = account,
        mediaType = mediaType,
        mediaId = mediaId
      ).onCoSuccess { mediaLocalDataSource.removeFromWatchList(mediaId) }
  }

  override suspend fun medias(
    mediaFilter: MediaFilter,
    page: Page,
    pageSize: PageSize
  ): ResultData<PageList<Media>> {
    val isAccountLogged = accountLocalDataSource.getAccount() is Account.Logged
    val isWatchList = mediaFilter is MediaFilter.WatchList
    return when {
      isWatchList && !isAccountLogged ->
        mediaLocalDataSource.watchList(mediaFilter.mediaType).first()
          .map { medias -> PageList(medias, page) }
      else -> mediaRemoteDataSource.medias(
        mediaFilter = mediaFilter,
        account = accountLocalDataSource.getAccount(),
        page = page
      ).coMap { pageList -> markMediasAsWatchedIfNecessary(pageList) }
    }
  }

  private suspend fun markMediasAsWatchedIfNecessary(pageList: PageList<Media>) =
    pageList.copy(
      items = pageList.items.map { media ->
        media.copyWith(watchList = mediaLocalDataSource.containsInWatchList(media.id))
      }
    )

  private suspend fun fetchWatchListFromRemoteAndUpdateLocalWatchList(
    mediaType: MediaType
  ): ResultData<List<Media>> = mediaRemoteDataSource.watchList(
    account = accountLocalDataSource.getAccount(),
    mediaType = mediaType,
  ).onCoSuccess { medias ->
    mediaLocalDataSource.clearWatchList()
    mediaLocalDataSource.addToWatchList(medias)
  }
}
