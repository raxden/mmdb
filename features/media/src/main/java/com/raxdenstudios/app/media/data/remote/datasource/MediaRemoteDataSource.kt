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
import com.raxdenstudios.commons.coFlatMap
import com.raxdenstudios.commons.map
import com.raxdenstudios.commons.pagination.model.Page
import com.raxdenstudios.commons.pagination.model.PageList

internal class MediaRemoteDataSource(
  private val mediaGateway: MediaGateway,
  private val mediaTypeToDtoMapper: MediaTypeToDtoMapper,
  private val mediaDtoToDomainMapper: MediaDtoToDomainMapper,
) {

  suspend fun mediaById(
    mediaId: Long,
    mediaType: MediaType
  ): ResultData<Media> = when (mediaType) {
    MediaType.Movie -> mediaGateway.detailMovie(mediaId.toString())
    MediaType.TVShow -> mediaGateway.detailTVShow(mediaId.toString())
  }.map { dto -> mediaDtoToDomainMapper.transform(dto) }

  suspend fun addMediaToWatchList(
    account: Account.Logged,
    mediaType: MediaType,
    mediaId: Long
  ): ResultData<Media> =
    mediaGateway.addToWatchList(
      accountId = account.credentials.accountId,
      mediaType = mediaTypeToDtoMapper.transform(mediaType),
      mediaId = mediaId
    )
      .coFlatMap { mediaById(mediaId, mediaType) }
      .map { media -> media.copyWith(watchList = true) }

  suspend fun removeMediaFromWatchList(
    account: Account.Logged,
    mediaType: MediaType,
    mediaId: Long
  ): ResultData<Boolean> =
    mediaGateway.removeFromWatchList(
      accountId = account.credentials.accountId,
      mediaType = mediaTypeToDtoMapper.transform(mediaType),
      mediaId = mediaId
    )

  suspend fun watchList(
    account: Account.Logged,
    mediaType: MediaType
  ): ResultData<List<Media>> = when (mediaType) {
    MediaType.Movie -> mediaGateway.watchListMovies(account.credentials.accountId)
    MediaType.TVShow -> mediaGateway.watchListTVShows(account.credentials.accountId)
  }.map { list ->
    list.map { dto -> mediaDtoToDomainMapper.transform(dto).copyWith(watchList = true) }
  }

  suspend fun medias(
    mediaFilter: MediaFilter,
    account: Account,
    page: Page,
  ): ResultData<PageList<Media>> = when (mediaFilter) {
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
    is Account.Logged -> when (mediaType) {
      MediaType.Movie -> mediaGateway.watchListMovies(
        accountId = account.credentials.accountId,
        page = page.value
      )
      MediaType.TVShow -> mediaGateway.watchListTVShows(
        accountId = account.credentials.accountId,
        page = page.value
      )
    }
      .map { pageDto -> transformMediaDtoPageData(pageDto) }
      .map { pageList -> markMediasAsWatched(pageList) }
  }

  private fun markMediasAsWatched(pageList: PageList<Media>) =
    pageList.copy(items = pageList.items.map { movie -> movie.copyWith(watchList = true) })

  private suspend fun upcoming(page: Page): ResultData<PageList<Media>> =
    mediaGateway.upcoming(page.value)
      .map { pageDto -> transformMediaDtoPageData(pageDto) }

  private suspend fun topRated(mediaType: MediaType, page: Page): ResultData<PageList<Media>> =
    when (mediaType) {
      MediaType.Movie -> mediaGateway.topRatedMovies(page.value)
      MediaType.TVShow -> mediaGateway.topRatedTVShows(page.value)
    }.map { pageDto -> transformMediaDtoPageData(pageDto) }

  private suspend fun popular(mediaType: MediaType, page: Page): ResultData<PageList<Media>> =
    when (mediaType) {
      MediaType.Movie -> mediaGateway.popularMovies(page.value)
      MediaType.TVShow -> mediaGateway.popularTVShows(page.value)
    }.map { pageDto -> transformMediaDtoPageData(pageDto) }

  private suspend fun nowPlaying(mediaType: MediaType, page: Page): ResultData<PageList<Media>> =
    when (mediaType) {
      MediaType.Movie -> mediaGateway.nowPlayingMovies(page.value)
      MediaType.TVShow -> mediaGateway.nowPlayingTVShows(page.value)
    }.map { pageDto -> transformMediaDtoPageData(pageDto) }

  private fun transformMediaDtoPageData(
    pageDto: PageDto<out MediaDto>
  ): PageList<Media> =
    pageDto.toPageList { mediaDtoList ->
      mediaDtoList.map { mediaDto -> mediaDtoToDomainMapper.transform(mediaDto) }
    }
}
