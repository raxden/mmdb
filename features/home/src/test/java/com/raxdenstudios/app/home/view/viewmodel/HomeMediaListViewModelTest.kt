package com.raxdenstudios.app.home.view.viewmodel

import androidx.lifecycle.Observer
import com.raxdenstudios.app.account.domain.IsAccountLoggedUseCase
import com.raxdenstudios.app.di.baseFeatureModule
import com.raxdenstudios.app.home.di.homeFeatureModule
import com.raxdenstudios.app.home.domain.GetHomeModulesUseCase
import com.raxdenstudios.app.home.domain.model.HomeModule
import com.raxdenstudios.app.home.view.model.HomeMediaListModel
import com.raxdenstudios.app.home.view.model.HomeModuleModel
import com.raxdenstudios.app.media.domain.AddMediaToWatchListUseCase
import com.raxdenstudios.app.media.domain.GetMediasUseCase
import com.raxdenstudios.app.media.domain.RemoveMediaFromWatchListUseCase
import com.raxdenstudios.app.media.domain.model.Media
import com.raxdenstudios.app.media.domain.model.MediaFilter
import com.raxdenstudios.app.media.domain.model.MediaId
import com.raxdenstudios.app.media.view.model.MediaListItemModel
import com.raxdenstudios.app.media.view.model.WatchButtonModel
import com.raxdenstudios.app.test.BaseTest
import com.raxdenstudios.commons.DispatcherFacade
import com.raxdenstudios.commons.ResultData
import com.raxdenstudios.commons.pagination.model.Page
import com.raxdenstudios.commons.pagination.model.PageList
import com.raxdenstudios.commons.provider.StringProvider
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Test
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.inject

@ExperimentalCoroutinesApi
internal class HomeMediaListViewModelTest : BaseTest() {

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
  private val addMediaToWatchListUseCase: AddMediaToWatchListUseCase = mockk {
    coEvery { execute(any()) } returns ResultData.Success(Media.Movie.empty)
  }
  private val removeMediaToWatchListUseCase: RemoveMediaFromWatchListUseCase = mockk {
    coEvery { execute(any()) } returns ResultData.Success(true)
  }
  private val stateObserver: Observer<UIState> = mockk(relaxed = true)
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
        factory(override = true) { addMediaToWatchListUseCase }
        factory(override = true) { removeMediaToWatchListUseCase }
        factory(override = true) { stringProvider }
        factory(override = true) { dispatcher }
      }
    )

  private val viewModel: HomeMediaListViewModel by inject()

  @Test
  fun `Given a viewModel, When viewModel is started, Then modules with movies are loaded`() =
    testDispatcher.runBlockingTest {
      viewModel.state.observeForever(stateObserver)

      verify {
        stateObserver.onChanged(
          UIState.Content(
            HomeMediaListModel.empty.copy(
              modules = listOf(
                HomeModuleModel.CarouselMedias.Popular.empty.copy(
                  medias = listOf(
                    MediaListItemModel.empty.copy(id = MediaId(1L)),
                    MediaListItemModel.empty.copy(id = MediaId(2L)),
                  )
                ),
                HomeModuleModel.CarouselMedias.NowPlaying.empty.copy(
                  medias = listOf(
                    MediaListItemModel.empty.copy(id = MediaId(1L)),
                    MediaListItemModel.empty.copy(id = MediaId(2L)),
                  )
                )
              )
            )
          )
        )
      }
    }

  @Test
  fun `Given a home model already populated and a media selected marked as watchlist, When addMovieToWatchList is called, Then movie is replaced in home model with watchButton as selected`() {
    testDispatcher.runBlockingTest {
      viewModel.state.observeForever(stateObserver)

      viewModel.addMediaToWatchList(
        aHomeModel,
        MediaListItemModel.empty.copy(id = MediaId(1L)),
      )

      coVerify {
        stateObserver.onChanged(
          UIState.Content(
            HomeMediaListModel.empty.copy(
              modules = listOf(
                HomeModuleModel.CarouselMedias.Popular.empty.copy(
                  medias = listOf(
                    MediaListItemModel.empty.copy(
                      id = MediaId(1L),
                      watchButtonModel = WatchButtonModel.Selected
                    ),
                    MediaListItemModel.empty.copy(id = MediaId(2L)),
                  )
                ),
                HomeModuleModel.CarouselMedias.TopRated.empty.copy(
                  medias = listOf(
                    MediaListItemModel.empty.copy(
                      id = MediaId(1L),
                      watchButtonModel = WatchButtonModel.Selected
                    ),
                    MediaListItemModel.empty.copy(id = MediaId(2L)),
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

private val aMediaListItemModelList = listOf(
  MediaListItemModel.empty.copy(id = MediaId(1L)),
  MediaListItemModel.empty.copy(id = MediaId(2L))
)
private val aCarouselMediasPopular = HomeModuleModel.CarouselMedias.Popular.empty.copy(
  medias = aMediaListItemModelList
)
private val aCarouselMediasTopRated = HomeModuleModel.CarouselMedias.TopRated.empty.copy(
  medias = aMediaListItemModelList
)
private val aHomeModel = HomeMediaListModel.empty.copy(
  modules = listOf(
    aCarouselMediasPopular,
    aCarouselMediasTopRated
  )
)
private val aMovies = listOf(
  Media.Movie.empty.copy(id = MediaId(1)),
  Media.Movie.empty.copy(id = MediaId(2)),
)
private val aHomeModules = listOf(
  HomeModule.popularMovies.copy(medias = aMovies),
  HomeModule.nowPlayingMovies.copy(medias = aMovies),
)
private val aPageMovieList = PageList<Media>(aMovies, Page(1))
private val aResultPageMovieListSuccess = ResultData.Success(aPageMovieList)
private val aGetUpcomingMoviesUseCaseParams =
  GetMediasUseCase.Params(MediaFilter.Upcoming, Page(1))
private val aGetTopRatedMoviesUseCaseParams =
  GetMediasUseCase.Params(MediaFilter.topRatedMovies, Page(1))
private val aGetPopularMoviesUseCaseParams =
  GetMediasUseCase.Params(MediaFilter.popularMovies, Page(1))
private val aGetNowPlayingMoviesUseCaseParams =
  GetMediasUseCase.Params(MediaFilter.NowPlaying, Page(1))
private val aGetWatchListMoviesUseCaseParams =
  GetMediasUseCase.Params(MediaFilter.watchListMovies, Page(1))
