package com.raxdenstudios.app.list.view.viewmodel

import androidx.lifecycle.Observer
import com.raxdenstudios.app.di.baseFeatureModule
import com.raxdenstudios.app.list.di.listFeatureModule
import com.raxdenstudios.app.list.view.model.MovieListModel
import com.raxdenstudios.app.list.view.model.MovieListParams
import com.raxdenstudios.app.list.view.model.MovieListUIState
import com.raxdenstudios.app.movie.domain.GetMoviesUseCase
import com.raxdenstudios.app.movie.domain.model.Movie
import com.raxdenstudios.app.movie.domain.model.SearchType
import com.raxdenstudios.app.movie.view.model.MovieListItemModel
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

internal class MovieListViewModelTest : BaseTest() {

  private val getMoviesUseCase: GetMoviesUseCase = mockk() {
    coEvery { execute(aGetMoviesUseCaseFirstPageParams) } returns ResultData.Success(aFirstPageList)
    coEvery { execute(aGetMoviesUseCaseSecondPageParams) } returns ResultData.Success(
      aSecondPageList
    )
  }
  private val stateObserver: Observer<MovieListUIState> = mockk(relaxed = true)

  override val modules: List<Module>
    get() = listOf(
      baseFeatureModule,
      listFeatureModule,
      module {
        factory(override = true) { getMoviesUseCase }
      }
    )

  private val viewModel: MovieListViewModel by inject()

  @Test
  fun `Given a params with searchType as popular, When loadMovies method is called, Then first page with movies is returned`() {
    val params = MovieListParams(SearchType.Popular)
    viewModel.state.observeForever(stateObserver)

    viewModel.loadMovies(params)

    coVerifyOrder {
      stateObserver.onChanged(MovieListUIState.Loading)
      stateObserver.onChanged(
        MovieListUIState.Content(
          MovieListModel(
            searchType = SearchType.Popular,
            movies = aFirstPageMoviesModel
          )
        )
      )
    }
  }

  @Test
  fun `Given a model with results from first page and pageIndex with value 8, When loadMoreMovies is called, Then second page with movies is returned`() {
    val model = givenAMovieListModelWithResultsFromFirstPage()
    val pageIndex = PageIndex(8)
    viewModel.state.observeForever(stateObserver)

    viewModel.loadMoreMovies(pageIndex, model)

    coVerifyOrder {
      stateObserver.onChanged(MovieListUIState.Loading)
      stateObserver.onChanged(
        MovieListUIState.Content(
          MovieListModel(
            searchType = SearchType.Popular,
            movies = aSecondPageMoviesModel
          )
        )
      )
    }
  }

  private fun givenAMovieListModelWithResultsFromFirstPage(): MovieListModel = MovieListModel(
    searchType = SearchType.Popular,
    movies = aFirstPageMoviesModel
  )
}

private val aFirstPage = Page(1)
private val aSecondPage = Page(2)
private val aPageSize = PageSize.defaultSize
private val aGetMoviesUseCaseFirstPageParams =
  GetMoviesUseCase.Params(SearchType.Popular, aFirstPage, aPageSize)
private val aGetMoviesUseCaseSecondPageParams =
  GetMoviesUseCase.Params(SearchType.Popular, aSecondPage, aPageSize)
private val aFirstPageMoviesModel = listOf(
  MovieListItemModel.empty.copy(id = 1L),
  MovieListItemModel.empty.copy(id = 2L),
  MovieListItemModel.empty.copy(id = 3L),
  MovieListItemModel.empty.copy(id = 4L),
  MovieListItemModel.empty.copy(id = 5L),
  MovieListItemModel.empty.copy(id = 6L),
  MovieListItemModel.empty.copy(id = 7L),
  MovieListItemModel.empty.copy(id = 8L),
  MovieListItemModel.empty.copy(id = 9L),
  MovieListItemModel.empty.copy(id = 10L),
)
private val aSecondPageMoviesModel = listOf(
  MovieListItemModel.empty.copy(id = 11L),
  MovieListItemModel.empty.copy(id = 12L),
  MovieListItemModel.empty.copy(id = 13L),
  MovieListItemModel.empty.copy(id = 14L),
  MovieListItemModel.empty.copy(id = 15L),
  MovieListItemModel.empty.copy(id = 16L),
  MovieListItemModel.empty.copy(id = 17L),
  MovieListItemModel.empty.copy(id = 18L),
  MovieListItemModel.empty.copy(id = 19L),
  MovieListItemModel.empty.copy(id = 20L),
)
private val aFirstPageMovies = listOf(
  Movie.empty.copy(id = 1L),
  Movie.empty.copy(id = 2L),
  Movie.empty.copy(id = 3L),
  Movie.empty.copy(id = 4L),
  Movie.empty.copy(id = 5L),
  Movie.empty.copy(id = 6L),
  Movie.empty.copy(id = 7L),
  Movie.empty.copy(id = 8L),
  Movie.empty.copy(id = 9L),
  Movie.empty.copy(id = 10L),
)
private val aSecondPageMovies = listOf(
  Movie.empty.copy(id = 11L),
  Movie.empty.copy(id = 12L),
  Movie.empty.copy(id = 13L),
  Movie.empty.copy(id = 14L),
  Movie.empty.copy(id = 15L),
  Movie.empty.copy(id = 16L),
  Movie.empty.copy(id = 17L),
  Movie.empty.copy(id = 18L),
  Movie.empty.copy(id = 19L),
  Movie.empty.copy(id = 20L),
)
private val aFirstPageList = PageList(
  items = aFirstPageMovies,
  aFirstPage,
)
private val aSecondPageList = PageList(
  items = aSecondPageMovies,
  aSecondPage,
)