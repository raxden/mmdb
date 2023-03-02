package com.raxdenstudios.app.core.data.remote.datasource

import com.google.common.truth.Truth.assertThat
import com.raxdenstudios.app.core.data.remote.mapper.CertificationDtoToDomainMapper
import com.raxdenstudios.app.core.data.remote.mapper.DateDtoToLocalDateMapper
import com.raxdenstudios.app.core.data.remote.mapper.GenreDtoToDomainMapper
import com.raxdenstudios.app.core.data.remote.mapper.MediaDetailDtoToDomainMapper
import com.raxdenstudios.app.core.data.remote.mapper.MediaDtoToDomainMapper
import com.raxdenstudios.app.core.data.remote.mapper.MediaTypeToDtoMapper
import com.raxdenstudios.app.core.data.remote.mapper.MovieDetailDtoToDomainMapper
import com.raxdenstudios.app.core.data.remote.mapper.MovieDtoToDomainMapper
import com.raxdenstudios.app.core.data.remote.mapper.NetworkErrorDtoToErrorMapper
import com.raxdenstudios.app.core.data.remote.mapper.PictureDtoToDomainMapper
import com.raxdenstudios.app.core.data.remote.mapper.TVShowDetailDtoToDomainMapper
import com.raxdenstudios.app.core.data.remote.mapper.TVShowDtoToDomainMapper
import com.raxdenstudios.app.core.data.remote.mapper.VideoDtoToDomainMapper
import com.raxdenstudios.app.core.data.remote.mapper.VoteDtoToDomainMapper
import com.raxdenstudios.app.core.model.Media
import com.raxdenstudios.app.core.model.MediaId
import com.raxdenstudios.app.core.model.MediaType
import com.raxdenstudios.app.core.model.Video
import com.raxdenstudios.app.core.network.APIDataProvider
import com.raxdenstudios.app.core.network.ConfigProvider
import com.raxdenstudios.app.core.network.gateway.MediaGateway
import com.raxdenstudios.app.core.network.gateway.WatchlistGateway
import com.raxdenstudios.app.core.network.model.MediaDetailDto
import com.raxdenstudios.app.core.network.model.MediaDto
import com.raxdenstudios.app.core.network.model.PageDto
import com.raxdenstudios.app.core.network.model.VideoDto
import com.raxdenstudios.commons.ResultData
import com.raxdenstudios.commons.pagination.model.Page
import com.raxdenstudios.commons.pagination.model.PageList
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.threeten.bp.LocalDate

@ExperimentalCoroutinesApi
internal class MediaRemoteDataSourceTest {

