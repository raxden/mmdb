package com.raxdenstudios.app.core.data

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.raxdenstudios.app.core.data.local.datasource.RecentSearchLocalDataSource
import com.raxdenstudios.commons.ResultData
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class RecentSearchRepositoryTest {

    private val recentSearchLocalDataSource: RecentSearchLocalDataSource = mockk()
    private lateinit var repository: RecentSearchRepository

    @Before
    fun setUp() {
        repository = RecentSearchRepository(
            recentSearchLocalDataSource = recentSearchLocalDataSource,
        )
    }

    @Test
    fun `observe should return a flow of result data`() = runTest {
        coEvery { recentSearchLocalDataSource.observe() } returns flowOf(ResultData.Success(listOf("query", "query2")))

        repository.observe().test {
            val result = awaitItem()
            assertThat(result).isEqualTo(ResultData.Success(listOf("query", "query2")))

            cancelAndConsumeRemainingEvents()
        }
    }

    @Test
    fun `save should return a result data`() = runTest {
        coEvery { recentSearchLocalDataSource.add("query") } returns ResultData.Success(true)

        val result = repository.save("query")
        assertThat(result).isEqualTo(ResultData.Success(true))
    }
}
