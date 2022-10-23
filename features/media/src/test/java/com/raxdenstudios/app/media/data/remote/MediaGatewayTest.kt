package com.raxdenstudios.app.media.data.remote

import com.haroldadmin.cnradapter.NetworkResponse
import com.raxdenstudios.app.media.data.remote.model.MediaDto
import com.raxdenstudios.app.media.data.remote.service.MediaV3Service
import com.raxdenstudios.app.media.data.remote.service.MediaV4Service
import com.raxdenstudios.app.network.model.ErrorDto
import com.raxdenstudios.app.network.model.PageDto
import com.raxdenstudios.app.test.BasePresentationTest
import com.raxdenstudios.commons.DispatcherProvider
import com.raxdenstudios.commons.ResultData
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test
import retrofit2.Response

@ExperimentalCoroutinesApi
internal class MediaGatewayTest : BasePresentationTest() {

    private val mediaV3Service: MediaV3Service = mockk(relaxed = true)
    private val mediaV4Service: MediaV4Service = mockk(relaxed = true) {
        coEvery { watchListMovies(aAccountId, 1) } returns aNetworkResponseSuccessFirstPage
        coEvery { watchListMovies(aAccountId, 2) } returns aNetworkResponseSuccessSecondPage
        coEvery { watchListMovies(aAccountId, 3) } returns aNetworkResponseSuccessThirdPage
    }
    private val dispatcher: DispatcherProvider = object : DispatcherProvider {
        override val main = testDispatcher
        override val io = testDispatcher
        override val default = testDispatcher
    }
    private val gateway: MediaGateway by lazy {
        MediaGateway(
            dispatcher = dispatcher,
            mediaV3Service = mediaV3Service,
            mediaV4Service = mediaV4Service,
        )
    }

    @Test
    fun `Given a movie results splitted in pages, When watchList is called with a valid accountId, Then return all movies`() =
        runTest {
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
private val response: Response<*> = mockk(relaxed = true) {
    every { code() } returns 200
}
private val aNetworkResponseSuccessFirstPage: NetworkResponse<PageDto<MediaDto.Movie>, ErrorDto> =
    NetworkResponse.Success(
        body = PageDto(
            page = 1,
            total_pages = 3,
            total_results = 6,
            results = listOf(
                MediaDto.Movie.empty.copy(id = 1),
                MediaDto.Movie.empty.copy(id = 2),
            )
        ),
        response = response,
    )
private val aNetworkResponseSuccessSecondPage: NetworkResponse<PageDto<MediaDto.Movie>, ErrorDto> =
    NetworkResponse.Success(
        body = PageDto(
            page = 2,
            total_pages = 3,
            total_results = 6,
            results = listOf(
                MediaDto.Movie.empty.copy(id = 3),
                MediaDto.Movie.empty.copy(id = 4),
            )
        ),
        response = response,
    )
private val aNetworkResponseSuccessThirdPage: NetworkResponse<PageDto<MediaDto.Movie>, ErrorDto> =
    NetworkResponse.Success(
        body = PageDto(
            page = 3,
            total_pages = 3,
            total_results = 6,
            results = listOf(
                MediaDto.Movie.empty.copy(id = 5),
                MediaDto.Movie.empty.copy(id = 6),
            )
        ),
        response = response,
    )
