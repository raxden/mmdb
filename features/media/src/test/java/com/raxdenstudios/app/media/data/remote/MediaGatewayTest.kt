package com.raxdenstudios.app.media.data.remote

import com.haroldadmin.cnradapter.NetworkResponse
import com.raxdenstudios.app.media.data.remote.model.MediaDto
import com.raxdenstudios.app.media.data.remote.service.MediaV3Service
import com.raxdenstudios.app.media.data.remote.service.MediaV4Service
import com.raxdenstudios.app.media.di.mediaDataModule
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
import org.koin.core.component.inject
import org.koin.core.module.Module
import org.koin.dsl.module

@ExperimentalCoroutinesApi
internal class MediaGatewayTest : BaseTest() {

  private val mediaV3Service: MediaV3Service = mockk(relaxed = true)
  private val mediaV4Service: MediaV4Service = mockk(relaxed = true) {
    coEvery { watchListMovies(aAccountId, 1) } returns aNetworkResponseSuccessFirstPage
    coEvery { watchListMovies(aAccountId, 2) } returns aNetworkResponseSuccessSecondPage
    coEvery { watchListMovies(aAccountId, 3) } returns aNetworkResponseSuccessThirdPage
  }
  private val dispatcher: DispatcherFacade = object : DispatcherFacade {
    override fun io() = testDispatcher
    override fun default() = testDispatcher
  }

  override val modules: List<Module>
    get() = listOf(
      mediaDataModule,
      module {
        factory(override = true) { dispatcher }
        factory(override = true) { mediaV3Service }
        factory(override = true) { mediaV4Service }
      }
    )

  private val gateway: MediaGateway by inject()

  @Test
  fun `Given a movie results splitted in pages, When watchList is called with a valid accountId, Then return all movies`() =
    testDispatcher.runBlockingTest {
      val resultData = gateway.watchListMovies(aAccountId)

      coVerify {
        mediaV4Service.watchListMovies(aAccountId, 1)
        mediaV4Service.watchListMovies(aAccountId, 2)
        mediaV4Service.watchListMovies(aAccountId, 3)
      }
      assertEquals(
        ResultData.Success(
          listOf(
            MediaDto.Movie.empty.copy(id = 1),
            MediaDto.Movie.empty.copy(id = 2),
            MediaDto.Movie.empty.copy(id = 3),
            MediaDto.Movie.empty.copy(id = 4),
            MediaDto.Movie.empty.copy(id = 5),
            MediaDto.Movie.empty.copy(id = 6),
          )
        ), resultData
      )
    }
}

private const val aAccountId = "aAccountId"
private val aNetworkResponseSuccessFirstPage = NetworkResponse.Success(
  body = PageDto(
    page = 1,
    total_pages = 3,
    total_results = 6,
    results = listOf(
      MediaDto.Movie.empty.copy(id = 1),
      MediaDto.Movie.empty.copy(id = 2),
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
      MediaDto.Movie.empty.copy(id = 3),
      MediaDto.Movie.empty.copy(id = 4),
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
      MediaDto.Movie.empty.copy(id = 5),
      MediaDto.Movie.empty.copy(id = 6),
    )
  ),
  headers = null,
  code = 200
)
