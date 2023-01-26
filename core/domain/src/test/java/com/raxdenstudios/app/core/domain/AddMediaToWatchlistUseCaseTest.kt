package com.raxdenstudios.app.core.domain

import com.google.common.truth.Truth.assertThat
import com.raxdenstudios.app.core.data.MediaRepository
import com.raxdenstudios.app.core.model.Media
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

@ExperimentalCoroutinesApi
internal class AddMediaToWatchlistUseCaseTest {

    @ExperimentalCoroutinesApi
    val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(
        testDispatcher = testDispatcher
    )

    private val mediaRepository: MediaRepository = mockk {
        coEvery { addToWatchlist(any(), any()) } returns ResultData.Success(media)
    }
    private val useCase: AddMediaToWatchlistUseCase by lazy {
        AddMediaToWatchlistUseCase(
            mediaRepository = mediaRepository,
        )
    }

    @Test
    fun `Should add media to watchlist`() = runTest {
        val params = AddMediaToWatchlistUseCase.Params(
            mediaId = mediaId,
            mediaType = mediaType,
        )

        val result = useCase(params)

        assertThat(result).isEqualTo(ResultData.Success(media))
        coVerify(exactly = 1) { mediaRepository.addToWatchlist(mediaId, mediaType) }
    }

    companion object {

        private val mediaId = MediaId(1)
        private val mediaType = MediaType.Movie
        private val media = Media.Movie.empty.copy(id = MediaId(1))
    }
}
