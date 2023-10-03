package com.raxdenstudios.app.core.data

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.raxdenstudios.app.core.data.local.datasource.AccountLocalDataSource
import com.raxdenstudios.app.core.data.local.datasource.WatchlistLocalDataSource
import com.raxdenstudios.app.core.data.remote.datasource.MediaRemoteDataSource
import com.raxdenstudios.app.core.model.Media
import com.raxdenstudios.app.core.model.MediaId
import com.raxdenstudios.app.core.model.MediaType
import com.raxdenstudios.commons.core.ResultData
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Test

class WatchlistDataSourceTest {

    private val accountLocalDataSource: AccountLocalDataSource = mockk {
        coEvery { getAccount() } returns aAccountLogged
    }
    private val mediaRemoteDataSource: MediaRemoteDataSource = mockk {
        coEvery { fetchWatchlist(any(), any()) } returns ResultData.Success(aMovies)
    }
    private val watchListLocalDataSource: WatchlistLocalDataSource = mockk {
        coEvery { observe(any<MediaType>()) } returns flowOf(ResultData.Success(aMovies))
        coEvery { list(any()) } returns ResultData.Success(aMovies)
        coEvery { init(any()) } returns ResultData.Success(true)
    }
    private val dataSource: WatchlistDataSource = WatchlistDataSourceImpl(
        watchListLocalDataSource = watchListLocalDataSource,
        mediaRemoteDataSource = mediaRemoteDataSource,
        accountLocalDataSource = accountLocalDataSource,
    )

    @Test
    fun `Given a mediaType and an empty local watchlist, When observe is called, Then update local watchLists from remote datasource and return the results`() =
        runTest {
            val mediaType = MediaType.Movie

            coEvery {
                watchListLocalDataSource.list(mediaType)
            } returns ResultData.Success(emptyList())

            dataSource.observe(mediaType).test {

                val result = awaitItem()
                assertThat(result).isEqualTo(ResultData.Success(aMovies))

                coVerify(exactly = 1) { mediaRemoteDataSource.fetchWatchlist(any(), any()) }
                coVerify(exactly = 1) { watchListLocalDataSource.init(aMovies) }

                awaitComplete()
            }
        }

    @Test
    fun `Given a mediaType, When observe is called, Then return the results`() = runTest {
        val mediaType = MediaType.Movie

        dataSource.observe(mediaType).test {

            val result = awaitItem()
            assertThat(result).isEqualTo(ResultData.Success(aMovies))

            coVerify(exactly = 0) { mediaRemoteDataSource.fetchWatchlist(any(), any()) }
            coVerify(exactly = 0) { watchListLocalDataSource.init(any()) }

            awaitComplete()
        }
    }

    companion object {

        private val aMovies = listOf(
            Media.Movie.mock.copy(id = MediaId(1)),
            Media.Movie.mock.copy(id = MediaId(2)),
        )
        private const val aCredentialsAccountId = "aCredentialsAccountId"
        private val aAccountLogged = com.raxdenstudios.core.model.Account.Logged.empty.copy(
            credentials = com.raxdenstudios.core.model.Credentials.empty.copy(
                accountId = aCredentialsAccountId
            )
        )
    }
}
