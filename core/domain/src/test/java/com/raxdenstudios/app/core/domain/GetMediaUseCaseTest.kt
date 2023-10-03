package com.raxdenstudios.app.core.domain

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.raxdenstudios.app.core.data.MediaRepository
import com.raxdenstudios.app.core.model.Media
import com.raxdenstudios.app.core.model.MediaId
import com.raxdenstudios.app.core.model.MediaType
import com.raxdenstudios.commons.core.ResultData
import com.raxdenstudios.commons.coroutines.test.rules.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk

import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test


internal class GetMediaUseCaseTest {

    @ExperimentalCoroutinesApi
    val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(
        testDispatcher = testDispatcher
    )

    private val mediaRepository: MediaRepository = mockk {
        coEvery { fetchById(any(), any()) } returns flowOf(ResultData.Success(media))
    }
    private val useCase: GetMediaUseCase by lazy {
        GetMediaUseCase(
            mediaRepository = mediaRepository,
        )
    }

    @Test
    fun `Should get media`() = runTest {
        val params = GetMediaUseCase.Params(
            mediaId = mediaId,
            mediaType = mediaType,
        )

        useCase(params).test {
            val mediaResult = awaitItem()
            assertThat(mediaResult).isEqualTo(ResultData.Success(media))
            awaitComplete()
        }
    }

    companion object {

        private val mediaId = MediaId(1)
        private val mediaType = MediaType.Movie
        private val media = Media.Movie.mock
    }
}
