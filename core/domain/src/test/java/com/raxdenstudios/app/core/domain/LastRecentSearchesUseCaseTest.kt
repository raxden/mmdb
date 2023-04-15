package com.raxdenstudios.app.core.domain

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.raxdenstudios.app.core.data.RecentSearchRepository
import com.raxdenstudios.commons.ResultData
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class LastRecentSearchesUseCaseTest {

    private val recentSearchRepository: RecentSearchRepository = mockk()
    private lateinit var useCase: LastRecentSearchesUseCase

    @Before
    fun setUp() {
        useCase = LastRecentSearchesUseCase(recentSearchRepository = recentSearchRepository)
    }

    @Test
    fun `should return the last 5 recent searches`() = runTest {
        coEvery { recentSearchRepository.observe() } returns flowOf(ResultData.Success(RECENT_SEARCHES))

        useCase().test {
            val result = awaitItem()
            assertThat(result).isEqualTo(
                ResultData.Success(
                    listOf(
                        "recent search 6",
                        "recent search 7",
                        "recent search 8",
                        "recent search 9",
                        "recent search 10",
                    )
                )
            )
            cancelAndConsumeRemainingEvents()
        }
    }

    companion object {
        private val RECENT_SEARCHES = listOf(
            "recent search 1",
            "recent search 2",
            "recent search 3",
            "recent search 4",
            "recent search 5",
            "recent search 6",
            "recent search 7",
            "recent search 8",
            "recent search 9",
            "recent search 10",
        )
    }
}
