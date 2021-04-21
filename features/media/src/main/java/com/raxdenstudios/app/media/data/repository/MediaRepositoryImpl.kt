package com.raxdenstudios.app.media.data.repository

import com.raxdenstudios.app.account.data.local.datasource.AccountLocalDataSource
import com.raxdenstudios.app.account.domain.model.Account
import com.raxdenstudios.app.media.data.local.datasource.MediaLocalDataSource
import com.raxdenstudios.app.media.data.remote.datasource.MediaRemoteDataSource
import com.raxdenstudios.app.media.data.remote.exception.UserNotLoggedException
import com.raxdenstudios.app.media.domain.model.Media
import com.raxdenstudios.app.media.domain.model.MediaFilter
import com.raxdenstudios.app.media.domain.model.MediaType
import com.raxdenstudios.commons.*
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

  private suspend fun getMovieAndMarkAsWatched(
    movieId: Long,
    mediaType: MediaType,
    watched: Boolean
  ): ResultData<Media> =
    mediaRemoteDataSource.mediaById(movieId, mediaType)
      .map { movie -> movie.copy(watchList = watched) }

  private suspend fun updateMovie(media: Media): ResultData.Success<Boolean> {
    mediaLocalDataSource.insert(media)
    return ResultData.Success(true)
  }

  override suspend fun medias(
    mediaFilter: MediaFilter,
    page: Page,
    pageSize: PageSize
  ): ResultData<PageList<Media>> =
    moviesFromRemote(mediaFilter, page, pageSize)

  private suspend fun moviesFromRemote(
    mediaFilter: MediaFilter,
    page: Page,
    pageSize: PageSize
  ): ResultData<PageList<Media>> {
    val account = accountLocalDataSource.getAccount()
    return mediaRemoteDataSource.medias(mediaFilter, account, page)
      .coMap { pageList -> markMoviesAsWatchedIfWereWatched(pageList) }
  }

  private suspend fun markMoviesAsWatchedIfWereWatched(pageList: PageList<Media>) =
    pageList.copy(
      items = pageList.items.map { movie ->
        movie.copy(watchList = mediaLocalDataSource.isWatchList(movie.id))
      }
    )

  override suspend fun loadWatchListInLocal(
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
      .coFlatMap { movies -> updateMovies(movies) }

  private suspend fun updateMovies(media: List<Media>): ResultData.Success<Boolean> {
    mediaLocalDataSource.insert(media)
    return ResultData.Success(true)
  }
}
