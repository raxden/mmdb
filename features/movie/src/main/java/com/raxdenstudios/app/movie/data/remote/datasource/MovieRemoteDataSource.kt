package com.raxdenstudios.app.movie.data.remote.datasource

import com.raxdenstudios.app.account.domain.model.Account
import com.raxdenstudios.app.movie.data.remote.MovieGateway
import com.raxdenstudios.app.movie.data.remote.exception.UserNotLoggedException
import com.raxdenstudios.app.movie.data.remote.mapper.MediaTypeToDtoMapper
import com.raxdenstudios.app.movie.data.remote.mapper.MovieDtoToDomainMapper
import com.raxdenstudios.app.movie.data.remote.model.MovieDto
import com.raxdenstudios.app.movie.domain.model.MediaType
import com.raxdenstudios.app.movie.domain.model.Movie
import com.raxdenstudios.app.movie.domain.model.SearchType
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

  suspend fun movieById(movieId: Long): ResultData<Movie> =
    movieGateway.detail(movieId.toString())
      .map { dto -> movieDtoToDomainMapper.transform(dto) }

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

  suspend fun watchList(account: Account.Logged, mediaType: MediaType): ResultData<List<Movie>> =
    movieGateway.watchList(
      mediaType = mediaTypeToDtoMapper.transform(mediaType),
      accountId = account.credentials.accountId
    )
      .map { dtoList ->
        dtoList.map { dto ->
          movieDtoToDomainMapper.transform(dto).copy(watchList = true)
        }
      }

  suspend fun movies(
    searchType: SearchType,
    account: Account,
    page: Page,
  ): ResultData<PageList<Movie>> =
    when (searchType) {
      is SearchType.NowPlaying -> nowPlaying(searchType.mediaType, page)
      is SearchType.Popular -> popular(searchType.mediaType, page)
      is SearchType.TopRated -> topRated(searchType.mediaType, page)
      SearchType.Upcoming -> upcoming(page)
      is SearchType.WatchList -> watchList(account, searchType.mediaType, page)
    }

  private suspend fun watchList(
    account: Account,
    mediaType: MediaType,
    page: Page
  ): ResultData<PageList<Movie>> = when (account) {
    is Account.Guest -> ResultData.Error(UserNotLoggedException())
    is Account.Logged -> movieGateway.watchList(
      accountId = account.credentials.accountId,
      mediaType = mediaTypeToDtoMapper.transform(mediaType),
      page = page.value
    )
      .map { pageDto -> transformPageData(pageDto) }
      .map { pageList -> markMoviesAsWatched(pageList) }
  }

  private fun markMoviesAsWatched(pageList: PageList<Movie>) =
    pageList.copy(items = pageList.items.map { movie -> movie.copy(watchList = true) })

  private suspend fun upcoming(page: Page): ResultData<PageList<Movie>> =
    movieGateway.upcoming(page.value)
      .map { pageDto -> transformPageData(pageDto) }

  private suspend fun topRated(mediaType: MediaType, page: Page): ResultData<PageList<Movie>> =
    movieGateway.topRated(mediaTypeToDtoMapper.transform(mediaType), page.value)
      .map { pageDto -> transformPageData(pageDto) }

  private suspend fun popular(mediaType: MediaType, page: Page): ResultData<PageList<Movie>> =
    movieGateway.popular(mediaTypeToDtoMapper.transform(mediaType), page.value)
      .map { pageDto -> transformPageData(pageDto) }

  private suspend fun nowPlaying(mediaType: MediaType, page: Page): ResultData<PageList<Movie>> =
    movieGateway.nowPlaying(mediaTypeToDtoMapper.transform(mediaType), page.value)
      .map { pageDto -> transformPageData(pageDto) }

  private fun transformPageData(pageDto: PageDto<MovieDto>): PageList<Movie> =
    pageDto.toPageList { movieDtoList -> movieDtoToDomainMapper.transform(movieDtoList) }
}
