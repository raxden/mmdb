package com.raxdenstudios.app.list.view

import androidx.lifecycle.Observer
import com.raxdenstudios.app.di.baseFeatureModule
import com.raxdenstudios.app.home.domain.GetMoviesUseCase
import com.raxdenstudios.app.home.view.model.MovieListItemModel
import com.raxdenstudios.app.list.di.listFeatureModule
import com.raxdenstudios.app.list.view.model.MovieListModel
import com.raxdenstudios.app.list.view.model.MovieListParams
import com.raxdenstudios.app.list.view.model.MovieListUIState
import com.raxdenstudios.app.movie.domain.model.Movie
import com.raxdenstudios.app.movie.domain.model.SearchType
import com.raxdenstudios.app.test.BaseTest
import com.raxdenstudios.commons.ResultData
import com.raxdenstudios.commons.pagination.model.Page
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
  fun `Given a params with searchType as popular, When loadMovies method is called, Then data is loaded`() {
    viewModel.state.observeForever(stateObserver)

    viewModel.loadMovies(MovieListParams(SearchType.Popular))

    coVerifyOrder {
      stateObserver.onChanged(MovieListUIState.Loading)
      stateObserver.onChanged(
        MovieListUIState.Content(
          MovieListModel(
            listOf(
              MovieListItemModel.empty.copy(id = 1L),
              MovieListItemModel.empty.copy(id = 2L),
            )
          )
        )
      )
    }
  }
}

private val aFirstPage = Page(1)
private val aPageSize = PageSize.defaultSize
private val aGetMoviesUseCaseFirstPageParams =
  GetMoviesUseCase.Params(SearchType.Popular, aFirstPage, aPageSize)
private val aFirstPageMovies = listOf(
  Movie.empty.copy(id = 1L),
  Movie.empty.copy(id = 2L),
)
private val aFirstPageList = PageList(
  items = aFirstPageMovies,
  aFirstPage,
)