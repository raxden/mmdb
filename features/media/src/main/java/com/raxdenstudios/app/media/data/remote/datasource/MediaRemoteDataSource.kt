package com.raxdenstudios.app.media.data.remote.datasource

import com.raxdenstudios.app.account.domain.model.Account
import com.raxdenstudios.app.media.data.remote.MediaGateway
import com.raxdenstudios.app.media.data.remote.exception.UserNotLoggedException
import com.raxdenstudios.app.media.data.remote.mapper.MediaDtoToDomainMapper
import com.raxdenstudios.app.media.data.remote.mapper.MediaTypeToDtoMapper
import com.raxdenstudios.app.media.data.remote.model.MediaDto
import com.raxdenstudios.app.media.domain.model.Media
import com.raxdenstudios.app.media.domain.model.MediaFilter
import com.raxdenstudios.app.media.domain.model.MediaType
import com.raxdenstudios.app.network.model.PageDto
import com.raxdenstudios.commons.ResultData
import com.raxdenstudios.commons.map
import com.raxdenstudios.commons.pagination.model.Page
import com.raxdenstudios.commons.pagination.model.PageList

internal class MediaRemoteDataSource(
  private val mediaGateway: MediaGateway,
  private val mediaTypeToDtoMapper: MediaTypeToDtoMapper,
  private val mediaDtoToDomainMapper: MediaDtoToDomainMapper,
) {

  suspend fun mediaById(mediaId: Long, mediaType: MediaType): ResultData<Media> =
    mediaGateway.detail(
      movieId = mediaId.toString(),
      mediaType = mediaTypeToDtoMapper.transform(mediaType)
    )
      .map { dto -> mediaDtoToDomainMapper.transform(mediaType, dto) }

  suspend fun addMediaToWatchList(
    account: Account.Logged,
    mediaType: MediaType,
    movieId: Long
  ): ResultData<Boolean> =
    mediaGateway.addToWatchList(
      accountId = account.credentials.accountId,
      mediaType = mediaTypeToDtoMapper.transform(mediaType),
      movieId = movieId
    ).map { true }

  suspend fun removeMediaFromWatchList(
    account: Account.Logged,
    mediaType: MediaType,
    movieId: Long
  ): ResultData<Boolean> =
    mediaGateway.removeFromWatchList(
      accountId = account.credentials.accountId,
      mediaType = mediaTypeToDtoMapper.transform(mediaType),
      movieId = movieId
    ).map { true }

  suspend fun watchList(account: Account.Logged, mediaType: MediaType): ResultData<List<Media>> =
    mediaGateway.watchList(
      mediaType = mediaTypeToDtoMapper.transform(mediaType),
      accountId = account.credentials.accountId
    )
      .map { dtoList ->
        dtoList.map { dto ->
          mediaDtoToDomainMapper.transform(mediaType, dto).copy(watchList = true)
        }
      }

  suspend fun medias(
    mediaFilter: MediaFilter,
    account: Account,
    page: Page,
  ): ResultData<PageList<Media>> =
    when (mediaFilter) {
      is MediaFilter.NowPlaying -> nowPlaying(mediaFilter.mediaType, page)
      is MediaFilter.Popular -> popular(mediaFilter.mediaType, page)
      is MediaFilter.TopRated -> topRated(mediaFilter.mediaType, page)
      MediaFilter.Upcoming -> upcoming(page)
      is MediaFilter.WatchList -> watchList(account, mediaFilter.mediaType, page)
    }

  private suspend fun watchList(
    account: Account,
    mediaType: MediaType,
    page: Page
  ): ResultData<PageList<Media>> = when (account) {
    is Account.Guest -> ResultData.Error(UserNotLoggedException())
    is Account.Logged -> mediaGateway.watchList(
      accountId = account.credentials.accountId,
      mediaType = mediaTypeToDtoMapper.transform(mediaType),
      page = page.value
    )
      .map { pageDto -> transformPageData(mediaType, pageDto) }
      .map { pageList -> markMediasAsWatched(pageList) }
  }

  private fun markMediasAsWatched(pageList: PageList<Media>) =
    pageList.copy(items = pageList.items.map { movie -> movie.copy(watchList = true) })

  private suspend fun upcoming(page: Page): ResultData<PageList<Media>> =
    mediaGateway.upcoming(page.value)
      .map { pageDto -> transformPageData(MediaType.Movie, pageDto) }

  private suspend fun topRated(mediaType: MediaType, page: Page): ResultData<PageList<Media>> =
    mediaGateway.topRated(mediaTypeToDtoMapper.transform(mediaType), page.value)
      .map { pageDto -> transformPageData(mediaType, pageDto) }

  private suspend fun popular(mediaType: MediaType, page: Page): ResultData<PageList<Media>> =
    mediaGateway.popular(mediaTypeToDtoMapper.transform(mediaType), page.value)
      .map { pageDto -> transformPageData(mediaType, pageDto) }

  private suspend fun nowPlaying(mediaType: MediaType, page: Page): ResultData<PageList<Media>> =
    mediaGateway.nowPlaying(mediaTypeToDtoMapper.transform(mediaType), page.value)
      .map { pageDto -> transformPageData(mediaType, pageDto) }

  private fun transformPageData(mediaType: MediaType, pageDto: PageDto<MediaDto>): PageList<Media> =
    pageDto.toPageList { movieDtoList ->
      movieDtoList.map { movieDto -> mediaDtoToDomainMapper.transform(mediaType, movieDto) }
    }
}
