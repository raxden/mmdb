package com.raxdenstudios.app.media.data.remote.datasource

import com.raxdenstudios.app.account.domain.model.Account
import com.raxdenstudios.app.account.domain.model.Credentials
import com.raxdenstudios.app.media.data.remote.MediaGateway
import com.raxdenstudios.app.media.data.remote.exception.UserNotLoggedException
import com.raxdenstudios.app.media.data.remote.mapper.MediaDtoToDomainMapper
import com.raxdenstudios.app.media.data.remote.mapper.MediaTypeToDtoMapper
import com.raxdenstudios.app.media.data.remote.model.MediaDto
import com.raxdenstudios.app.media.domain.model.Media
import com.raxdenstudios.app.media.domain.model.MediaFilter
import com.raxdenstudios.app.media.domain.model.MediaId
import com.raxdenstudios.app.media.domain.model.MediaType
import com.raxdenstudios.app.network.model.PageDto
import com.raxdenstudios.app.test.BasePresentationTest
import com.raxdenstudios.commons.ResultData
import com.raxdenstudios.commons.pagination.model.Page
import com.raxdenstudios.commons.pagination.model.PageList
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Ignore
import org.junit.Test

@ExperimentalCoroutinesApi
internal class MediaRemoteDataSourceTest : BasePresentationTest() {

    private val mediaGateway: MediaGateway = mockk {
        coEvery { upcoming(any()) } returns ResultData.Success(aPageDto)
        coEvery { topRatedMovies(any()) } returns ResultData.Success(aPageDto)
        coEvery { popularMovies(any()) } returns ResultData.Success(aPageDto)
        coEvery { nowPlaying(any()) } returns ResultData.Success(aPageDto)
        coEvery {
            watchListMovies(aAccountLogged.credentials.accountId, any())
        } returns ResultData.Success(aPageDto)
        coEvery {
            watchListMovies(aAccountLogged.credentials.accountId)
        } returns ResultData.Success(aPageDtoList)
        coEvery { detailMovie(aMediaId) } returns ResultData.Success(aMediaDto)
        coEvery {
            addToWatchList(aTMDBAccountId, "movie", aMediaId)
        } returns ResultData.Success(true)
        coEvery {
            removeFromWatchList(aTMDBAccountId, "movie", aMediaId)
        } returns ResultData.Success(true)
    }
    private val mediaDtoToDomainMapper: MediaDtoToDomainMapper = mockk()
    private val mediaTypeToDtoMapper: MediaTypeToDtoMapper = mockk()
    private val dataSource: MediaRemoteDataSource by lazy {
        MediaRemoteDataSource(
            mediaGateway = mediaGateway,
            mediaTypeToDtoMapper = mediaTypeToDtoMapper,
            mediaDtoToDomainMapper = mediaDtoToDomainMapper,
        )
    }

    @Test
    @Ignore
    fun `Given a mediaId and mediaType, When mediaById is called, Then returns a ResultData success with Media`() =
        runTest {
            val result = dataSource.findById(aMediaId, aMediaType)

            assertEquals(
                ResultData.Success(Media.Movie.empty.copy(id = MediaId(1))),
                result
            )
        }

    @Test
    @Ignore
    fun `Given a account logged, mediaId and mediaType, When addMediaToWatchList is called, Then returns a ResultData success with Media`() =
        runTest {
            val result = dataSource.addToWatchList(aAccountLogged, aMediaType, aMediaId)

            assertEquals(
                ResultData.Success(
                    Media.Movie.empty.copy(
                        id = MediaId(1),
                        watchList = true
                    )
                ),
                result
            )
        }

    @Test
    @Ignore
    fun `Given a account logged, mediaId and mediaType, When removeMediaFromWatchList is called, Then returns a ResultData success`() =
        runTest {
            val result = dataSource.removeFromWatchList(aAccountLogged, aMediaType, aMediaId)

            assertEquals(
                ResultData.Success(true),
                result
            )
        }

