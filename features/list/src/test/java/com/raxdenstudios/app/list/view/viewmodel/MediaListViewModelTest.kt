package com.raxdenstudios.app.list.view.viewmodel

import androidx.lifecycle.Observer
import com.raxdenstudios.app.account.domain.IsAccountLoggedUseCase
import com.raxdenstudios.app.di.baseFeatureModule
import com.raxdenstudios.app.list.di.listFeatureModule
import com.raxdenstudios.app.list.view.model.MediaListModel
import com.raxdenstudios.app.list.view.model.MediaListParams
import com.raxdenstudios.app.list.view.model.MediaListUIState
import com.raxdenstudios.app.movie.domain.AddMediaToWatchListUseCase
import com.raxdenstudios.app.movie.domain.GetMediasUseCase
import com.raxdenstudios.app.movie.domain.RemoveMediaFromWatchListUseCase
import com.raxdenstudios.app.movie.domain.model.Media
import com.raxdenstudios.app.movie.domain.model.MediaFilter
import com.raxdenstudios.app.movie.view.model.MediaListItemModel
import com.raxdenstudios.app.movie.view.model.WatchButtonModel
import com.raxdenstudios.app.test.BaseTest
import com.raxdenstudios.commons.ResultData
import com.raxdenstudios.commons.pagination.model.Page
import com.raxdenstudios.commons.pagination.model.PageIndex
import com.raxdenstudios.commons.pagination.model.PageList
import com.raxdenstudios.commons.pagination.model.PageSize
import io.mockk.coEvery
import io.mockk.coVerifyOrder
import io.mockk.mockk
import org.junit.Test
import org.koin.core.module.Module
import org.koin.dsl.module
import org.koin.test.inject

internal class MediaListViewModelTest : BaseTest() {

  private val isAccountLoggedUseCase: IsAccountLoggedUseCase = mockk {
    coEvery { execute() } returns false
  }
  private val addMediaToWatchListUseCase: AddMediaToWatchListUseCase = mockk() {
    coEvery { execute(any()) } returns ResultData.Success(true)
  }
  private val removeMediaFromWatchListUseCase: RemoveMediaFromWatchListUseCase = mockk() {
    coEvery { execute(any()) } returns ResultData.Success(true)
  }
  private val getMediasUseCase: GetMediasUseCase = mockk() {
    coEvery { execute(aGetMoviesUseCaseFirstPageParams) } returns ResultData.Success(aFirstPageList)
    coEvery { execute(aGetMoviesUseCaseSecondPageParams) } returns ResultData.Success(
      aSecondPageList
    )
  }
  private val stateObserver: Observer<MediaListUIState> = mockk(relaxed = true)

  override val modules: List<Module>
    get() = listOf(
      baseFeatureModule,
      listFeatureModule,
      module {
        factory(override = true) { isAccountLoggedUseCase }
        factory(override = true) { getMediasUseCase }
        factory(override = true) { addMediaToWatchListUseCase }
        factory(override = true) { removeMediaFromWatchListUseCase }
      }
    )

  private val viewModel: MediaListViewModel by inject()

  @Test
  fun `when movie is added to watchlist, movie is replaced in model`() {
    val model = givenAMovieListModelWithResultsFromFirstPage()
    val itemToAddToWatchList = MediaListItemModel.empty.copy(id = 2L)
    viewModel.state.observeForever(stateObserver)

    viewModel.addMovieToWatchList(model, itemToAddToWatchList)

    coVerifyOrder {
      stateObserver.onChanged(
        MediaListUIState.Content(
          MediaListModel.empty.copy(
            media = listOf(
              MediaListItemModel.empty.copy(id = 1L),
              MediaListItemModel.empty.copy(id = 2L, watchButtonModel = WatchButtonModel.Selected),
              MediaListItemModel.empty.copy(id = 3L),
              MediaListItemModel.empty.copy(id = 4L),
              MediaListItemModel.empty.copy(id = 5L),
              MediaListItemModel.empty.copy(id = 6L),
              MediaListItemModel.empty.copy(id = 7L),
              MediaListItemModel.empty.copy(id = 8L),
              MediaListItemModel.empty.copy(id = 9L),
              MediaListItemModel.empty.copy(id = 10L),
            )
          )
        )
      )
    }
  }

  @Test
  fun `when movie is removed from watchlist, movie is replaced in model`() {
    val model = givenAMovieListModelWithResultsFromFirstPage()
    val itemToRemoveFromWatchList = MediaListItemModel.empty.copy(
      id = 2L, watchButtonModel = WatchButtonModel.Selected
    )
    viewModel.state.observeForever(stateObserver)

    viewModel.removeMovieFromWatchList(model, itemToRemoveFromWatchList)

    coVerifyOrder {
      stateObserver.onChanged(
        MediaListUIState.Content(
          MediaListModel.empty.copy(
            media = listOf(
              MediaListItemModel.empty.copy(id = 1L),
              MediaListItemModel.empty.copy(
                id = 2L,
                watchButtonModel = WatchButtonModel.Unselected
              ),
              MediaListItemModel.empty.copy(id = 3L),
              MediaListItemModel.empty.copy(id = 4L),
              MediaListItemModel.empty.copy(id = 5L),
              MediaListItemModel.empty.copy(id = 6L),
              MediaListItemModel.empty.copy(id = 7L),
              MediaListItemModel.empty.copy(id = 8L),
              MediaListItemModel.empty.copy(id = 9L),
              MediaListItemModel.empty.copy(id = 10L),
            )
          )
        )
      )
    }
  }

