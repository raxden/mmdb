package com.raxdenstudios.app.list.view.viewmodel

import androidx.lifecycle.Observer
import androidx.lifecycle.SavedStateHandle
import com.raxdenstudios.app.list.view.mapper.GetMediasUseCaseParamsMapper
import com.raxdenstudios.app.list.view.mapper.MediaListModelMapper
import com.raxdenstudios.app.list.view.model.MediaListModel
import com.raxdenstudios.app.list.view.model.MediaListParams
import com.raxdenstudios.app.media.domain.AddMediaToWatchListUseCase
import com.raxdenstudios.app.media.domain.GetMediasUseCase
import com.raxdenstudios.app.media.domain.RemoveMediaFromWatchListUseCase
import com.raxdenstudios.app.media.domain.model.Media
import com.raxdenstudios.app.media.domain.model.MediaFilter
import com.raxdenstudios.app.media.domain.model.MediaId
import com.raxdenstudios.app.media.view.mapper.MediaListItemModelMapper
import com.raxdenstudios.app.media.view.model.MediaListItemModel
import com.raxdenstudios.app.media.view.model.WatchButtonModel
import com.raxdenstudios.app.test.BaseTest
import com.raxdenstudios.commons.ResultData
import com.raxdenstudios.commons.pagination.Pagination
import com.raxdenstudios.commons.pagination.model.Page
import com.raxdenstudios.commons.pagination.model.PageIndex
import com.raxdenstudios.commons.pagination.model.PageList
import com.raxdenstudios.commons.pagination.model.PageSize
import com.raxdenstudios.commons.provider.StringProvider
import io.mockk.coEvery
import io.mockk.coVerifyOrder
import io.mockk.every
import io.mockk.mockk
import org.junit.Test

internal class MediaListViewModelTest : BaseTest() {

  private val addMediaToWatchListUseCase: AddMediaToWatchListUseCase = mockk {
    coEvery { this@mockk.invoke(any()) } returns ResultData.Success(Media.Movie.empty)
  }
  private val removeMediaFromWatchListUseCase: RemoveMediaFromWatchListUseCase = mockk {
    coEvery { this@mockk.invoke(any()) } returns ResultData.Success(true)
  }
  private val getMediasUseCase: GetMediasUseCase = mockk {
    coEvery {
      this@mockk.invoke(aGetMoviesUseCaseFirstPageParams)
    } returns ResultData.Success(aFirstPageList)
    coEvery {
      this@mockk.invoke(aGetMoviesUseCaseSecondPageParams)
    } returns ResultData.Success(aSecondPageList)
  }
  private val stateObserver: Observer<MediaListViewModel.UIState> = mockk(relaxed = true)
  private val paginationConfig = Pagination.Config.default.copy(
    initialPage = aFirstPage,
    pageSize = aPageSize,
    prefetchDistance = 0
  )
  private val getMediasUseCaseParamsMapper = GetMediasUseCaseParamsMapper()
  private val stringProvider: StringProvider = mockk(relaxed = true)
  private val mediaListItemModelMapper = MediaListItemModelMapper()
  private val mediaListModelMapper = MediaListModelMapper(
    stringProvider = stringProvider,
    mediaListItemModelMapper = mediaListItemModelMapper,
  )
  private val savedStateHandle: SavedStateHandle = mockk(relaxed = true)
  private val viewModel: MediaListViewModel by lazy {
    MediaListViewModel(
      savedStateHandle = savedStateHandle,
      paginationConfig = paginationConfig,
      getMediasUseCase = getMediasUseCase,
      addMediaToWatchListUseCase = addMediaToWatchListUseCase,
      removeMediaFromWatchListUseCase = removeMediaFromWatchListUseCase,
      getMediasUseCaseParamsMapper = getMediasUseCaseParamsMapper,
      mediaListModelMapper = mediaListModelMapper,
    )
  }

  @Test
  fun `when movie is added to watchlist, movie is replaced in model`() {
    val model = givenAMovieListModelWithResultsFromFirstPage()
    val itemToAddToWatchList = MediaListItemModel.empty.copy(id = MediaId(2L))
    viewModel.uiState.observeForever(stateObserver)

    viewModel.addMovieToWatchList(model, itemToAddToWatchList)

    coVerifyOrder {
      stateObserver.onChanged(
        MediaListViewModel.UIState.Content(
          MediaListModel.empty.copy(
            items = listOf(
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
        MediaListViewModel.UIState.Content(
          MediaListModel.empty.copy(
            items = listOf(
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
    every { savedStateHandle.get<MediaListParams>("params") } returns MediaListParams.popularMovies
    viewModel.uiState.observeForever(stateObserver)

    viewModel.refreshMovies()

    coVerifyOrder {
      stateObserver.onChanged(MediaListViewModel.UIState.Loading)
      stateObserver.onChanged(
        MediaListViewModel.UIState.Content(
          MediaListModel.empty.copy(
            items = listOf(
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
    every { savedStateHandle.get<MediaListParams>("params") } returns MediaListParams.popularMovies
    viewModel.uiState.observeForever(stateObserver)

    coVerifyOrder {
      stateObserver.onChanged(MediaListViewModel.UIState.Loading)
      stateObserver.onChanged(
        MediaListViewModel.UIState.Content(
          MediaListModel.empty.copy(
            items = listOf(
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
    every { savedStateHandle.get<MediaListParams>("params") } returns MediaListParams.popularMovies
    val pageIndex = PageIndex(2)
    viewModel.uiState.observeForever(stateObserver)

    viewModel.loadMoreMovies(pageIndex)

    coVerifyOrder {
      stateObserver.onChanged(MediaListViewModel.UIState.Loading)
      stateObserver.onChanged(
        MediaListViewModel.UIState.Content(
          MediaListModel.empty.copy(
            items = listOf(
              MediaListItemModel.empty.copy(id = MediaId(1L)),
              MediaListItemModel.empty.copy(id = MediaId(2L)),
            )
          )
        )
      )
      stateObserver.onChanged(MediaListViewModel.UIState.Loading)
      stateObserver.onChanged(
        MediaListViewModel.UIState.Content(
          MediaListModel.empty.copy(
            items = listOf(
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
      items = aFirstPageMoviesModel
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
