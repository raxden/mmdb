package com.raxdenstudios.app.core.domain

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.raxdenstudios.app.core.data.FakeHomeModuleRepository
import com.raxdenstudios.app.core.data.HomeModuleRepository
import com.raxdenstudios.app.core.data.MediaRepository
import com.raxdenstudios.app.core.model.ErrorDomain
import com.raxdenstudios.app.core.model.HomeModule
import com.raxdenstudios.app.core.model.Media
import com.raxdenstudios.app.core.model.MediaCategory
import com.raxdenstudios.app.core.model.MediaFilter
import com.raxdenstudios.app.core.model.MediaId
import com.raxdenstudios.app.core.model.MediaType
import com.raxdenstudios.commons.coroutines.DispatcherProvider
import com.raxdenstudios.commons.core.ResultData
import com.raxdenstudios.commons.pagination.model.Page
import com.raxdenstudios.commons.pagination.model.PageList
import com.raxdenstudios.commons.coroutines.test.rules.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

class GetHomeModulesUseCaseTest {

    private val testDispatcher = StandardTestDispatcher()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule(
        testDispatcher = testDispatcher
    )

    private val dispatcherProvider: DispatcherProvider = object : DispatcherProvider {
        override val main = testDispatcher
        override val default = testDispatcher
        override val io = testDispatcher
    }
    private val homeModuleRepository: HomeModuleRepository = FakeHomeModuleRepository()
    private val mediaRepository: MediaRepository = mockk {
        coEvery { medias(any(), any(), any()) } returns ResultData.Success(pageListOfMedias)
        coEvery { observeWatchlist() } returns flowOf(ResultData.Success(medias))
    }
    private val useCase: GetHomeModulesUseCase by lazy {
        GetHomeModulesUseCase(
            dispatcher = dispatcherProvider,
            homeModuleRepository = homeModuleRepository,
            mediaRepository = mediaRepository,
        )
    }

    @Test
    fun `Should return a list of modules with movies`() = runTest {
        useCase().test {
            val modules = awaitItem()

            assertThat(modules).isEqualTo(
                ResultData.Success(
                    listOf(
                        HomeModule.Carousel.popular(
                            0,
                            0,
                            MediaType.Movie,
                            medias = listOf(Media.Movie.mock.copy(id = MediaId(1)))
                        ),
                        HomeModule.Carousel.nowPlaying(
                            0,
                            0,
                            medias = listOf(Media.Movie.mock.copy(id = MediaId(1)))
                        ),
                    )
                )
            )

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Given an error produced when retrieve medias from nowplaying, When execute is called, Then return a list of modules with movies except nowplaying`() =
        runTest {
            coEvery {
                mediaRepository.medias(MediaFilter.nowPlaying, any(), any())
            } returns ResultData.Failure(ErrorDomain.Unknown(""))

            useCase().test {
                val modules = awaitItem()

                assertThat(modules).isEqualTo(
                    ResultData.Success(
                        listOf(
                            HomeModule.Carousel(
                                0,
                                0,
                                mediaType = MediaType.Movie,
                                mediaCategory = MediaCategory.Popular,
                                medias = listOf(Media.Movie.mock.copy(id = MediaId(1)))
                            ),
                        )
                    )
                )

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `When modules are updated, Then return a list of modules with movies updated`() = runTest {
        useCase().test {
            val modules = awaitItem()
            assertThat(modules).isEqualTo(
                ResultData.Success(
                    listOf(
                        HomeModule.Carousel.popular(
                            0,
                            0,
                            MediaType.Movie,
                            medias = listOf(Media.Movie.mock.copy(id = MediaId(1)))
                        ),
                        HomeModule.Carousel.nowPlaying(
                            0,
                            0,
                            medias = listOf(Media.Movie.mock.copy(id = MediaId(1)))
                        ),
                    )
                )
            )

            homeModuleRepository.save(HomeModule.Carousel.topRated(0, 0, MediaType.Movie))

            val modulesUpdated = awaitItem()
            assertThat(modulesUpdated).isEqualTo(
                ResultData.Success(
                    listOf(
                        HomeModule.Carousel.popular(
                            0,
                            0,
                            MediaType.Movie,
                            medias = listOf(Media.Movie.mock.copy(id = MediaId(1))),
                        ),
                        HomeModule.Carousel.nowPlaying(
                            0,
                            0,
                            medias = listOf(Media.Movie.mock.copy(id = MediaId(1)))
                        ),
                        HomeModule.Carousel.topRated(
                            0,
                            0,
                            MediaType.Movie,
                            medias = listOf(Media.Movie.mock.copy(id = MediaId(1)))
                        ),
                    )
                )
            )

            cancelAndIgnoreRemainingEvents()
        }
    }

    companion object {

        private val firstPage = Page(1)
        private val medias = listOf(Media.Movie.mock.copy(id = MediaId(1)))
        private val pageListOfMedias = PageList<Media>(medias, firstPage)
    }
}
