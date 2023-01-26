package com.raxdenstudios.app.core.domain

import com.google.common.truth.Truth.assertThat
import com.raxdenstudios.app.core.data.MediaRepository
import com.raxdenstudios.app.core.model.MediaId
import com.raxdenstudios.app.core.model.MediaType
import com.raxdenstudios.commons.ResultData
import com.raxdenstudios.commons.test.rules.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class RemoveMediaFromWatchlistUseCaseTest {

    @ExperimentalCoroutinesApi
    val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(
        testDispatcher = testDispatcher
    )

    private val mediaRepository: MediaRepository = mockk {
        coEvery { removeFromWatchlist(any(), any()) } returns ResultData.Success(true)
    }
    private val useCase: RemoveMediaFromWatchlistUseCase by lazy {
        RemoveMediaFromWatchlistUseCase(
            mediaRepository = mediaRepository,
        )
    }

    @Test
    fun `Should remove media from watchlist`() = runTest {
        val params = RemoveMediaFromWatchlistUseCase.Params(
            mediaId = mediaId,
            mediaType = mediaType,
        )

        val result = useCase(params)

        assertThat(result).isEqualTo(ResultData.Success(true))
        coVerify(exactly = 1) { mediaRepository.removeFromWatchlist(mediaId, mediaType) }
    }

    companion object {

        private val mediaId = MediaId(1)
        private val mediaType = MediaType.Movie
    }
}
