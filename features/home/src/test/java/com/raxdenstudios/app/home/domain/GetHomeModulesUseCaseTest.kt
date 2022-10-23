package com.raxdenstudios.app.home.domain

import com.raxdenstudios.app.home.data.repository.HomeModuleRepository
import com.raxdenstudios.app.home.domain.model.HomeModule
import com.raxdenstudios.app.media.data.repository.MediaRepository
import com.raxdenstudios.app.media.domain.model.Media
import com.raxdenstudios.app.media.domain.model.MediaFilter
import com.raxdenstudios.app.media.domain.model.MediaId
import com.raxdenstudios.app.media.domain.model.MediaType
import com.raxdenstudios.app.test.BasePresentationTest
import com.raxdenstudios.commons.DispatcherProvider
import com.raxdenstudios.commons.ResultData
import com.raxdenstudios.commons.pagination.model.Page
import com.raxdenstudios.commons.pagination.model.PageList
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Test

@ExperimentalCoroutinesApi
internal class GetHomeModulesUseCaseTest : BasePresentationTest() {

    private val dispatcherProvider: DispatcherProvider = object : DispatcherProvider {
        override val main = testDispatcher
        override val default = testDispatcher
        override val io = testDispatcher
    }
    private val homeModuleRepository: HomeModuleRepository = mockk {
        coEvery { observeModules() } returns flowOf(aModules)
    }
    private val mediasRepository: MediaRepository = mockk {
        coEvery { medias(any(), any(), any()) } returns ResultData.Success(aPageListOfMedias)
    }

    private val useCase: GetHomeModulesUseCase by lazy {
        GetHomeModulesUseCase(
            dispatcherProvider = dispatcherProvider,
            homeModuleRepository = homeModuleRepository,
            mediasRepository = mediasRepository,
        )
    }

    @Test
    fun `Should return a list of modules with movies`() = runTest {
        useCase().collect { result ->
            assertEquals(
                listOf(
                    HomeModule.Popular(MediaType.MOVIE, listOf(Media.Movie.empty.copy(id = MediaId(1)))),
                    HomeModule.NowPlaying(listOf(Media.Movie.empty.copy(id = MediaId(1)))),
                    HomeModule.TopRated(MediaType.MOVIE, listOf(Media.Movie.empty.copy(id = MediaId(1)))),
                    HomeModule.Upcoming(listOf(Media.Movie.empty.copy(id = MediaId(1)))),
                    HomeModule.WatchList(MediaType.MOVIE, listOf(Media.Movie.empty.copy(id = MediaId(1)))),
                ), result
            )
        }
    }

    @Test
    fun `Given an error produced when retrieve medias from nowplaying, When execute is called, Then return a list of modules with movies except nowplaying`() =
        runTest {
            coEvery {
                mediasRepository.medias(MediaFilter.NowPlaying, any(), any())
            } returns ResultData.Error(IllegalStateException(""))

            useCase().collect { result ->
                assertEquals(
                    listOf(
                        HomeModule.Popular(MediaType.MOVIE, listOf(Media.Movie.empty.copy(id = MediaId(1)))),
                        HomeModule.NowPlaying(emptyList()),
                        HomeModule.TopRated(MediaType.MOVIE, listOf(Media.Movie.empty.copy(id = MediaId(1)))),
                        HomeModule.Upcoming(listOf(Media.Movie.empty.copy(id = MediaId(1)))),
                        HomeModule.WatchList(MediaType.MOVIE, listOf(Media.Movie.empty.copy(id = MediaId(1)))),
                    ), result
                )
            }
        }
}

private val aModules = listOf(
    HomeModule.popularMovies,
    HomeModule.nowPlayingMovies,
    HomeModule.topRatedMovies,
    HomeModule.upcomingMovies,
    HomeModule.watchListMovies,
)
private val aPage = Page(1)
private val aMedias = listOf(Media.Movie.empty.copy(id = MediaId(1)))
private val aPageListOfMedias = PageList<Media>(aMedias, aPage)
