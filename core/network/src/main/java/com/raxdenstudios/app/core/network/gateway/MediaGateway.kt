package com.raxdenstudios.app.core.network.gateway

import com.raxdenstudios.app.core.model.MediaId
import com.raxdenstudios.app.core.model.MediaType
import com.raxdenstudios.app.core.network.model.MediaDetailDto
import com.raxdenstudios.app.core.network.model.MediaDto
import com.raxdenstudios.app.core.network.model.NetworkErrorDto
import com.raxdenstudios.app.core.network.model.PageDto
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
}
