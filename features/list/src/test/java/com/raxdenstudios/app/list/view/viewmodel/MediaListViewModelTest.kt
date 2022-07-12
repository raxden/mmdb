package com.raxdenstudios.app.list.view.viewmodel

import androidx.lifecycle.Observer
import com.raxdenstudios.app.account.domain.IsAccountLoggedUseCase
import com.raxdenstudios.app.di.baseFeatureModule
import com.raxdenstudios.app.list.di.listFeatureModule
import com.raxdenstudios.app.list.view.model.MediaListModel
import com.raxdenstudios.app.list.view.model.MediaListParams
import com.raxdenstudios.app.list.view.model.MediaListUIState
import com.raxdenstudios.app.media.domain.AddMediaToWatchListUseCase
import com.raxdenstudios.app.media.domain.GetMediasUseCase
import com.raxdenstudios.app.media.domain.RemoveMediaFromWatchListUseCase
import com.raxdenstudios.app.media.domain.model.Media
import com.raxdenstudios.app.media.domain.model.MediaFilter
import com.raxdenstudios.app.media.domain.model.MediaId
import com.raxdenstudios.app.media.view.model.MediaListItemModel
import com.raxdenstudios.app.media.view.model.WatchButtonModel
import com.raxdenstudios.app.test.BaseTest
import com.raxdenstudios.commons.ResultData
import com.raxdenstudios.commons.pagination.Pagination
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
    coEvery { execute(any()) } returns ResultData.Success(Media.Movie.empty)
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
  private val paginationConfig = Pagination.Config.default.copy(
    initialPage = aFirstPage,
    pageSize = aPageSize,
    prefetchDistance = 0
  )

  override val modules: List<Module>
    get() = listOf(
      baseFeatureModule,
      listFeatureModule,
      module {
        factory(override = true) { isAccountLoggedUseCase }
        factory(override = true) { getMediasUseCase }
        factory(override = true) { addMediaToWatchListUseCase }
        factory(override = true) { removeMediaFromWatchListUseCase }
        factory(override = true) { paginationConfig }
      }
    )

  private val viewModel: MediaListViewModel by inject()

  @Test
  fun `when movie is added to watchlist, movie is replaced in model`() {
    val model = givenAMovieListModelWithResultsFromFirstPage()
    val itemToAddToWatchList = MediaListItemModel.empty.copy(id = MediaId(2L))
    viewModel.uiState.observeForever(stateObserver)

    viewModel.addMovieToWatchList(model, itemToAddToWatchList)

    coVerifyOrder {
      stateObserver.onChanged(
        MediaListUIState.Content(
          MediaListModel.empty.copy(
            media = listOf(
              MediaListItemModel.empty.copy(id = MediaId(1L)),
              MediaListItemModel.empty.copy(
                id = MediaId(2L),
                watchButtonModel = WatchButtonModel.Selected
              ),
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
      id = MediaId(2L), watchButtonModel = WatchButtonModel.Selected
    )
    viewModel.uiState.observeForever(stateObserver)

    viewModel.removeMovieFromWatchList(model, itemToRemoveFromWatchList)

    coVerifyOrder {
      stateObserver.onChanged(
        MediaListUIState.Content(
          MediaListModel.empty.copy(
            media = listOf(
              MediaListItemModel.empty.copy(id = MediaId(1L)),
              MediaListItemModel.empty.copy(
                id = MediaId(2L),
                watchButtonModel = WatchButtonModel.Unselected
              ),
            )
          )
        )
      )
    }
  }

  @Test
  fun `Given a params with searchType as popular, When refreshMovies method is called, Then first page with movies is returned`() {
    val params = MediaListParams.popularMovies
    viewModel.uiState.observeForever(stateObserver)

    viewModel.refreshMovies(params)

    coVerifyOrder {
      stateObserver.onChanged(MediaListUIState.Loading)
      stateObserver.onChanged(
        MediaListUIState.Content(
          MediaListModel.empty.copy(
            media = listOf(
              MediaListItemModel.empty.copy(id = MediaId(1L)),
              MediaListItemModel.empty.copy(id = MediaId(2L)),
            )
          )
        )
      )
    }
  }

  @Test
  fun `Given a params with searchType as popular, When loadMovies method is called, Then first page with movies is returned`() {
    val params = MediaListParams.popularMovies
    viewModel.uiState.observeForever(stateObserver)

    viewModel.loadMedias(params)

    coVerifyOrder {
      stateObserver.onChanged(MediaListUIState.Loading)
      stateObserver.onChanged(
        MediaListUIState.Content(
          MediaListModel.empty.copy(
            media = listOf(
              MediaListItemModel.empty.copy(id = MediaId(1L)),
              MediaListItemModel.empty.copy(id = MediaId(2L)),
            )
          )
        )
      )
    }
  }

  @Test
  fun `Given a pageIndex with value 20, When loadMoreMovies is called, Then second page with movies are returned`() {
    val params = MediaListParams.popularMovies
    val pageIndex = PageIndex(2)
    viewModel.uiState.observeForever(stateObserver)

    viewModel.loadMedias(params)
    viewModel.loadMoreMovies(pageIndex, params)

    coVerifyOrder {
      stateObserver.onChanged(MediaListUIState.Loading)
      stateObserver.onChanged(
        MediaListUIState.Content(
          MediaListModel.empty.copy(
            media = listOf(
              MediaListItemModel.empty.copy(id = MediaId(1L)),
              MediaListItemModel.empty.copy(id = MediaId(2L)),
            )
          )
        )
      )
      stateObserver.onChanged(MediaListUIState.Loading)
      stateObserver.onChanged(
        MediaListUIState.Content(
          MediaListModel.empty.copy(
            media = listOf(
              MediaListItemModel.empty.copy(id = MediaId(1L)),
              MediaListItemModel.empty.copy(id = MediaId(2L)),
              MediaListItemModel.empty.copy(id = MediaId(3L)),
              MediaListItemModel.empty.copy(id = MediaId(4L)),
            )
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
private val aPageSize = PageSize(2)
private val aGetMoviesUseCaseFirstPageParams =
  GetMediasUseCase.Params(MediaFilter.popularMovies, aFirstPage, aPageSize)
private val aGetMoviesUseCaseSecondPageParams =
  GetMediasUseCase.Params(MediaFilter.popularMovies, aSecondPage, aPageSize)
private val aFirstPageMoviesModel = listOf(
  MediaListItemModel.empty.copy(id = MediaId(1L)),
  MediaListItemModel.empty.copy(id = MediaId(2L)),
)
private val aFirstPageMovies = listOf(
  Media.Movie.empty.copy(id = MediaId(1)),
  Media.Movie.empty.copy(id = MediaId(2)),
)
private val aSecondPageMovies = listOf(
  Media.Movie.empty.copy(id = MediaId(3)),
  Media.Movie.empty.copy(id = MediaId(4)),
)
private val aFirstPageList = PageList<Media>(
  items = aFirstPageMovies,
  aFirstPage,
)
private val aSecondPageList = PageList<Media>(
  items = aSecondPageMovies,
  aSecondPage,
)
