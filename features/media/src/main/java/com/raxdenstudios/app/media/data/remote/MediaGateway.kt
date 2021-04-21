package com.raxdenstudios.app.media.data.remote

import com.raxdenstudios.app.media.data.remote.model.MediaDto
import com.raxdenstudios.app.media.data.remote.model.WatchListDto
import com.raxdenstudios.app.media.data.remote.service.MediaV3Service
import com.raxdenstudios.app.media.data.remote.service.MediaV4Service
import com.raxdenstudios.app.network.model.PageDto
import com.raxdenstudios.commons.DispatcherFacade
import com.raxdenstudios.commons.ResultData
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

  suspend fun watchList(
    accountId: String,
    mediaType: String,
  ): ResultData<List<MediaDto>> =
    when (val resultData = watchList(accountId, mediaType, FIRST_PAGE)) {
      is ResultData.Error -> resultData
      is ResultData.Success -> {
        withContext(dispatcher.io()) {
          val allMovies = resultData.value.results.toMutableList()
          val totalPages = resultData.value.total_pages
          val movies = (FIRST_PAGE + 1..totalPages)
            .map { page -> async { watchList(accountId, mediaType, page) } }
            .mapNotNull { deferred -> deferred.await().getValueOrNull() }
            .map { resultData -> resultData.results }
            .flatten()
          allMovies.addAll(movies)
          ResultData.Success(allMovies)
        }
      }
    }

  suspend fun watchList(
    accountId: String,
    mediaType: String,
    page: Int
  ): ResultData<PageDto<MediaDto>> =
    mediaV4Service.watchList(accountId, mediaType, page).toResultData(
      "Error occurred during fetching watch list movies"
    ) { body -> body }

  suspend fun addToWatchList(
    accountId: String,
    mediaType: String,
    mediaId: Long
  ): ResultData<WatchListDto.Response> =
    mediaV3Service.watchList(
      accountId,
      WatchListDto.Request.Add(mediaId, mediaType)
    ).toResultData(
      "Error occurred during adding movie to watch list"
    ) { body -> body }

  suspend fun removeFromWatchList(
    accountId: String,
    mediaType: String,
    mediaId: Long
  ): ResultData<WatchListDto.Response> =
    mediaV3Service.watchList(
      accountId,
      WatchListDto.Request.Remove(mediaId, mediaType)
    ).toResultData(
      "Error occurred during adding movie to watch list"
    ) { body -> body }

  suspend fun popular(mediaType: String, page: Int): ResultData<PageDto<MediaDto>> =
    mediaV3Service.popular(mediaType, page).toResultData(
      "Error occurred during fetching popular movies"
    ) { body -> body }

  suspend fun nowPlaying(mediaType: String, page: Int): ResultData<PageDto<MediaDto>> =
    mediaV3Service.nowPlaying(mediaType, page).toResultData(
      "Error occurred during fetching now playing movies"
    ) { body -> body }

  suspend fun topRated(mediaType: String, page: Int): ResultData<PageDto<MediaDto>> =
    mediaV3Service.topRated(mediaType, page).toResultData(
      "Error occurred during fetching top rated movies"
    ) { body -> body }

  suspend fun upcoming(page: Int): ResultData<PageDto<MediaDto>> =
    mediaV3Service.upcoming(page).toResultData(
      "Error occurred during fetching upcoming movies"
    ) { body -> body }

  suspend fun detail(mediaId: String, mediaType: String): ResultData<MediaDto> =
    mediaV3Service.detail(mediaType, mediaId).toResultData(
      "Error ocurred during fetching detail movie with id: $mediaId"
    ) { body -> body }
}
