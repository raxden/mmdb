package com.raxdenstudios.app.core.domain

import com.google.common.truth.Truth.assertThat
import com.raxdenstudios.app.core.data.MediaRepository
import com.raxdenstudios.app.core.model.Media
import com.raxdenstudios.commons.ResultData
import com.raxdenstudios.commons.pagination.model.Page
import com.raxdenstudios.commons.pagination.model.PageList
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test

class SearchMediasUseCaseTest {

    private val mediaRepository: MediaRepository = mockk()
    private lateinit var useCase: SearchMediasUseCase

    @Before
    fun setUp() {
        useCase = SearchMediasUseCase(
            mediaRepository = mediaRepository,
        )
    }

    @Test
    fun `Given a query, When invoke method is called, Then search call is called`() = runTest {
        val params = SearchMediasUseCase.Params(
            query = "query",
        )
        coEvery { mediaRepository.search(params.query, params.page, params.pageSize) } returns searchResultSuccess

        val result = useCase(params)

        assertThat(result).isEqualTo(
            ResultData.Success(
                PageList(
                    page = Page(1),
                    items = listOf(
                        Media.Movie.mock,
                        Media.TVShow.mock,
                    ),
                )
            )
        )
    }

    companion object {

        private val searchResultSuccess = ResultData.Success(
            PageList(
                page = Page(1),
                items = listOf(
                    Media.Movie.mock,
                    Media.TVShow.mock,
                ),
            )
        )
    }
}
