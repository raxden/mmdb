package com.raxdenstudios.app.core.domain

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.raxdenstudios.app.core.data.FakeMediaRepository
import com.raxdenstudios.app.core.data.MediaRepository
import com.raxdenstudios.app.core.data.RecentSearchRepository
import com.raxdenstudios.app.core.model.Media
import com.raxdenstudios.app.core.model.MediaId
import com.raxdenstudios.app.core.model.MediaType
import com.raxdenstudios.commons.core.ResultData
import com.raxdenstudios.commons.pagination.model.Page
import com.raxdenstudios.commons.pagination.model.PageList
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SearchMediasUseCaseTest {

    private val mediaRepository: MediaRepository = FakeMediaRepository()
    private val recentSearchRepository: RecentSearchRepository = mockk(relaxed = true)
    private lateinit var useCase: SearchMediasUseCase

    @Before
    fun setUp() {
        useCase = SearchMediasUseCase(
            mediaRepository = mediaRepository,
            recentSearchRepository = recentSearchRepository,
        )
    }

    @Test
    fun `Given a query with results, When invoke method is called, Then query is saved as recent search`() = runTest {
        val params = SearchMediasUseCase.Params(query = "query")

        useCase(params).test {
            coVerify { recentSearchRepository.save("query") }
            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `Given a query, When invoke method is called, Then search call is called`() = runTest {
        val params = SearchMediasUseCase.Params(query = "query")

        useCase(params).test {
            val result = awaitItem()
            assertThat(result).isEqualTo(
                ResultData.Success(
                    PageList(
                        page = Page(1),
                        items = listOf(
                            Media.Movie.mock.copy(id = MediaId(1)),
                            Media.Movie.mock.copy(id = MediaId(2)),
                            Media.Movie.mock.copy(id = MediaId(3), watchList = true),
                        ),
                    )
                )
            )

            mediaRepository.addToWatchlist(MediaId(1L), MediaType.Movie)

            val result2 = awaitItem()
            assertThat(result2).isEqualTo(
                ResultData.Success(
                    PageList(
                        page = Page(1),
                        items = listOf(
                            Media.Movie.mock.copy(id = MediaId(1), watchList = true),
                            Media.Movie.mock.copy(id = MediaId(2)),
                            Media.Movie.mock.copy(id = MediaId(3), watchList = true),
                        ),
                    )
                )
            )
            cancelAndConsumeRemainingEvents()
        }
    }
}