    private val mediaGateway: MediaGateway = mockk()
    private val watchlistGateway: WatchlistGateway = mockk()
    private val apiDataProvider: APIDataProvider = object : APIDataProvider {
        override val baseUrl: String = ""
        override val baseImageUrl: String = ""
        override val token: String = ""
    }
    private val configProvider: ConfigProvider = mockk {
        every { language } returns "es-ES"
        every { region } returns "ES"
    }
    private val voteDtoToDomainMapper: VoteDtoToDomainMapper = VoteDtoToDomainMapper()
    private val pictureDtoToDomainMapper: PictureDtoToDomainMapper = PictureDtoToDomainMapper(
        apiDataProvider = apiDataProvider
    )
    private val dateDtoToLocalDateMapper: DateDtoToLocalDateMapper = mockk {
        every { transform(any<String>()) } returns LocalDate.of(1970, 1, 1)
    }
    private val genreDtoToDomainMapper: GenreDtoToDomainMapper = GenreDtoToDomainMapper()
    private val movieDtoToDomainMapper: MovieDtoToDomainMapper = MovieDtoToDomainMapper(
        voteDtoToDomainMapper = voteDtoToDomainMapper,
        pictureDtoToDomainMapper = pictureDtoToDomainMapper,
        dateDtoToLocalDateMapper = dateDtoToLocalDateMapper,
        genreDtoToDomainMapper = genreDtoToDomainMapper,
    )
    private val tvShowDtoToDomainMapper: TVShowDtoToDomainMapper = TVShowDtoToDomainMapper(
        voteDtoToDomainMapper = voteDtoToDomainMapper,
        pictureDtoToDomainMapper = pictureDtoToDomainMapper,
        dateDtoToLocalDateMapper = dateDtoToLocalDateMapper,
        genreDtoToDomainMapper = genreDtoToDomainMapper,
    )
    private val mediaDtoToDomainMapper: MediaDtoToDomainMapper = MediaDtoToDomainMapper(
        movieDtoToDomainMapper = movieDtoToDomainMapper,
        tvShowDtoToDomainMapper = tvShowDtoToDomainMapper,
    )
    private val certificationDtoToDomainMapper: CertificationDtoToDomainMapper = CertificationDtoToDomainMapper(
        configProvider = configProvider
    )
    private val movieDetailDtoToDomainMapper: MovieDetailDtoToDomainMapper = MovieDetailDtoToDomainMapper(
        voteDtoToDomainMapper = voteDtoToDomainMapper,
        pictureDtoToDomainMapper = pictureDtoToDomainMapper,
        dateDtoToLocalDateMapper = dateDtoToLocalDateMapper,
        genreDtoToDomainMapper = genreDtoToDomainMapper,
        certificationDtoToDomainMapper = certificationDtoToDomainMapper,
    )
    private val tvShowDetailDtoToDomainMapper: TVShowDetailDtoToDomainMapper = TVShowDetailDtoToDomainMapper(
        voteDtoToDomainMapper = voteDtoToDomainMapper,
        pictureDtoToDomainMapper = pictureDtoToDomainMapper,
        dateDtoToLocalDateMapper = dateDtoToLocalDateMapper,
        genreDtoToDomainMapper = genreDtoToDomainMapper,
        certificationDtoToDomainMapper = certificationDtoToDomainMapper,
    )
    private val mediaDetailDtoToDomainMapper: MediaDetailDtoToDomainMapper = MediaDetailDtoToDomainMapper(
        movieDetailDtoToDomainMapper = movieDetailDtoToDomainMapper,
        tvShowDetailDtoToDomainMapper = tvShowDetailDtoToDomainMapper,
    )
    private val mediaTypeToDtoMapper: MediaTypeToDtoMapper = MediaTypeToDtoMapper()
    private val videoDtoToDomainMapper: VideoDtoToDomainMapper = VideoDtoToDomainMapper(
        dateDtoToLocalDateMapper = dateDtoToLocalDateMapper,
    )
    private val networkErrorDtoToErrorMapper: NetworkErrorDtoToErrorMapper = NetworkErrorDtoToErrorMapper()
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
        coEvery { mediaGateway.fetchById(mediaId, mediaType) } returns ResultData.Success(MediaDetailDto.Movie.mock)

        val result = dataSource.fetchById(mediaId, mediaType)

        assertThat(result).isEqualTo(ResultData.Success(Media.Movie.mock.copy(genres = emptyList())))
    }

    @Test
    fun `videos should return a list of videos`() = runTest {
        val mediaId = MediaId(1)
        val mediaType = MediaType.Movie
        coEvery { mediaGateway.videos(mediaId, mediaType) } returns ResultData.Success(videosDto)

        val result = dataSource.videos(mediaId, mediaType)

        assertThat(result).isEqualTo(ResultData.Success(listOf(Video.mock)))
    }

    @Test
    fun `related should return a list of media`() = runTest {
        val mediaId = MediaId(1)
        val mediaType = MediaType.Movie
        val page = Page(1)
        coEvery { mediaGateway.related(mediaId, mediaType, page) } returns ResultData.Success(moviesDto)

        val result = dataSource.related(mediaId, mediaType, page)

        assertThat(result).isEqualTo(
            ResultData.Success(
                PageList(
                    items = listOf(
                        Media.Movie.mock.copy(
                            genres = emptyList(),
                            certification = ""
                        )
                    ),
                    page = Page(1)
                )
            )
        )
    }

    companion object {

        private val moviesDto = PageDto<MediaDto>(results = listOf(MediaDto.Movie.mock), page = 1)
        private val videosDto = PageDto(results = listOf(VideoDto.mock))
    }
}
