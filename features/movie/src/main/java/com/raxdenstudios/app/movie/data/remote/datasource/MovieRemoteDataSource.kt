package com.raxdenstudios.app.movie.data.remote.datasource

import com.raxdenstudios.app.account.domain.model.Account
import com.raxdenstudios.app.movie.data.remote.MovieGateway
import com.raxdenstudios.app.movie.data.remote.exception.UserNotLoggedException
import com.raxdenstudios.app.movie.data.remote.mapper.MediaTypeToDtoMapper
import com.raxdenstudios.app.movie.data.remote.mapper.MovieDtoToDomainMapper
import com.raxdenstudios.app.movie.data.remote.model.MovieDto
import com.raxdenstudios.app.movie.domain.model.Media
import com.raxdenstudios.app.movie.domain.model.MediaFilter
import com.raxdenstudios.app.movie.domain.model.MediaType
import com.raxdenstudios.app.network.model.PageDto
import com.raxdenstudios.commons.ResultData
import com.raxdenstudios.commons.map
import com.raxdenstudios.commons.pagination.model.Page
import com.raxdenstudios.commons.pagination.model.PageList

internal class MovieRemoteDataSource(
  private val movieGateway: MovieGateway,
  private val mediaTypeToDtoMapper: MediaTypeToDtoMapper,
  private val movieDtoToDomainMapper: MovieDtoToDomainMapper,
) {

  suspend fun movieById(movieId: Long, mediaType: MediaType): ResultData<Media> =
    movieGateway.detail(
      movieId = movieId.toString(),
      mediaType = mediaTypeToDtoMapper.transform(mediaType)
    )
      .map { dto -> movieDtoToDomainMapper.transform(mediaType, dto) }

  suspend fun addMovieToWatchList(
    account: Account.Logged,
    mediaType: MediaType,
    movieId: Long
  ): ResultData<Boolean> =
    movieGateway.addToWatchList(
      accountId = account.credentials.accountId,
      mediaType = mediaTypeToDtoMapper.transform(mediaType),
      movieId = movieId
    ).map { true }

  suspend fun removeMovieFromWatchList(
    account: Account.Logged,
    mediaType: MediaType,
    movieId: Long
  ): ResultData<Boolean> =
    movieGateway.removeFromWatchList(
      accountId = account.credentials.accountId,
      mediaType = mediaTypeToDtoMapper.transform(mediaType),
      movieId = movieId
    ).map { true }

  suspend fun watchList(account: Account.Logged, mediaType: MediaType): ResultData<List<Media>> =
    movieGateway.watchList(
      mediaType = mediaTypeToDtoMapper.transform(mediaType),
      accountId = account.credentials.accountId
    )
      .map { dtoList ->
        dtoList.map { dto ->
          movieDtoToDomainMapper.transform(mediaType, dto).copy(watchList = true)
        }
      }

  suspend fun movies(
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
    is Account.Logged -> movieGateway.watchList(
      accountId = account.credentials.accountId,
      mediaType = mediaTypeToDtoMapper.transform(mediaType),
      page = page.value
    )
      .map { pageDto -> transformPageData(mediaType, pageDto) }
      .map { pageList -> markMoviesAsWatched(pageList) }
  }

  private fun markMoviesAsWatched(pageList: PageList<Media>) =
    pageList.copy(items = pageList.items.map { movie -> movie.copy(watchList = true) })

  private suspend fun upcoming(page: Page): ResultData<PageList<Media>> =
    movieGateway.upcoming(page.value)
      .map { pageDto -> transformPageData(MediaType.Movie, pageDto) }

  private suspend fun topRated(mediaType: MediaType, page: Page): ResultData<PageList<Media>> =
    movieGateway.topRated(mediaTypeToDtoMapper.transform(mediaType), page.value)
      .map { pageDto -> transformPageData(mediaType, pageDto) }

  private suspend fun popular(mediaType: MediaType, page: Page): ResultData<PageList<Media>> =
    movieGateway.popular(mediaTypeToDtoMapper.transform(mediaType), page.value)
      .map { pageDto -> transformPageData(mediaType, pageDto) }

  private suspend fun nowPlaying(mediaType: MediaType, page: Page): ResultData<PageList<Media>> =
    movieGateway.nowPlaying(mediaTypeToDtoMapper.transform(mediaType), page.value)
      .map { pageDto -> transformPageData(mediaType, pageDto) }

  private fun transformPageData(mediaType: MediaType, pageDto: PageDto<MovieDto>): PageList<Media> =
    pageDto.toPageList { movieDtoList ->
      movieDtoList.map { movieDto -> movieDtoToDomainMapper.transform(mediaType, movieDto) }
    }
}
