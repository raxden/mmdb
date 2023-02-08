package com.raxdenstudios.app.core.domain

import com.google.common.truth.Truth.assertThat
import com.raxdenstudios.app.core.data.MediaRepository
import com.raxdenstudios.app.core.model.Media
import com.raxdenstudios.app.core.model.MediaCategory
import com.raxdenstudios.app.core.model.MediaFilter
import com.raxdenstudios.app.core.model.MediaType
import com.raxdenstudios.commons.ResultData
import com.raxdenstudios.commons.pagination.model.Page
import com.raxdenstudios.commons.pagination.model.PageList
import com.raxdenstudios.commons.pagination.model.PageSize
import com.raxdenstudios.commons.test.rules.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
internal class GetMediasUseCaseTest {

    @ExperimentalCoroutinesApi
    val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(
        testDispatcher = testDispatcher
    )

    private val mediaRepository: MediaRepository = mockk {
        coEvery { medias(any(), any(), any()) } returns ResultData.Success(medias)
    }
    private val useCase: GetMediasUseCase by lazy {
        GetMediasUseCase(
            mediaRepository = mediaRepository,
        )
    }

    @Test
    fun `Should get medias`() = runTest {
        val params = GetMediasUseCase.Params(
            mediaFilter = mediaFilter,
            page = page,
            pageSize = pageSize,
        )

        val result = useCase(params)

        assertThat(result).isEqualTo(ResultData.Success(medias))
    }

    companion object {

        private val mediaType = MediaType.Movie
        private val media = Media.Movie.mock
        private val mediaCategory = MediaCategory.Popular
        private val mediaFilter = MediaFilter(
            mediaType = mediaType,
            mediaCategory = mediaCategory,
        )
        private val page = Page(1)
        private val pageSize = PageSize.defaultSize
        private val medias = PageList<Media>(listOf(media), Page(1))
    }
}
