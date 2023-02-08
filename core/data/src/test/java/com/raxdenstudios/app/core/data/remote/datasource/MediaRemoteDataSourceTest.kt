package com.raxdenstudios.app.core.data.remote.datasource

import com.google.common.truth.Truth.assertThat
import com.raxdenstudios.app.core.data.remote.mapper.MediaDetailDtoToDomainMapper
import com.raxdenstudios.app.core.data.remote.mapper.MediaDtoToDomainMapper
import com.raxdenstudios.app.core.data.remote.mapper.MediaTypeToDtoMapper
import com.raxdenstudios.app.core.data.remote.mapper.NetworkErrorDtoToErrorMapper
import com.raxdenstudios.app.core.data.remote.mapper.VideoDtoToDomainMapper
import com.raxdenstudios.app.core.model.Media
import com.raxdenstudios.app.core.model.MediaId
import com.raxdenstudios.app.core.model.MediaType
import com.raxdenstudios.app.core.model.Video
import com.raxdenstudios.app.core.network.gateway.MediaGateway
import com.raxdenstudios.app.core.network.gateway.WatchlistGateway
import com.raxdenstudios.app.core.network.model.MediaDetailDto
import com.raxdenstudios.app.core.network.model.PageDto
import com.raxdenstudios.app.core.network.model.VideoDto
import com.raxdenstudios.commons.ResultData
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
internal class MediaRemoteDataSourceTest {

    private val mediaGateway: MediaGateway = mockk()
    private val watchlistGateway: WatchlistGateway = mockk()
    private val mediaDtoToDomainMapper: MediaDtoToDomainMapper = mockk()
    private val mediaDetailDtoToDomainMapper: MediaDetailDtoToDomainMapper = mockk()
    private val mediaTypeToDtoMapper: MediaTypeToDtoMapper = mockk()
    private val videoDtoToDomainMapper: VideoDtoToDomainMapper = mockk()
    private val networkErrorDtoToErrorMapper: NetworkErrorDtoToErrorMapper = mockk()
    private lateinit var dataSource: MediaRemoteDataSource

    @Before
    fun setUp() {
        dataSource = MediaRemoteDataSource(
            mediaGateway = mediaGateway,
            watchlistGateway = watchlistGateway,
            mediaDtoToDomainMapper = mediaDtoToDomainMapper,
            mediaDetailDtoToDomainMapper = mediaDetailDtoToDomainMapper,
            mediaTypeToDtoMapper = mediaTypeToDtoMapper,
            videoDtoToDomainMapper = videoDtoToDomainMapper,
            networkErrorDtoToErrorMapper = networkErrorDtoToErrorMapper,
        )
    }

    @Test
    fun `fetchById should return a media`() = runTest {
        val mediaId = MediaId(1)
        val mediaType = MediaType.Movie
        coEvery { mediaGateway.fetchById(mediaId, mediaType) } returns ResultData.Success(MediaDetailDto.Movie())
        every { mediaDetailDtoToDomainMapper.transform(any<MediaDetailDto.Movie>()) } returns Media.Movie.mock

        val result = dataSource.fetchById(mediaId, mediaType)

        assertThat(result).isEqualTo(ResultData.Success(Media.Movie.mock))
    }

    @Test
    fun `videos should return a list of videos`() = runTest {
        val mediaId = MediaId(1)
        val mediaType = MediaType.Movie
        coEvery { mediaGateway.videos(mediaId, mediaType) } returns ResultData.Success(videosDto)
        every { videoDtoToDomainMapper.transform(videosDto.results) } returns listOf(Video.mock)

        val result = dataSource.videos(mediaId, mediaType)

        assertThat(result).isEqualTo(ResultData.Success(listOf(Video.mock)))
    }

    companion object {

        private val videosDto = PageDto(results = listOf(VideoDto.mock))
    }
}
