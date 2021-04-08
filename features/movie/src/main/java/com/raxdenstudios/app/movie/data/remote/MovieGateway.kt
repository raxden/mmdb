package com.raxdenstudios.app.movie.data.remote

import com.raxdenstudios.app.movie.data.remote.model.MovieDto
import com.raxdenstudios.app.movie.data.remote.model.WatchListDto
import com.raxdenstudios.app.movie.data.remote.service.MovieV3Service
import com.raxdenstudios.app.movie.data.remote.service.MovieV4Service
import com.raxdenstudios.app.network.model.PageDto
import com.raxdenstudios.commons.ResultData
import com.raxdenstudios.commons.retrofit.toResultData

internal class MovieGateway(
  private val movieV3Service: MovieV3Service,
  private val movieV4Service: MovieV4Service,
) {

  suspend fun watchList(
    accountId: String,
    page: Int
  ): ResultData<PageDto<MovieDto>> =
    movieV4Service.watchList(accountId, page).toResultData(
      "Error occurred during fetching watch list movies"
    ) { body -> body }

  suspend fun addToWatchList(
    accountId: String,
    movieId: Long
  ): ResultData<WatchListDto.Response> =
    movieV3Service.watchList(
      accountId,
      WatchListDto.Request.Add(movieId)
    ).toResultData(
      "Error occurred during adding movie to watch list"
    ) { body -> body }

  suspend fun removeFromWatchList(
    accountId: String,
    movieId: Long
  ): ResultData<WatchListDto.Response> =
    movieV3Service.watchList(
      accountId,
      WatchListDto.Request.Remove(movieId)
    ).toResultData(
      "Error occurred during adding movie to watch list"
    ) { body -> body }

  suspend fun popular(page: Int): ResultData<PageDto<MovieDto>> =
    movieV3Service.popular(page).toResultData(
      "Error occurred during fetching popular movies"
    ) { body -> body }

  suspend fun nowPlaying(page: Int): ResultData<PageDto<MovieDto>> =
    movieV3Service.nowPlaying(page).toResultData(
      "Error occurred during fetching now playing movies"
    ) { body -> body }

  suspend fun topRated(page: Int): ResultData<PageDto<MovieDto>> =
    movieV3Service.topRated(page).toResultData(
      "Error occurred during fetching top rated movies"
    ) { body -> body }

  suspend fun upcoming(page: Int): ResultData<PageDto<MovieDto>> =
    movieV3Service.upcoming(page).toResultData(
      "Error occurred during fetching upcoming movies"
    ) { body -> body }
}
