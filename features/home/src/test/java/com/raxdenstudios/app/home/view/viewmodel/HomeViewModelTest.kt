package com.raxdenstudios.app.home.view.viewmodel

import androidx.lifecycle.Observer
import com.raxdenstudios.app.account.domain.IsAccountLoggedUseCase
import com.raxdenstudios.app.home.di.homeFeatureModule
import com.raxdenstudios.app.home.domain.GetHomeModulesUseCase
import com.raxdenstudios.app.home.domain.GetMoviesUseCase
import com.raxdenstudios.app.home.domain.model.HomeModule
import com.raxdenstudios.app.home.view.model.*
import com.raxdenstudios.app.movie.domain.AddMovieToWatchListUseCase
import com.raxdenstudios.app.movie.domain.RemoveMovieFromWatchListUseCase
import com.raxdenstudios.app.movie.domain.model.Movie
import com.raxdenstudios.app.movie.domain.model.SearchType
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
  private val getMoviesUseCase: GetMoviesUseCase = mockk {
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
      homeFeatureModule,
      module {
        factory(override = true) { getHomeModulesUseCase }
        factory(override = true) { getMoviesUseCase }
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
              HomeModuleModel.CarouselMovies.Popular(
                CarouselMovieListModel.empty.copy(
                  movies = listOf(
                    MovieListItemModel.empty.copy(id = 1L),
                    MovieListItemModel.empty.copy(id = 2L),
                  )
                )
              ),
              HomeModuleModel.CarouselMovies.NowPlaying(
                CarouselMovieListModel.empty.copy(
                  movies = listOf(
                    MovieListItemModel.empty.copy(id = 1L),
                    MovieListItemModel.empty.copy(id = 2L),
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
                HomeModuleModel.CarouselMovies.Popular(
                  CarouselMovieListModel.empty.copy(
                    movies = listOf(
                      MovieListItemModel.empty.copy(
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

private val aMovieModel = MovieListItemModel.empty.copy(id = 1L)
private val aCarouselMovieListModel = CarouselMovieListModel.empty.copy(
  movies = listOf(aMovieModel)
)
private val aCarouselMoviesPopularModuleModel =
  HomeModuleModel.CarouselMovies.Popular(aCarouselMovieListModel)
private val aHomeModelModules = listOf(
  aCarouselMoviesPopularModuleModel
)
private val aHomeModel = HomeModel.empty.copy(
  modules = aHomeModelModules
)
private val aHomeModules = listOf(
  HomeModule.PopularMovies,
  HomeModule.NowPlayingMovies
)
private val aMovies = listOf(
  Movie.empty.copy(id = 1),
  Movie.empty.copy(id = 2),
)
private val aPageMovieList = PageList(aMovies, Page(1))
private val aResultPageMovieListSuccess = ResultData.Success(aPageMovieList)
private val aGetUpcomingMoviesUseCaseParams =
  GetMoviesUseCase.Params.BySearchType(SearchType.Upcoming, Page(1))
private val aGetTopRatedMoviesUseCaseParams =
  GetMoviesUseCase.Params.BySearchType(SearchType.TopRated, Page(1))
private val aGetPopularMoviesUseCaseParams =
  GetMoviesUseCase.Params.BySearchType(SearchType.Popular, Page(1))
private val aGetNowPlayingMoviesUseCaseParams =
  GetMoviesUseCase.Params.BySearchType(SearchType.NowPlaying, Page(1))
private val aGetWatchListMoviesUseCaseParams =
  GetMoviesUseCase.Params.WatchList(Page(1))
