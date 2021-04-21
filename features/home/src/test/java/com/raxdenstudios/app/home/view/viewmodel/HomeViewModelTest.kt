package com.raxdenstudios.app.home.view.viewmodel

import androidx.lifecycle.Observer
import com.raxdenstudios.app.account.domain.IsAccountLoggedUseCase
import com.raxdenstudios.app.di.baseFeatureModule
import com.raxdenstudios.app.home.di.homeFeatureModule
import com.raxdenstudios.app.home.domain.GetHomeModulesUseCase
import com.raxdenstudios.app.home.domain.model.HomeModule
import com.raxdenstudios.app.home.view.model.CarouselMediaListModel
import com.raxdenstudios.app.home.view.model.HomeModel
import com.raxdenstudios.app.home.view.model.HomeModuleModel
import com.raxdenstudios.app.home.view.model.HomeUIState
import com.raxdenstudios.app.movie.domain.AddMovieToWatchListUseCase
import com.raxdenstudios.app.movie.domain.GetMediasUseCase
import com.raxdenstudios.app.movie.domain.RemoveMovieFromWatchListUseCase
import com.raxdenstudios.app.movie.domain.model.Media
import com.raxdenstudios.app.movie.domain.model.MediaFilter
import com.raxdenstudios.app.movie.view.model.MediaFilterModel
import com.raxdenstudios.app.movie.view.model.MediaListItemModel
import com.raxdenstudios.app.movie.view.model.WatchButtonModel
import com.raxdenstudios.app.test.BaseTest
import com.raxdenstudios.commons.DispatcherFacade
import com.raxdenstudios.commons.ResultData
import com.raxdenstudios.commons.pagination.model.Page
import com.raxdenstudios.commons.pagination.model.PageList
import com.raxdenstudios.commons.provider.StringProvider
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import org.koin.core.inject
import org.koin.core.module.Module
import org.koin.dsl.module

@ExperimentalCoroutinesApi
internal class HomeViewModelTest : BaseTest() {

  private val stringProvider: StringProvider = mockk(relaxed = true)
  private val getHomeModulesUseCase: GetHomeModulesUseCase = mockk {
    every { execute() } returns flow { emit(aHomeModules) }
  }
  private val getMediasUseCase: GetMediasUseCase = mockk {
    coEvery { execute(aGetUpcomingMoviesUseCaseParams) } returns aResultPageMovieListSuccess
    coEvery { execute(aGetTopRatedMoviesUseCaseParams) } returns aResultPageMovieListSuccess
    coEvery { execute(aGetPopularMoviesUseCaseParams) } returns aResultPageMovieListSuccess
    coEvery { execute(aGetNowPlayingMoviesUseCaseParams) } returns aResultPageMovieListSuccess
    coEvery { execute(aGetWatchListMoviesUseCaseParams) } returns aResultPageMovieListSuccess
  }
  private val isAccountLoggedUseCase: IsAccountLoggedUseCase = mockk {
    coEvery { execute() } returns false
  }
  private val addMovieToWatchListUseCase: AddMovieToWatchListUseCase = mockk {
    coEvery { execute(any()) } returns ResultData.Success(true)
  }
  private val removeMovieToWatchListUseCase: RemoveMovieFromWatchListUseCase = mockk {
    coEvery { execute(any()) } returns ResultData.Success(true)
  }
  private val stateObserver: Observer<HomeUIState> = mockk(relaxed = true)
  private val dispatcher: DispatcherFacade = object : DispatcherFacade {
    override fun io() = testDispatcher
    override fun default() = testDispatcher
  }

  override val modules: List<Module>
    get() = listOf(
      baseFeatureModule,
      homeFeatureModule,
      module {
        factory(override = true) { getHomeModulesUseCase }
        factory(override = true) { getMediasUseCase }
        factory(override = true) { isAccountLoggedUseCase }
        factory(override = true) { addMovieToWatchListUseCase }
        factory(override = true) { removeMovieToWatchListUseCase }
        factory(override = true) { stringProvider }
        factory(override = true) { dispatcher }
      }
    )

