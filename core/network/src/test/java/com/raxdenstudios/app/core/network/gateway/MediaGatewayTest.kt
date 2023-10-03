package com.raxdenstudios.app.core.network.gateway

import com.google.common.truth.Truth.assertThat
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.haroldadmin.cnradapter.NetworkResponse
import com.raxdenstudios.app.core.model.MediaType
import com.raxdenstudios.app.core.network.model.ErrorDto
import com.raxdenstudios.app.core.network.model.MediaDto
import com.raxdenstudios.app.core.network.model.PageDto
import com.raxdenstudios.app.core.network.service.MediaV3Service
import com.raxdenstudios.commons.core.ResultData
import com.raxdenstudios.commons.pagination.model.Page
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk

import kotlinx.coroutines.test.runTest
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
        private val aSearchNetworkResponseSuccessFirstPage: NetworkResponse<PageDto<JsonObject>, ErrorDto> =
            NetworkResponse.Success(
                body = PageDto(
                    page = 1,
                    total_pages = 1,
                    total_results = 2,
                    results = listOf(
                        JsonObject().apply {
                            addProperty("adult", false)
                            addProperty("backdrop_path", "/9yBVqNruk6Ykrwc32qrK2TIE5xw.jpg")
                            addProperty("id", 1)
                            addProperty("title", "The Last of Us")
                            addProperty("original_language", "en")
                            addProperty("original_title", "")
                            addProperty("overview", "Twenty years after modern civilization has been destroyed...")
                            addProperty("poster_path", "")
                            addProperty("media_type", "movie")
                            add("genre_ids", JsonArray())
                            addProperty("popularity", 0.0)
                            addProperty("release_date", "1970-01-01")
                            addProperty("vote_average", 0.0)
                            addProperty("vote_count", 0)
                        },
                        JsonObject().apply {
                            addProperty("adult", false)
                            addProperty("backdrop_path", "/9yBVqNruk6Ykrwc32qrK2TIE5xw.jpg")
                            addProperty("id", 1)
                            addProperty("name", "The Last of Us")
                            addProperty("original_language", "en")
                            addProperty("original_name", "")
                            addProperty("overview", "Twenty years after modern civilization has been destroyed...")
                            addProperty("poster_path", "")
                            addProperty("media_type", "tv")
                            add("genre_ids", JsonArray())
                            add("origin_country", JsonArray())
                            addProperty("popularity", 0.0)
                            addProperty("first_air_date", "1970-01-01")
                            addProperty("vote_average", 0.0)
                            addProperty("vote_count", 0)
                        }
                    )
                ),
                response = response,
            )
    }
}
