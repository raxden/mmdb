package com.raxdenstudios.app.core.domain

import com.google.common.truth.Truth.assertThat
import com.raxdenstudios.app.core.data.MediaRepository
import com.raxdenstudios.app.core.model.MediaId
import com.raxdenstudios.app.core.model.MediaType
import com.raxdenstudios.app.core.model.Video
import com.raxdenstudios.commons.core.ResultData
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

internal class GetMediaVideosUseCaseTest {

    private val mediaRepository: MediaRepository = mockk()
    private lateinit var useCase: GetMediaVideosUseCase

    @Before
    fun setUp() {
        useCase = GetMediaVideosUseCase(
            mediaRepository = mediaRepository,
        )
    }

    @Test
    fun `Given a valid params, When invoke is called Then return ResultData with success`() = runTest {
        val params = GetMediaVideosUseCase.Params(
            mediaId = MediaId(1L),
            mediaType = MediaType.Movie,
        )
        coEvery { mediaRepository.videos(any(), any()) } returns ResultData.Success(listOf(Video.mock))

        val result = useCase(params)

        assertThat(result).isEqualTo(ResultData.Success(listOf(Video.mock)))
    }
}