  @Test
  fun `Given a params with searchType as popular, When refreshMovies method is called, Then first page with movies is returned`() {
    val params = MediaListParams.popularMovies
    viewModel.state.observeForever(stateObserver)

    viewModel.refreshMovies(params)

    coVerifyOrder {
      stateObserver.onChanged(MediaListUIState.Loading)
      stateObserver.onChanged(
        MediaListUIState.Content(
          MediaListModel.empty.copy(
            media = aFirstPageMoviesModel
          )
        )
      )
    }
  }

  @Test
  fun `Given a params with searchType as popular, When loadMovies method is called, Then first page with movies is returned`() {
    val params = MediaListParams.popularMovies
    viewModel.state.observeForever(stateObserver)

    viewModel.loadMovies(params)

    coVerifyOrder {
      stateObserver.onChanged(MediaListUIState.Loading)
      stateObserver.onChanged(
        MediaListUIState.Content(
          MediaListModel.empty.copy(
            media = aFirstPageMoviesModel
          )
        )
      )
    }
  }

  @Test
  fun `Given a model with results from first page and pageIndex with value 8, When loadMoreMovies is called, Then second page with movies is returned`() {
    val model = givenAMovieListModelWithResultsFromFirstPage()
    val pageIndex = PageIndex(20)
    viewModel.state.observeForever(stateObserver)

    viewModel.loadMoreMovies(pageIndex, model)

    coVerifyOrder {
      stateObserver.onChanged(MediaListUIState.Loading)
      stateObserver.onChanged(
        MediaListUIState.Content(
          MediaListModel.empty.copy(
            media = aSecondPageMoviesModel
          )
        )
      )
    }
  }

  private fun givenAMovieListModelWithResultsFromFirstPage(): MediaListModel =
    MediaListModel.empty.copy(
      media = aFirstPageMoviesModel
    )
}

private val aFirstPage = Page(1)
private val aSecondPage = Page(2)
private val aPageSize = PageSize(20)
private val aGetMoviesUseCaseFirstPageParams =
  GetMediasUseCase.Params(MediaFilter.popularMovies, aFirstPage, aPageSize)
private val aGetMoviesUseCaseSecondPageParams =
  GetMediasUseCase.Params(MediaFilter.popularMovies, aSecondPage, aPageSize)
private val aFirstPageMoviesModel = listOf(
  MediaListItemModel.empty.copy(id = 1L),
  MediaListItemModel.empty.copy(id = 2L),
  MediaListItemModel.empty.copy(id = 3L),
  MediaListItemModel.empty.copy(id = 4L),
  MediaListItemModel.empty.copy(id = 5L),
  MediaListItemModel.empty.copy(id = 6L),
  MediaListItemModel.empty.copy(id = 7L),
  MediaListItemModel.empty.copy(id = 8L),
  MediaListItemModel.empty.copy(id = 9L),
  MediaListItemModel.empty.copy(id = 10L),
)
private val aSecondPageMoviesModel = listOf(
  MediaListItemModel.empty.copy(id = 11L),
  MediaListItemModel.empty.copy(id = 12L),
  MediaListItemModel.empty.copy(id = 13L),
  MediaListItemModel.empty.copy(id = 14L),
  MediaListItemModel.empty.copy(id = 15L),
  MediaListItemModel.empty.copy(id = 16L),
  MediaListItemModel.empty.copy(id = 17L),
  MediaListItemModel.empty.copy(id = 18L),
  MediaListItemModel.empty.copy(id = 19L),
  MediaListItemModel.empty.copy(id = 20L),
)
private val aFirstPageMovies = listOf(
  Media.empty.copy(id = 1L),
  Media.empty.copy(id = 2L),
  Media.empty.copy(id = 3L),
  Media.empty.copy(id = 4L),
  Media.empty.copy(id = 5L),
  Media.empty.copy(id = 6L),
  Media.empty.copy(id = 7L),
  Media.empty.copy(id = 8L),
  Media.empty.copy(id = 9L),
  Media.empty.copy(id = 10L),
)
private val aSecondPageMovies = listOf(
  Media.empty.copy(id = 11L),
  Media.empty.copy(id = 12L),
  Media.empty.copy(id = 13L),
  Media.empty.copy(id = 14L),
  Media.empty.copy(id = 15L),
  Media.empty.copy(id = 16L),
  Media.empty.copy(id = 17L),
  Media.empty.copy(id = 18L),
  Media.empty.copy(id = 19L),
  Media.empty.copy(id = 20L),
)
private val aFirstPageList = PageList(
  items = aFirstPageMovies,
  aFirstPage,
)
private val aSecondPageList = PageList(
  items = aSecondPageMovies,
  aSecondPage,
)