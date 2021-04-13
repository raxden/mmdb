package com.raxdenstudios.app.movie.data.remote.datasource

import com.raxdenstudios.app.movie.data.remote.MovieGateway
import com.raxdenstudios.app.movie.data.remote.mapper.MovieDtoToDomainMapper
import com.raxdenstudios.app.movie.data.remote.model.MovieDto
import com.raxdenstudios.app.movie.domain.model.Movie
import com.raxdenstudios.app.movie.domain.model.SearchType
import com.raxdenstudios.app.network.model.PageDto
import com.raxdenstudios.commons.ResultData
import com.raxdenstudios.commons.map
import com.raxdenstudios.commons.pagination.model.Page
import com.raxdenstudios.commons.pagination.model.PageList

internal class MovieRemoteDataSource(
  private val movieGateway: MovieGateway,
  private val movieDtoToDomainMapper: MovieDtoToDomainMapper,
) {

  suspend fun movieById(movieId: Long): ResultData<Movie> =
    movieGateway.detail(movieId.toString())
      .map { dto -> movieDtoToDomainMapper.transform(dto) }

  suspend fun addMovieToWatchList(accountId: String, movieId: Long): ResultData<Boolean> =
    movieGateway.addToWatchList(accountId, movieId).map { true }

  suspend fun removeMovieFromWatchList(accountId: String, movieId: Long): ResultData<Boolean> =
    movieGateway.removeFromWatchList(accountId, movieId).map { true }

  suspend fun watchList(accountId: String): ResultData<List<Movie>> =
    movieGateway.watchList(accountId)
      .map { dtoList ->
        dtoList.map { dto ->
          movieDtoToDomainMapper.transform(dto).copy(watchList = true)
        }
      }

  suspend fun watchList(accountId: String, page: Page): ResultData<PageList<Movie>> =
    movieGateway.watchList(accountId, page.value)
      .map { pageDto -> transformPageData(pageDto) }
      .map { pageList -> markMoviesAsWatched(pageList) }

  private fun markMoviesAsWatched(pageList: PageList<Movie>) =
    pageList.copy(items = pageList.items.map { movie -> movie.copy(watchList = true) })

  suspend fun movies(
    searchType: SearchType,
    page: Page,
  ): ResultData<PageList<Movie>> =
    when (searchType) {
      SearchType.NowPlaying ->
        movieGateway.nowPlaying(page.value).map { pageDto -> transformPageData(pageDto) }
      SearchType.Popular ->
        movieGateway.popular(page.value).map { pageDto -> transformPageData(pageDto) }
      SearchType.TopRated ->
        movieGateway.topRated(page.value).map { pageDto -> transformPageData(pageDto) }
      SearchType.Upcoming ->
        movieGateway.upcoming(page.value).map { pageDto -> transformPageData(pageDto) }
    }

  private fun transformPageData(pageDto: PageDto<MovieDto>): PageList<Movie> =
    pageDto.toPageList { movieDtoList ->
      movieDtoToDomainMapper.transform(movieDtoList)
    }
}
