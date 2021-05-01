package com.raxdenstudios.app.media.data.remote

import com.raxdenstudios.app.media.data.remote.model.MediaDto
import com.raxdenstudios.app.media.data.remote.model.WatchListDto
import com.raxdenstudios.app.media.data.remote.service.MediaV3Service
import com.raxdenstudios.app.media.data.remote.service.MediaV4Service
import com.raxdenstudios.app.network.model.PageDto
import com.raxdenstudios.commons.DispatcherFacade
import com.raxdenstudios.commons.ResultData
import com.raxdenstudios.commons.coMap
import com.raxdenstudios.commons.getValueOrNull
import com.raxdenstudios.commons.retrofit.toResultData
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext

internal class MediaGateway(
  private val dispatcher: DispatcherFacade,
  private val mediaV3Service: MediaV3Service,
  private val mediaV4Service: MediaV4Service,
) {

  companion object {
    private const val FIRST_PAGE = 1
  }

  suspend fun watchListMovies(
    accountId: String,
  ): ResultData<List<MediaDto.Movie>> =
    watchListMovies(accountId, FIRST_PAGE)
      .coMap { pageDto ->
        withContext(dispatcher.io()) {
          val allMedias = pageDto.results.toMutableList()
          val totalPages = pageDto.total_pages
          val movies = (FIRST_PAGE + 1..totalPages)
            .map { page -> async { watchListMovies(accountId, page) } }
            .mapNotNull { deferred -> deferred.await().getValueOrNull() }
            .map { resultData -> resultData.results }
            .flatten()
          allMedias.addAll(movies)
          allMedias
        }
      }

  suspend fun watchListTVShows(
    accountId: String,
  ): ResultData<List<MediaDto.TVShow>> =
    watchListTVShows(accountId, FIRST_PAGE)
      .coMap { pageDto ->
        withContext(dispatcher.io()) {
          val allMedias = pageDto.results.toMutableList()
          val totalPages = pageDto.total_pages
          val movies = (FIRST_PAGE + 1..totalPages)
            .map { page -> async { watchListTVShows(accountId, page) } }
            .mapNotNull { deferred -> deferred.await().getValueOrNull() }
            .map { resultData -> resultData.results }
            .flatten()
          allMedias.addAll(movies)
          allMedias
        }
      }

  suspend fun watchListMovies(
    accountId: String,
    page: Int
  ): ResultData<PageDto<MediaDto.Movie>> =
    mediaV4Service.watchListMovies(accountId, page)
      .toResultData("Error occurred during fetching watch list movies")

  suspend fun watchListTVShows(
    accountId: String,
    page: Int
  ): ResultData<PageDto<MediaDto.TVShow>> =
    mediaV4Service.watchListTVShows(accountId, page)
      .toResultData("Error occurred during fetching watch list tv shows")

  suspend fun addToWatchList(
    accountId: String,
    mediaType: String,
    mediaId: Long
  ): ResultData<Boolean> =
    mediaV3Service.watchList(
      accountId,
      WatchListDto.Request.Add(mediaId, mediaType)
    ).toResultData("Error occurred during adding movie to watch list") { true }

  suspend fun removeFromWatchList(
    accountId: String,
    mediaType: String,
    mediaId: Long
  ): ResultData<Boolean> =
    mediaV3Service.watchList(
      accountId,
      WatchListDto.Request.Remove(mediaId, mediaType)
    ).toResultData("Error occurred during adding movie to watch list") { true }

  suspend fun popularMovies(
    page: Int
  ): ResultData<PageDto<MediaDto.Movie>> =
    mediaV3Service.popularMovies(page)
      .toResultData("Error occurred during fetching popular movies")

  suspend fun popularTVShows(
    page: Int
  ): ResultData<PageDto<MediaDto.TVShow>> =
    mediaV3Service.popularTVShows(page)
      .toResultData("Error occurred during fetching popular tv shows")

  suspend fun nowPlaying(
    page: Int
  ): ResultData<PageDto<MediaDto.Movie>> =
    mediaV3Service.nowPlayingMovies(page)
      .toResultData("Error occurred during fetching now playing movies")

  suspend fun topRatedMovies(
    page: Int
  ): ResultData<PageDto<MediaDto.Movie>> =
    mediaV3Service.topRatedMovies(page)
      .toResultData("Error occurred during fetching top rated movies")

  suspend fun topRatedTVShows(
    page: Int
  ): ResultData<PageDto<MediaDto.TVShow>> =
    mediaV3Service.topRatedTVShows(page)
      .toResultData("Error occurred during fetching top rated tv shows")

  suspend fun upcoming(page: Int): ResultData<PageDto<MediaDto.Movie>> =
    mediaV3Service.upcoming(page)
      .toResultData("Error occurred during fetching upcoming movies")

  suspend fun detailMovie(mediaId: String): ResultData<MediaDto> =
    mediaV3Service.detailMovie(mediaId)
      .toResultData("Error ocurred during fetching detail movie with id: $mediaId")

  suspend fun detailTVShow(mediaId: String): ResultData<MediaDto> =
    mediaV3Service.detailTVShow(mediaId)
      .toResultData("Error ocurred during fetching detail tvShow with id: $mediaId")
}