    @Test
    @Ignore
    fun `Given a account logged and mediaType, When watchList is called, Then returns a List of Media's`() =
        runTest {
            val result = dataSource.watchList(aAccountLogged, aMediaType)

            assertEquals(
                ResultData.Success(
                    listOf(
                        Media.Movie.empty.copy(id = MediaId(1), watchList = true),
                        Media.Movie.empty.copy(id = MediaId(2), watchList = true)
                    )
                ),
                result
            )
        }

    @Test
    @Ignore
    fun `Given a mediaFilter of type upcoming, When movies is called, Then load movies`() =
        runTest {

            val result = dataSource.medias(MediaFilter.Upcoming, aAccountLogged, aFirstPage)

            assertEquals(
                ResultData.Success(
                    PageList(
                        items = listOf(
                            Media.Movie.empty.copy(id = MediaId(1)),
                            Media.Movie.empty.copy(id = MediaId(2)),
                        ),
                        page = Page(1)
                    )
                ), result
            )
        }

    @Test
    @Ignore
    fun `Given a mediaFilter of type popular, When movies is called, Then load movies`() =
        runTest {

            val result = dataSource.medias(MediaFilter.popularMovies, aAccountLogged, aFirstPage)

            assertEquals(
                ResultData.Success(
                    PageList(
                        items = listOf(
                            Media.Movie.empty.copy(id = MediaId(1)),
                            Media.Movie.empty.copy(id = MediaId(2)),
                        ),
                        page = Page(1)
                    )
                ), result
            )
        }

    @Test
    @Ignore
    fun `Given a mediaFilter of type topRated, When movies is called, Then load movies`() =
        runTest {

            val result = dataSource.medias(MediaFilter.topRatedMovies, aAccountLogged, aFirstPage)

            assertEquals(
                ResultData.Success(
                    PageList(
                        items = listOf(
                            Media.Movie.empty.copy(id = MediaId(1)),
                            Media.Movie.empty.copy(id = MediaId(2)),
                        ),
                        page = Page(1)
                    )
                ), result
            )
        }

    @Test
    @Ignore
    fun `Given a mediaFilter of type nowPlaying, When movies is called, Then load movies`() =
        runTest {

            val result = dataSource.medias(MediaFilter.NowPlaying, aAccountLogged, aFirstPage)

            assertEquals(
                ResultData.Success(
                    PageList(
                        items = listOf(
                            Media.Movie.empty.copy(id = MediaId(1)),
                            Media.Movie.empty.copy(id = MediaId(2)),
                        ),
                        page = Page(1)
                    )
                ), result
            )
        }

    @Test
    @Ignore
    fun `Given a mediaFilter of type watchlist and account logged, When movies is called, Then load movies`() =
        runTest {

            val result = dataSource.medias(MediaFilter.watchListMovies, aAccountLogged, aFirstPage)

            assertEquals(
                ResultData.Success(
                    PageList(
                        items = listOf(
                            Media.Movie.empty.copy(id = MediaId(1), watchList = true),
                            Media.Movie.empty.copy(id = MediaId(2), watchList = true),
                        ),
                        page = Page(1)
                    )
                ), result
            )
        }

    @Test
    @Ignore
    fun `Given a mediaFilter of type watchlist and account not logged, When movies is called, Then UserNotLoggedException is returned`() =
        runTest {

            val result = dataSource.medias(MediaFilter.watchListMovies, aAccountGuest, aFirstPage)

            result as ResultData.Error
            assert(result.throwable is UserNotLoggedException)
        }
}

private const val aTMDBAccountId = "aTMDBAccountId"
private val aMediaId = MediaId(1L)
private val aMediaDto = MediaDto.Movie.empty.copy(id = 1)
private val aMediaType = MediaType.MOVIE
private val aPageDtoList = listOf(
    MediaDto.Movie.empty.copy(id = 1),
    MediaDto.Movie.empty.copy(id = 2),
)
private val aPageDto = PageDto(
    page = 1,
    total_pages = 1,
    total_results = 2,
    results = aPageDtoList,
)
private val aFirstPage = Page(1)
private val aAccountGuest = Account.Guest.empty
private val aAccountLogged =
    Account.Logged.empty.copy(credentials = Credentials.empty.copy(accountId = aTMDBAccountId))
