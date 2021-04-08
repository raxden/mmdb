package com.raxdenstudios.app.home.view.viewmodel

import androidx.lifecycle.Observer
import com.raxdenstudios.app.account.domain.IsAccountLogged
import com.raxdenstudios.app.home.di.homeFeatureModule
import com.raxdenstudios.app.home.domain.GetHomeModulesUseCase
import com.raxdenstudios.app.home.domain.GetMoviesUseCase
import com.raxdenstudios.app.home.domain.model.HomeModule
import com.raxdenstudios.app.home.view.model.CarouselMovieListModel
import com.raxdenstudios.app.home.view.model.HomeModel
import com.raxdenstudios.app.home.view.model.HomeModuleModel
import com.raxdenstudios.app.home.view.model.HomeUIState
import com.raxdenstudios.app.home.view.model.MovieListItemModel
import com.raxdenstudios.app.home.view.model.WatchButtonModel
import com.raxdenstudios.app.movie.domain.AddMovieToWatchList
import com.raxdenstudios.app.movie.domain.Movie
import com.raxdenstudios.app.movie.domain.RemoveMovieFromWatchList
import com.raxdenstudios.app.movie.domain.SearchType
import com.raxdenstudios.app.test.BaseTest
import com.raxdenstudios.commons.ResultData
import com.raxdenstudios.commons.pagination.model.Page
import com.raxdenstudios.commons.pagination.model.PageList
import com.raxdenstudios.commons.provider.StringProvider
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.flow.flow
import org.junit.Test
import org.koin.core.inject
import org.koin.core.module.Module
import org.koin.dsl.module

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
  }
  private val isAccountLogged: IsAccountLogged = mockk {
    coEvery { execute() } returns false
  }
  private val addMovieToWatchList: AddMovieToWatchList = mockk {
    coEvery { execute(any()) } returns ResultData.Success(true)
  }
  private val removeMovieToWatchList: RemoveMovieFromWatchList = mockk {
    coEvery { execute(any()) } returns ResultData.Success(true)
  }
  private val stateObserver: Observer<HomeUIState> = mockk(relaxed = true)

  override val modules: List<Module>
    get() = listOf(
      homeFeatureModule,
      module {
        factory(override = true) { getHomeModulesUseCase }
        factory(override = true) { getMoviesUseCase }
        factory(override = true) { isAccountLogged }
        factory(override = true) { addMovieToWatchList }
        factory(override = true) { removeMovieToWatchList }
        factory(override = true) { stringProvider }
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
    viewModel.state.observeForever(stateObserver)

    viewModel.addMovieToWatchList(
      aHomeModel,
      aCarouselMoviesPopularModuleModel,
      aCarouselMovieListModel,
      aMovieModel,
    )

    verify {
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
private val aGetUpcomingMoviesUseCaseParams = GetMoviesUseCase.Params(SearchType.Upcoming, Page(1))
private val aGetTopRatedMoviesUseCaseParams = GetMoviesUseCase.Params(SearchType.TopRated, Page(1))
private val aGetPopularMoviesUseCaseParams = GetMoviesUseCase.Params(SearchType.Popular, Page(1))
private val aGetNowPlayingMoviesUseCaseParams =
  GetMoviesUseCase.Params(SearchType.NowPlaying, Page(1))
