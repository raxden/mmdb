package com.raxdenstudios.app.core.network.gateway

import com.google.gson.Gson
import com.raxdenstudios.app.core.model.MediaId
import com.raxdenstudios.app.core.model.MediaType
import com.raxdenstudios.app.core.network.model.MediaDetailDto
import com.raxdenstudios.app.core.network.model.MediaDto
import com.raxdenstudios.app.core.network.model.NetworkErrorDto
import com.raxdenstudios.app.core.network.model.PageDto
import com.raxdenstudios.app.core.network.model.VideoDto
import com.raxdenstudios.app.core.network.service.MediaV3Service
import com.raxdenstudios.commons.ResultData
import com.raxdenstudios.commons.pagination.model.Page
import com.raxdenstudios.commons.retrofit.toResultData
import javax.inject.Inject

class MediaGateway @Inject constructor(
    private val mediaV3Service: MediaV3Service,
) {

    suspend fun fetchById(
        mediaId: MediaId,
        mediaType: MediaType
    ): ResultData<MediaDetailDto, NetworkErrorDto> = when (mediaType) {
        MediaType.Movie -> mediaV3Service.movie(mediaId.value.toString())
        MediaType.TvShow -> mediaV3Service.tvShow(mediaId.value.toString())
    }.toResultData("Error occurred during fetching detail with id: $mediaId")

    suspend fun nowPlaying(
        mediaType: MediaType,
        page: Page,
    ): ResultData<PageDto<MediaDto>, NetworkErrorDto> = when (mediaType) {
        MediaType.Movie -> mediaV3Service.nowPlayingMovies(page.value)
            .toResultData("Error occurred during fetching now playing movies")

        MediaType.TvShow -> ResultData.Success(PageDto.empty())
    }

    suspend fun popular(
        mediaType: MediaType,
        page: Page,
    ): ResultData<PageDto<MediaDto>, NetworkErrorDto> = when (mediaType) {
        MediaType.Movie -> mediaV3Service.popularMovies(page.value)
            .toResultData("Error occurred during fetching popular movies")

        MediaType.TvShow -> mediaV3Service.popularTVShows(page.value)
            .toResultData("Error occurred during fetching popular tv shows")
    }

    suspend fun topRated(
        mediaType: MediaType,
        page: Page,
    ): ResultData<PageDto<MediaDto>, NetworkErrorDto> = when (mediaType) {
        MediaType.Movie -> mediaV3Service.topRatedMovies(page.value)
            .toResultData("Error occurred during fetching top rated movies")

        MediaType.TvShow -> mediaV3Service.topRatedTVShows(page.value)
            .toResultData("Error occurred during fetching top rated tv shows")
    }

    @Suppress("UNCHECKED_CAST")
    suspend fun upcoming(
        mediaType: MediaType,
        page: Page,
    ): ResultData<PageDto<MediaDto>, NetworkErrorDto> = when (mediaType) {
        MediaType.Movie -> mediaV3Service.upcoming(page.value)
            .toResultData("Error occurred during fetching upcoming movies")

        MediaType.TvShow -> ResultData.Success(PageDto.empty())
    }

    suspend fun videos(
        mediaId: MediaId,
        mediaType: MediaType,
    ): ResultData<PageDto<VideoDto>, NetworkErrorDto> = when (mediaType) {
        MediaType.Movie -> mediaV3Service.movieVideos(mediaId.value.toString())
            .toResultData("Error occurred during searching videos for movie with id: $mediaId")

        MediaType.TvShow -> mediaV3Service.tvVideos(mediaId.value.toString())
            .toResultData("Error occurred during searching videos for tv show with id: $mediaId")
    }

    suspend fun related(
        mediaId: MediaId,
        mediaType: MediaType,
        page: Page,
    ): ResultData<PageDto<MediaDto>, NetworkErrorDto> = when (mediaType) {
        MediaType.Movie -> mediaV3Service.relatedMovies(mediaId.value.toString(), page.value)
            .toResultData("Error occurred during related videos for movie with id: $mediaId")

        MediaType.TvShow -> mediaV3Service.relatedTVShows(mediaId.value.toString(), page.value)
            .toResultData("Error occurred during related videos for tv show with id: $mediaId")
    }

    suspend fun search(
        query: String,
        page: Page,
    ): ResultData<PageDto<MediaDto>, NetworkErrorDto> =
        mediaV3Service.search(query, page.value)
            .toResultData("Error occurred during searching media with query: $query") { pageDto ->
                PageDto(
                    page = pageDto.page,
                    total_pages = pageDto.total_pages,
                    total_results = pageDto.total_results,
                    results = pageDto.results
                        .filter { jsonObject -> jsonObject.has("media_type") }
                        .mapNotNull { jsonObject ->
                            when (jsonObject.get("media_type").asString) {
                                "movie" -> Gson().fromJson(jsonObject, MediaDto.Movie::class.java)
                                "tv" -> Gson().fromJson(jsonObject, MediaDto.TVShow::class.java)
                                "person" -> null
                                else -> null
                            }
                        }
                )
            }
}
