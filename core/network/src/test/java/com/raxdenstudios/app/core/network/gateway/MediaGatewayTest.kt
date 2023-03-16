package com.raxdenstudios.app.core.network.gateway

import com.google.common.truth.Truth.assertThat
import com.haroldadmin.cnradapter.NetworkResponse
import com.raxdenstudios.app.core.model.MediaType
import com.raxdenstudios.app.core.network.model.ErrorDto
import com.raxdenstudios.app.core.network.model.MediaDto
import com.raxdenstudios.app.core.network.model.PageDto
import com.raxdenstudios.app.core.network.service.MediaV3Service
import com.raxdenstudios.commons.ResultData
import com.raxdenstudios.commons.pagination.model.Page
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.json.JSONObject
import org.junit.Test
import retrofit2.Response

@ExperimentalCoroutinesApi
class MediaGatewayTest {

    private val mediaV3Service: MediaV3Service = mockk(relaxed = true)
    private val gateway: MediaGateway by lazy {
        MediaGateway(
            mediaV3Service = mediaV3Service,
        )
    }

    @Test
    fun `Given a filer as nowPlaying, When fetch method is called, Then nowPlayingMovies call is called`() =
        runTest {
            coEvery { mediaV3Service.nowPlayingMovies(1) } returns aNetworkResponseSuccessFirstPage
            val page = Page(1)

            gateway.nowPlaying(MediaType.Movie, page)

            coVerify(exactly = 1) { mediaV3Service.nowPlayingMovies(1) }
        }

    @Test
    fun `Given a filer as upcoming, When fetch method is called, Then upcoming call is called`() =
        runTest {
            coEvery { mediaV3Service.upcoming(1) } returns aNetworkResponseSuccessFirstPage
            val page = Page(1)

            gateway.upcoming(MediaType.Movie, page)

            coVerify(exactly = 1) { mediaV3Service.upcoming(1) }
        }

    @Test
    fun `Given a filer as popularMovies, When fetch method is called, Then nowPlayingMovies call is called`() =
        runTest {
            coEvery { mediaV3Service.popularMovies(1) } returns aNetworkResponseSuccessFirstPage
            val page = Page(1)

            gateway.popular(MediaType.Movie, page)

            coVerify(exactly = 1) { mediaV3Service.popularMovies(1) }
        }

    @Test
    fun `Given a filer as topRatedMovies, When fetch method is called, Then topRatedMovies call is called`() =
        runTest {
            coEvery { mediaV3Service.topRatedMovies(1) } returns aNetworkResponseSuccessFirstPage
            val page = Page(1)

            gateway.topRated(MediaType.Movie, page)

            coVerify(exactly = 1) { mediaV3Service.topRatedMovies(1) }
        }

    @Test
    fun `Given a query, When search method is called, Then return valid results`() =
        runTest {
            val query = "query"
            val page = Page(1)
            coEvery { mediaV3Service.search(query, page.value) } returns aSearchNetworkResponseSuccessFirstPage

            val result = gateway.search(query, page)

            assertThat(result).isEqualTo(
                ResultData.Success(
                    PageDto(
                        page = 1,
                        total_pages = 1,
                        total_results = 2,
                        results = listOf(
                            MediaDto.Movie.mock,
                            MediaDto.TVShow.mock,
                        )
                    )
                )
            )
        }

    companion object {

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
                        MediaDto.Movie().copy(id = 1),
                        MediaDto.Movie().copy(id = 2),
                    )
                ),
                response = response,
            )
        private val aSearchNetworkResponseSuccessFirstPage: NetworkResponse<PageDto<JSONObject>, ErrorDto> =
            NetworkResponse.Success(
                body = PageDto(
                    page = 1,
                    total_pages = 1,
                    total_results = 2,
                    results = listOf(
                        JSONObject().apply {
                            put("adult", false)
                            put("backdrop_path", "/9yBVqNruk6Ykrwc32qrK2TIE5xw.jpg")
                            put("id", 1)
                            put("title", "The Last of Us")
                            put("original_language", "en")
                            put("original_title", "")
                            put("overview", "Twenty years after modern civilization has been destroyed...")
                            put("poster_path", "")
                            put("media_type", "movie")
                            put("genre_ids", emptyList<Int>())
                            put("popularity", 0.0)
                            put("release_date", "1970-01-01")
                            put("vote_average", 0.0)
                            put("vote_count", 0)
                        },
                        JSONObject().apply {
                            put("adult", false)
                            put("backdrop_path", "/9yBVqNruk6Ykrwc32qrK2TIE5xw.jpg")
                            put("id", 1)
                            put("name", "The Last of Us")
                            put("original_language", "en")
                            put("original_name", "")
                            put("overview", "Twenty years after modern civilization has been destroyed...")
                            put("poster_path", "")
                            put("media_type", "tv")
                            put("genre_ids", emptyList<Int>())
                            put("origin_country", emptyList<String>())
                            put("popularity", 0.0)
                            put("first_air_date", "1970-01-01")
                            put("vote_average", 0.0)
                            put("vote_count", 0)
                        }
                    )
                ),
                response = response,
            )
    }
}