  private val viewModel: HomeViewModel by inject()

  @Test
  fun `Given a viewModel, When viewModel is started, Then modules with movies are loaded`() {
    viewModel.state.observeForever(stateObserver)

    verify {
      stateObserver.onChanged(
        HomeUIState.Content(
          HomeModel.empty.copy(
            modules = listOf(
              HomeModuleModel.CarouselMovies(
                mediaFilterModel = MediaFilterModel.popularMovies,
                carouselMediaListModel = CarouselMediaListModel.empty.copy(
                  medias = listOf(
                    MediaListItemModel.empty.copy(id = 1L),
                    MediaListItemModel.empty.copy(id = 2L),
                  )
                )
              ),
              HomeModuleModel.CarouselMovies(
                mediaFilterModel = MediaFilterModel.nowPlayingMovies,
                carouselMediaListModel = CarouselMediaListModel.empty.copy(
                  medias = listOf(
                    MediaListItemModel.empty.copy(id = 1L),
                    MediaListItemModel.empty.copy(id = 2L),
                  )
                )
              )
            )
          )
        )
      )
    }
  }

  @Test
  fun `Given a Home model populated, When addMovieToWatchList is called, Then movie is replaced in home model with watchButton as selected`() {
    testDispatcher.runBlockingTest {
      viewModel.state.observeForever(stateObserver)

      viewModel.addMovieToWatchList(
        aHomeModel,
        aCarouselMoviesPopularModuleModel,
        aCarouselMovieListModel,
        aMovieModel,
      )

      coVerify {
        stateObserver.onChanged(
          HomeUIState.Content(
            HomeModel.empty.copy(
              modules = listOf(
                HomeModuleModel.CarouselMovies(
                  mediaFilterModel = MediaFilterModel.popularMovies,
                  carouselMediaListModel = CarouselMediaListModel.empty.copy(
                    medias = listOf(
                      MediaListItemModel.empty.copy(
                        id = 1L,
                        watchButtonModel = WatchButtonModel.Selected
                      ),
                    )
                  )
                )
              )
            )
          )
        )
      }
    }
  }
}

private val aMovieModel = MediaListItemModel.empty.copy(id = 1L)
private val aCarouselMovieListModel = CarouselMediaListModel.empty.copy(
  medias = listOf(aMovieModel)
)
private val aCarouselMoviesPopularModuleModel =
  HomeModuleModel.CarouselMovies(
    mediaFilterModel = MediaFilterModel.popularMovies,
    carouselMediaListModel = aCarouselMovieListModel
  )
private val aHomeModelModules = listOf(
  aCarouselMoviesPopularModuleModel
)
private val aHomeModel = HomeModel.empty.copy(
  modules = aHomeModelModules
)
private val aHomeModules = listOf(
  HomeModule.popularMovies,
  HomeModule.nowPlayingMovies
)
private val aMovies = listOf(
  Media.empty.copy(id = 1),
  Media.empty.copy(id = 2),
)
private val aPageMovieList = PageList(aMovies, Page(1))
private val aResultPageMovieListSuccess = ResultData.Success(aPageMovieList)
private val aGetUpcomingMoviesUseCaseParams =
  GetMediasUseCase.Params(MediaFilter.Upcoming, Page(1))
private val aGetTopRatedMoviesUseCaseParams =
  GetMediasUseCase.Params(MediaFilter.topRatedMovies, Page(1))
private val aGetPopularMoviesUseCaseParams =
  GetMediasUseCase.Params(MediaFilter.popularMovies, Page(1))
private val aGetNowPlayingMoviesUseCaseParams =
  GetMediasUseCase.Params(MediaFilter.nowPlayingMovies, Page(1))
private val aGetWatchListMoviesUseCaseParams =
  GetMediasUseCase.Params(MediaFilter.watchListMovies, Page(1))
