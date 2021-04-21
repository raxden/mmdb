package com.raxdenstudios.app.movie.data.remote

import com.haroldadmin.cnradapter.NetworkResponse
import com.raxdenstudios.app.movie.data.remote.model.MovieDto
import com.raxdenstudios.app.movie.data.remote.service.MediaV3Service
import com.raxdenstudios.app.movie.data.remote.service.MediaV4Service
import com.raxdenstudios.app.network.model.PageDto
import com.raxdenstudios.app.test.BaseTest
import com.raxdenstudios.commons.DispatcherFacade
import com.raxdenstudios.commons.ResultData
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.assertEquals
import org.junit.Test
import org.koin.core.module.Module

@ExperimentalCoroutinesApi
internal class MediaGatewayTest : BaseTest() {

  override val modules: List<Module>
    get() = emptyList()

  private val mediaV3Service: MediaV3Service = mockk(relaxed = true)
  private val mediaV4Service: MediaV4Service = mockk(relaxed = true) {
    coEvery { watchList(aAccountId, aCategory, 1) } returns aNetworkResponseSuccessFirstPage
    coEvery { watchList(aAccountId, aCategory, 2) } returns aNetworkResponseSuccessSecondPage
    coEvery { watchList(aAccountId, aCategory, 3) } returns aNetworkResponseSuccessThirdPage
  }

  private val gateway: MediaGateway by lazy {
    MediaGateway(
      dispatcher = object : DispatcherFacade {
        override fun io() = testDispatcher
        override fun default() = testDispatcher
      },
      mediaV3Service = mediaV3Service,
      mediaV4Service = mediaV4Service,
    )
  }

  @Test
  fun `Given a movie results split in pages, When watchList is called with a valid accountId, Then return all movies`() =
    testDispatcher.runBlockingTest {
      val resultData = gateway.watchList(aAccountId, aCategory)

      coVerify {
        mediaV4Service.watchList(aAccountId, aCategory, 1)
        mediaV4Service.watchList(aAccountId, aCategory, 2)
        mediaV4Service.watchList(aAccountId, aCategory, 3)
      }
      assertEquals(
        ResultData.Success(
          listOf(
            MovieDto.empty.copy(id = 1),
            MovieDto.empty.copy(id = 2),
            MovieDto.empty.copy(id = 3),
            MovieDto.empty.copy(id = 4),
            MovieDto.empty.copy(id = 5),
            MovieDto.empty.copy(id = 6),
          )
        ), resultData
      )
    }
}

private const val aCategory = "movie"
private const val aAccountId = "aAccountId"
private val aNetworkResponseSuccessFirstPage = NetworkResponse.Success(
  body = PageDto(
    page = 1,
    total_pages = 3,
    total_results = 6,
    results = listOf(
      MovieDto.empty.copy(id = 1),
      MovieDto.empty.copy(id = 2),
    )
  ),
  headers = null,
  code = 200
)
private val aNetworkResponseSuccessSecondPage = NetworkResponse.Success(
  body = PageDto(
    page = 2,
    total_pages = 3,
    total_results = 6,
    results = listOf(
      MovieDto.empty.copy(id = 3),
      MovieDto.empty.copy(id = 4),
    )
  ),
  headers = null,
  code = 200
)
private val aNetworkResponseSuccessThirdPage = NetworkResponse.Success(
  body = PageDto(
    page = 3,
    total_pages = 3,
    total_results = 6,
    results = listOf(
      MovieDto.empty.copy(id = 5),
      MovieDto.empty.copy(id = 6),
    )
  ),
  headers = null,
  code = 200
)
