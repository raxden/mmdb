package com.raxdenstudios.app.core.domain

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.raxdenstudios.app.core.data.FakeMediaRepository
import com.raxdenstudios.app.core.data.MediaRepository
import com.raxdenstudios.app.core.model.Media
import com.raxdenstudios.app.core.model.MediaId
import com.raxdenstudios.app.core.model.MediaType
import com.raxdenstudios.commons.core.ResultData
import com.raxdenstudios.commons.pagination.model.Page
import com.raxdenstudios.commons.pagination.model.PageList
import com.raxdenstudios.commons.coroutines.test.rules.MainDispatcherRule
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

internal class GetRelatedMediasUseCaseTest {

    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(
        testDispatcher = testDispatcher
    )
    private val mediaRepository: MediaRepository = FakeMediaRepository()

    private lateinit var useCase: GetRelatedMediasUseCase

    @Before
    fun setUp() {
        useCase = GetRelatedMediasUseCase(
            mediaRepository = mediaRepository,
        )
    }

    @Test
    fun `should get related medias`() = runTest {
        val params = GetRelatedMediasUseCase.Params(
            mediaId = MediaId(1L),
            mediaType = MediaType.Movie,
        )

        useCase(params).test {
            val result = awaitItem()

            assertThat(result).isEqualTo(
                ResultData.Success(
                    PageList(
                        items = listOf(
                            Media.Movie.mock.copy(id = MediaId(1)),
                            Media.Movie.mock.copy(id = MediaId(2)),
                            Media.Movie.mock.copy(id = MediaId(3), watchList = true),
                        ),
                        page = Page(1)
                    )
                )
            )
        }
    }

    @Test
    fun `should get related medias updated after media is added to watchlist`() = runTest {
        val params = GetRelatedMediasUseCase.Params(
            mediaId = MediaId(1L),
            mediaType = MediaType.Movie,
        )

        useCase(params).test {
            val firstResult = awaitItem()
            assertThat(firstResult).isEqualTo(
                ResultData.Success(
                    PageList(
                        items = listOf(
                            Media.Movie.mock.copy(id = MediaId(1)),
                            Media.Movie.mock.copy(id = MediaId(2)),
                            Media.Movie.mock.copy(id = MediaId(3), watchList = true),
                        ),
                        page = Page(1)
                    )
                )
            )

            mediaRepository.addToWatchlist(MediaId(2L), MediaType.Movie)

            val secondResult = awaitItem()
            assertThat(secondResult).isEqualTo(
                ResultData.Success(
                    PageList(
                        items = listOf(
                            Media.Movie.mock.copy(id = MediaId(1)),
                            Media.Movie.mock.copy(id = MediaId(2), watchList = true),
                            Media.Movie.mock.copy(id = MediaId(3), watchList = true),
                        ),
                        page = Page(1)
                    )
                )
            )
        }
    }
}
