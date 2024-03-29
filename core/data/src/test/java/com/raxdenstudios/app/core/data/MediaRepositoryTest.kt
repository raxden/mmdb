package com.raxdenstudios.app.core.data

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.raxdenstudios.app.core.model.Media
import com.raxdenstudios.app.core.model.MediaFilter
import com.raxdenstudios.app.core.model.MediaId
import com.raxdenstudios.app.core.model.MediaType
import com.raxdenstudios.app.core.model.Video
import com.raxdenstudios.commons.coroutines.DispatcherProvider
import com.raxdenstudios.commons.core.ResultData
import com.raxdenstudios.commons.pagination.model.Page
import com.raxdenstudios.commons.pagination.model.PageList
import com.raxdenstudios.commons.pagination.model.PageSize
import com.raxdenstudios.commons.coroutines.test.rules.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class MediaRepositoryTest {

    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(
        testDispatcher = testDispatcher
    )

    private val dispatcherProvider: DispatcherProvider = object : DispatcherProvider {
        override val main = testDispatcher
        override val default = testDispatcher
        override val io = testDispatcher
    }
    private val mediaDataSource: MediaDataSource = mockk {
        coEvery { fetch(any(), firstPage, aPageSize) } returns ResultData.Success(aPageList)
        coEvery { fetchById(any(), any()) } returns ResultData.Success(movie)
    }
    private val watchlistDataSource: WatchlistDataSource = mockk {
        coEvery { observe() } returns flowOf(ResultData.Success(aMovies))
        coEvery { observe(any(), any()) } returns flowOf(ResultData.Success(movie))
    }
    private val repository: MediaRepository = MediaRepositoryImpl(
        dispatcher = dispatcherProvider,
        mediaDataSource = mediaDataSource,
        watchlistDataSource = watchlistDataSource,
    )

    @Test
    fun `Given a movies returned by the server, When movies are called, Then movies are returned`() =
        runTest {
            val result = repository.medias(MediaFilter.popular(MediaType.Movie), firstPage, aPageSize)

            assertThat(result).isEqualTo(
                ResultData.Success(
                    PageList(
                        items = listOf(
                            Media.Movie.mock.copy(id = MediaId(1), watchList = true),
                            Media.Movie.mock.copy(id = MediaId(2), watchList = true),
                        ),
                        page = Page(1),
                    )
                )
            )
        }

    @Test
    fun `Given a movie added to watchlist, When fetchById is called, Then return movie marked as watchlist`() =
        runTest {
            val mediaId = MediaId(1L)
            val mediaType = MediaType.Movie

            repository.fetchById(mediaId, mediaType).test {
                val result = awaitItem()

                assertThat(result).isEqualTo(
                    ResultData.Success(Media.Movie.mock.copy(id = MediaId(1), watchList = true))
                )
                awaitComplete()
            }
        }

    @Test
    fun `Given a mediaId and mediaType, When videos is called, Then return a list of videos`() = runTest {
        val mediaId = MediaId(1L)
        val mediaType = MediaType.Movie
        coEvery {
            mediaDataSource.videos(mediaId, mediaType)
        } returns ResultData.Success(listOf(Video.mock))

        val result = repository.videos(mediaId, mediaType)

        assertThat(result).isEqualTo(ResultData.Success(listOf(Video.mock)))
    }

    @Test
    fun `Given a query and page, When search is called, Then result results`() = runTest {
        val query = "query"
        val page = Page(1)
        val pageSize = PageSize(10)
        coEvery { mediaDataSource.search(query, page, pageSize) } returns ResultData.Success(pageListSearchResults)
        coEvery { watchlistDataSource.observe() } returns flowOf(ResultData.Success(emptyList()))

        val result = repository.search(query, page, pageSize)

        assertThat(result).isEqualTo(
            ResultData.Success(
                PageList(
                    items = listOf(
                        Media.Movie.mock.copy(id = MediaId(1)),
                        Media.TVShow.mock.copy(id = MediaId(2)),
                    ),
                    page = Page(1),
                )
            )
        )
    }

    companion object {

        private val aMovies = listOf(
            Media.Movie.mock.copy(id = MediaId(1)),
            Media.Movie.mock.copy(id = MediaId(2)),
        )
        private val searchResults = listOf(
            Media.Movie.mock.copy(id = MediaId(1)),
            Media.TVShow.mock.copy(id = MediaId(2)),
        )
        private val movie = Media.Movie.mock.copy(id = MediaId(1))
        private val firstPage = Page(1)
        private val aPageSize = PageSize.defaultSize
        private val aPageList = PageList<Media>(
            items = aMovies,
            page = firstPage,
        )
        private val pageListSearchResults = PageList(
            items = searchResults,
            page = firstPage,
        )
    }
}
