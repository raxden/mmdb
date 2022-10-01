package com.raxdenstudios.app.core.domain

import app.cash.turbine.test
import com.google.common.truth.Truth.assertThat
import com.raxdenstudios.app.core.data.HomeModuleRepository
import com.raxdenstudios.app.core.data.MediaRepository
import com.raxdenstudios.app.core.model.HomeModule
import com.raxdenstudios.app.core.model.Media
import com.raxdenstudios.app.core.model.MediaFilter
import com.raxdenstudios.app.core.model.MediaId
import com.raxdenstudios.commons.DispatcherProvider
import com.raxdenstudios.commons.ResultData
import com.raxdenstudios.commons.pagination.model.Page
import com.raxdenstudios.commons.pagination.model.PageList
import com.raxdenstudios.commons.test.rules.MainDispatcherRule
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class GetHomeModulesUseCaseTest {

    @ExperimentalCoroutinesApi
    val testDispatcher = StandardTestDispatcher()

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
    private val mediasRepository: MediaRepository = mockk {
        coEvery { medias(any(), any(), any()) } returns ResultData.Success(pageListOfMedias)
        coEvery { observeWatchlist() } returns flowOf(ResultData.Success(medias))
    }
    private val useCase: GetHomeModulesUseCase by lazy {
        GetHomeModulesUseCase(
            dispatcher = dispatcherProvider,
            homeModuleRepository = homeModuleRepository,
            mediasRepository = mediasRepository,
        )
    }

    @Test
    fun `Should return a list of modules with movies`() = runTest {
        useCase().test {
            val modules = awaitItem()

            assertThat(modules).isEqualTo(
                mapOf(
                    HomeModule.Popular.empty to listOf(Media.Movie.empty.copy(id = MediaId(1))),
                    HomeModule.NowPlaying.empty to listOf(Media.Movie.empty.copy(id = MediaId(1))),
                ),
            )

            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `Given an error produced when retrieve medias from nowplaying, When execute is called, Then return a list of modules with movies except nowplaying`() =
        runTest {
            coEvery {
                mediasRepository.medias(MediaFilter.nowPlaying, any(), any())
            } returns ResultData.Error(IllegalStateException(""))

            useCase().test {
                val modules = awaitItem()

                assertThat(modules).isEqualTo(
                    mapOf(
                        HomeModule.Popular.empty to listOf(Media.Movie.empty.copy(id = MediaId(1))),
                        HomeModule.NowPlaying.empty to emptyList(),
                    ),
                )

                cancelAndIgnoreRemainingEvents()
            }
        }

    @Test
    fun `When modules are updated, Then return a list of modules with movies updated`() = runTest {
        useCase().test {
            val modules = awaitItem()
            assertThat(modules).isEqualTo(
                mapOf(
                    HomeModule.Popular.empty to listOf(Media.Movie.empty.copy(id = MediaId(1))),
                    HomeModule.NowPlaying.empty to listOf(Media.Movie.empty.copy(id = MediaId(1))),
                ),
            )

            homeModuleRepository.save(HomeModule.TopRated.empty)

            val modulesUpdated = awaitItem()
            assertThat(modulesUpdated).isEqualTo(
                mapOf(
                    HomeModule.Popular.empty to listOf(Media.Movie.empty.copy(id = MediaId(1))),
                    HomeModule.NowPlaying.empty to listOf(Media.Movie.empty.copy(id = MediaId(1))),
                    HomeModule.TopRated.empty to listOf(Media.Movie.empty.copy(id = MediaId(1))),
                ),
            )

            cancelAndIgnoreRemainingEvents()
        }
    }

    companion object {

        private val firstPage = Page(1)
        private val medias = listOf(Media.Movie.empty.copy(id = MediaId(1)))
        private val pageListOfMedias = PageList<Media>(medias, firstPage)
    }
}
