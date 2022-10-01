package com.raxdenstudios.app.core.data

import com.google.common.truth.Truth.assertThat
import com.raxdenstudios.app.core.model.Media
import com.raxdenstudios.app.core.model.MediaFilter
import com.raxdenstudios.app.core.model.MediaId
import com.raxdenstudios.app.core.model.MediaType
import com.raxdenstudios.commons.DispatcherProvider
import com.raxdenstudios.commons.ResultData
import com.raxdenstudios.commons.pagination.model.Page
import com.raxdenstudios.commons.pagination.model.PageList
import com.raxdenstudios.commons.pagination.model.PageSize
import com.raxdenstudios.commons.test.rules.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MediaRepositoryTest {

    @ExperimentalCoroutinesApi
    val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(testDispatcher)

    private val dispatcherProvider: DispatcherProvider = object : DispatcherProvider {
        override val main = testDispatcher
        override val default = testDispatcher
        override val io = testDispatcher
    }
    private val mediaDataSource: MediaDataSource = mockk {
        coEvery { fetch(any(), firstPage, aPageSize) } returns ResultData.Success(aPageList)
    }
    private val watchlistDataSource: WatchlistDataSource = mockk {
        coEvery { fetch() } returns ResultData.Success(aMovies)
    }
    private val repository: MediaRepository = MediaRepository(
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
                            Media.Movie.empty.copy(id = MediaId(1), watchList = true),
                            Media.Movie.empty.copy(id = MediaId(2), watchList = true),
                        ),
                        page = Page(1),
                    )
                )
            )
        }

    companion object {

        private val aMovies = listOf(
            Media.Movie.empty.copy(id = MediaId(1)),
            Media.Movie.empty.copy(id = MediaId(2)),
        )
        private val firstPage = Page(1)
        private val aPageSize = PageSize.defaultSize
        private val aPageList = PageList<Media>(
            items = aMovies,
            page = firstPage,
        )
    }
}