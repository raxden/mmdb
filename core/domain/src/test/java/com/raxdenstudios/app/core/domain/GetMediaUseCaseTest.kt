package com.raxdenstudios.app.core.domain

import com.google.common.truth.Truth.assertThat
import com.raxdenstudios.app.core.data.MediaRepository
import com.raxdenstudios.app.core.model.Media
import com.raxdenstudios.app.core.model.MediaId
import com.raxdenstudios.app.core.model.MediaType
import com.raxdenstudios.commons.ResultData
import com.raxdenstudios.commons.test.rules.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class GetMediaUseCaseTest {

    @ExperimentalCoroutinesApi
    val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(
        testDispatcher = testDispatcher
    )

    private val mediaRepository: MediaRepository = mockk {
        coEvery { fetchById(any(), any()) } returns ResultData.Success(media)
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

        val result = useCase(params)

        assertThat(result).isEqualTo(ResultData.Success(media))
    }

    companion object {

        private val mediaId = MediaId(1)
        private val mediaType = MediaType.Movie
        private val media = Media.Movie.empty
    }
}
