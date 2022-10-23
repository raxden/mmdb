package com.raxdenstudios.app.media.data.repository

import com.raxdenstudios.app.account.data.local.datasource.AccountLocalDataSource
import com.raxdenstudios.app.account.domain.model.Account
import com.raxdenstudios.app.account.domain.model.Credentials
import com.raxdenstudios.app.media.data.local.datasource.MediaLocalDataSource
import com.raxdenstudios.app.media.data.remote.datasource.MediaRemoteDataSource
import com.raxdenstudios.app.media.domain.model.Media
import com.raxdenstudios.app.media.domain.model.MediaFilter
import com.raxdenstudios.app.media.domain.model.MediaId
import com.raxdenstudios.app.media.domain.model.MediaType
import com.raxdenstudios.app.test.BasePresentationTest
import com.raxdenstudios.commons.ResultData
import com.raxdenstudios.commons.pagination.model.Page
import com.raxdenstudios.commons.pagination.model.PageList
import com.raxdenstudios.commons.pagination.model.PageSize
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

@ExperimentalCoroutinesApi
internal class MediaRepositoryImplTest : BasePresentationTest() {

    private val accountLocalDataSource: AccountLocalDataSource = mockk {
        coEvery { getAccount() } returns aAccountLogged
    }
    private val mediaRemoteDataSource: MediaRemoteDataSource = mockk {
        coEvery {
            addToWatchList(aAccountLogged, aMediaType, aMediaId)
        } returns ResultData.Success(aMovie.copy(watchList = true))
        coEvery {
            removeFromWatchList(aAccountLogged, aMediaType, aMediaId)
        } returns ResultData.Success(true)
        coEvery {
            medias(any(), aAccountLogged, aPage)
        } returns ResultData.Success(aPageList)
        coEvery {
            findById(aMediaId, aMediaType)
        } returns ResultData.Success(Media.Movie.empty.copy(id = aMediaId))
        coEvery {
            watchList(aAccountLogged, aMediaType)
        } returns ResultData.Success(aMovies)
    }
    private val mediaLocalDataSource: MediaLocalDataSource = mockk {
        coEvery { watchList(any()) } returns flowOf(ResultData.Success(aMovies))
        coEvery { clearWatchList() } returns ResultData.Success(true)
        coEvery { containsInWatchList(MediaId(any())) } returns false
        coEvery { addToWatchList(any<Media>()) } returns ResultData.Success(true)
        coEvery { addToWatchList(any<List<Media>>()) } returns ResultData.Success(true)
        coEvery { removeFromWatchList(MediaId(any())) } returns Unit
    }
    private val repository: MediaRepository by lazy {
        MediaRepositoryImpl(
            mediaRemoteDataSource = mediaRemoteDataSource,
            mediaLocalDataSource = mediaLocalDataSource,
            accountLocalDataSource = accountLocalDataSource,
        )
    }

    @Test
    fun `Given a list of medias, When addToLocalWatchList is called, Then medias are persisted`() =
        runTest {
            val result = repository.addToLocalWatchList(aMovies)

            coVerify { mediaLocalDataSource.addToWatchList(aMovies) }
            assertEquals(ResultData.Success(true), result)
        }

    @Test
    fun `Given a movie mediaType, When watchList is called, Then watchList is recovered from local, and updated from remote`() =
        runTest {
            coEvery {
                mediaLocalDataSource.watchList(MediaType.MOVIE)
            } returns flowOf(ResultData.Success(listOf(Media.Movie.withId(MediaId(1L)))))

            val flow = repository.watchList(MediaType.MOVIE)

            assertEquals(
                listOf(
                    ResultData.Success(
                        listOf(
                            Media.Movie.empty.copy(id = MediaId(1)),
                        )
                    ),
                    ResultData.Success(
                        listOf(
                            Media.Movie.empty.copy(id = MediaId(1)),
                            Media.Movie.empty.copy(id = MediaId(2)),
                        )
                    )
                ), flow.take(2).toList()
            )
        }

    @Test
    fun `Given a movie, When addMovieToWatchList is called, Then movie is added to watchlist`() =
        runTest {
            val result = repository.addToWatchList(aMediaId, aMediaType)

            coVerify { mediaLocalDataSource.addToWatchList(aMovie.copy(watchList = true)) }
            assertEquals(ResultData.Success(aMovie.copy(watchList = true)), result)
        }

    @Test
    fun `Given a movie, When removeMovieFromWatchList is called, Then movie is removed`() =
        runTest {
            val result = repository.removeFromWatchList(aMediaId, aMediaType)

            coVerify { mediaLocalDataSource.removeFromWatchList(aMediaId) }
            assertEquals(ResultData.Success(true), result)
        }

    @Test
    fun `Given a movies returned by the server, When movies are called, Then movies returned should be marked as watched if requires`() =
        runTest {
            coEvery { mediaLocalDataSource.containsInWatchList(MediaId(2L)) } returns true

            val result = repository.medias(MediaFilter.popularMovies, aPage, aPageSize)

            assertEquals(
                ResultData.Success(
                    PageList(
                        items = listOf(
                            Media.Movie.empty.copy(id = MediaId(1)),
                            Media.Movie.empty.copy(id = MediaId(2), watchList = true),
                        ),
                        page = Page(1),
                    )
                ),
                result
            )
        }
}

private val aMediaType = MediaType.MOVIE
private val aMovies = listOf(
    Media.Movie.empty.copy(id = MediaId(1)),
    Media.Movie.empty.copy(id = MediaId(2)),
)
private val aPage = Page(1)
private val aPageSize = PageSize.defaultSize
private val aPageList = PageList<Media>(
    items = aMovies,
    page = aPage,
)
private const val aCredentialsAccountId = "aCredentialsAccountId"
private val aAccountGuest = Account.Guest.empty
private val aAccountLogged = Account.Logged.empty.copy(
    credentials = Credentials.empty.copy(
        accountId = aCredentialsAccountId
    )
)
private val aMediaId = MediaId(1)
private val aMovie = Media.Movie.empty.copy(id = aMediaId)
