package com.raxdenstudios.app.core.data.remote.datasource

import com.google.common.truth.Truth.assertThat
import com.raxdenstudios.app.core.data.remote.mapper.CertificationDtoToDomainMapper
import com.raxdenstudios.app.core.data.remote.mapper.ContentRatingDtoToDomainMapper
import com.raxdenstudios.app.core.data.remote.mapper.DateDtoToLocalDateMapper
import com.raxdenstudios.app.core.data.remote.mapper.GenreDtoToDomainMapper
import com.raxdenstudios.app.core.data.remote.mapper.LocaleDtoToDomainMapper
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
import com.raxdenstudios.app.core.model.ErrorDomain
import com.raxdenstudios.app.core.model.Media
import com.raxdenstudios.app.core.model.MediaFilter
import com.raxdenstudios.app.core.model.MediaId
import com.raxdenstudios.app.core.model.MediaType
import com.raxdenstudios.app.core.model.Video
import com.raxdenstudios.app.core.network.APIDataProvider
import com.raxdenstudios.app.core.network.APIDataV3Provider
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
import com.raxdenstudios.core.model.Account
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
    private val apiDataProvider: APIDataProvider = APIDataV3Provider()
    private val configProvider: ConfigProvider = mockk {
        every { language } returns "es-ES"
        every { region } returns "ES"
    }
    private val voteDtoToDomainMapper = VoteDtoToDomainMapper()
    private val pictureDtoToDomainMapper = PictureDtoToDomainMapper(
        apiDataProvider = apiDataProvider
    )
    private val dateDtoToLocalDateMapper: DateDtoToLocalDateMapper = mockk {
        every { transform(any<String>()) } returns LocalDate.of(1970, 1, 1)
    }
    private val genreDtoToDomainMapper = GenreDtoToDomainMapper()
    private val localeDtoToDomainMapper = LocaleDtoToDomainMapper()
    private val movieDtoToDomainMapper = MovieDtoToDomainMapper(
        voteDtoToDomainMapper = voteDtoToDomainMapper,
        pictureDtoToDomainMapper = pictureDtoToDomainMapper,
        dateDtoToLocalDateMapper = dateDtoToLocalDateMapper,
        genreDtoToDomainMapper = genreDtoToDomainMapper,
        localeDtoToDomainMapper = localeDtoToDomainMapper,
    )
    private val tvShowDtoToDomainMapper = TVShowDtoToDomainMapper(
        voteDtoToDomainMapper = voteDtoToDomainMapper,
        pictureDtoToDomainMapper = pictureDtoToDomainMapper,
        dateDtoToLocalDateMapper = dateDtoToLocalDateMapper,
        genreDtoToDomainMapper = genreDtoToDomainMapper,
        localeDtoToDomainMapper = localeDtoToDomainMapper,
    )
    private val mediaDtoToDomainMapper = MediaDtoToDomainMapper(
        movieDtoToDomainMapper = movieDtoToDomainMapper,
        tvShowDtoToDomainMapper = tvShowDtoToDomainMapper,
    )
    private val certificationDtoToDomainMapper = CertificationDtoToDomainMapper(
        configProvider = configProvider
    )
    private val movieDetailDtoToDomainMapper = MovieDetailDtoToDomainMapper(
        voteDtoToDomainMapper = voteDtoToDomainMapper,
        pictureDtoToDomainMapper = pictureDtoToDomainMapper,
        dateDtoToLocalDateMapper = dateDtoToLocalDateMapper,
        genreDtoToDomainMapper = genreDtoToDomainMapper,
        certificationDtoToDomainMapper = certificationDtoToDomainMapper,
        localeDtoToDomainMapper = localeDtoToDomainMapper,
    )
    private val contentRatingDtoToDomainMapper = ContentRatingDtoToDomainMapper(
        configProvider = configProvider,
    )
    private val tvShowDetailDtoToDomainMapper = TVShowDetailDtoToDomainMapper(
        voteDtoToDomainMapper = voteDtoToDomainMapper,
        pictureDtoToDomainMapper = pictureDtoToDomainMapper,
        dateDtoToLocalDateMapper = dateDtoToLocalDateMapper,
        genreDtoToDomainMapper = genreDtoToDomainMapper,
        contentRatingDtoToDomainMapper = contentRatingDtoToDomainMapper,
        localeDtoToDomainMapper = localeDtoToDomainMapper,
    )
    private val mediaDetailDtoToDomainMapper = MediaDetailDtoToDomainMapper(
        movieDetailDtoToDomainMapper = movieDetailDtoToDomainMapper,
        tvShowDetailDtoToDomainMapper = tvShowDetailDtoToDomainMapper,
    )
    private val mediaTypeToDtoMapper = MediaTypeToDtoMapper()
    private val videoDtoToDomainMapper = VideoDtoToDomainMapper(
        dateDtoToLocalDateMapper = dateDtoToLocalDateMapper,
    )
    private val networkErrorDtoToErrorMapper = NetworkErrorDtoToErrorMapper()
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
    fun `nowPlaying should return a list of media`() = runTest {
        val mediaFilter = MediaFilter.nowPlaying
        val page = Page(1)
        val account: Account = Account.Guest.mock
        coEvery { mediaGateway.nowPlaying(mediaFilter.mediaType, page) } returns ResultData.Success(moviesDto)

        val result = dataSource.fetch(mediaFilter, page, account)

        assertThat(result).isEqualTo(
            ResultData.Success(
                PageList(
                    items = listOf(
                        Media.Movie.mock.copy(
                            genres = emptyList(),
                            certification = "",
                        )
                    ),
                    page = Page(1),
                )
            )
        )
    }

    @Test
    fun `topRated should return a list of media`() = runTest {
        val mediaFilter = MediaFilter.topRated(MediaType.Movie)
        val page = Page(1)
        val account: Account = Account.Guest.mock
        coEvery { mediaGateway.topRated(mediaFilter.mediaType, page) } returns ResultData.Success(moviesDto)

        val result = dataSource.fetch(mediaFilter, page, account)

        assertThat(result).isEqualTo(
            ResultData.Success(
                PageList(
                    items = listOf(
                        Media.Movie.mock.copy(
                            genres = emptyList(),
                            certification = "",
                        )
                    ),
                    page = Page(1),
                )
            )
        )
    }

    @Test
    fun `upcoming should return a list of media`() = runTest {
        val mediaFilter = MediaFilter.upcoming
        val page = Page(1)
        val account: Account = Account.Guest.mock
        coEvery { mediaGateway.upcoming(mediaFilter.mediaType, page) } returns ResultData.Success(moviesDto)

        val result = dataSource.fetch(mediaFilter, page, account)

        assertThat(result).isEqualTo(
            ResultData.Success(
                PageList(
                    items = listOf(
                        Media.Movie.mock.copy(
                            genres = emptyList(),
                            certification = "",
                        )
                    ),
                    page = Page(1),
                )
            )
        )
    }

    @Test
    fun `watchlist should return a list of media`() = runTest {
        val mediaFilter = MediaFilter.watchlist(MediaType.Movie)
        val page = Page(1)
        val account = Account.Logged.mock
        coEvery {
            watchlistGateway.fetch(mediaFilter.mediaType, page, account.credentials.accountId)
        } returns ResultData.Success(moviesDto)

        val result = dataSource.fetch(mediaFilter, page, account)

        assertThat(result).isEqualTo(
            ResultData.Success(
                PageList(
                    items = listOf(
                        Media.Movie.mock.copy(
                            genres = emptyList(),
                            certification = "",
                            watchList = true,
                        )
                    ),
                    page = Page(1),
                )
            )
        )
    }

    @Test
    fun `watchlist should return an error when account is not logged`() = runTest {
        val mediaFilter = MediaFilter.watchlist(MediaType.Movie)
        val page = Page(1)
        val account = Account.Guest.mock

        val result = dataSource.fetch(mediaFilter, page, account)

        assertThat(result).isEqualTo(
            ResultData.Failure(
                ErrorDomain.Unauthorized("Guest account can't fetch watchlist")
            )
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
                            certification = "",
                        )
                    ),
                    page = Page(1)
                )
            )
        )
    }

    @Test
    fun `results should return a list of medias`() = runTest {
        val query = "query"
        val page = Page(1)
        coEvery { mediaGateway.search(query, page) } returns ResultData.Success(resultsDto)

        val result = dataSource.search(query, page)

        assertThat(result).isEqualTo(
            ResultData.Success(
                PageList(
                    items = listOf(
                        Media.Movie.mock.copy(
                            genres = emptyList(),
                            certification = "",
                        ),
                        Media.TVShow.mock.copy(
                            genres = emptyList(),
                            certification = "",
                        ),
                    ),
                    page = Page(1)
                )
            )
        )
    }

    companion object {

        private val moviesDto = PageDto<MediaDto>(results = listOf(MediaDto.Movie.mock), page = 1)
        private val resultsDto = PageDto(
            results = listOf(
                MediaDto.Movie.mock,
                MediaDto.TVShow.mock,
            ), page = 1
        )
        private val videosDto = PageDto(results = listOf(VideoDto.mock))
    }
}
