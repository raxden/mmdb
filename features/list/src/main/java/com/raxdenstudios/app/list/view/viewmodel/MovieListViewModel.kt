package com.raxdenstudios.app.list.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.raxdenstudios.app.base.BaseViewModel
import com.raxdenstudios.app.list.view.model.MovieListModel
import com.raxdenstudios.app.list.view.model.MovieListParams
import com.raxdenstudios.app.list.view.model.MovieListUIState
import com.raxdenstudios.app.movie.domain.GetMoviesUseCase
import com.raxdenstudios.app.movie.domain.model.SearchType
import com.raxdenstudios.app.movie.view.mapper.MovieListItemModelMapper
import com.raxdenstudios.app.movie.view.model.MovieListItemModel
import com.raxdenstudios.commons.ResultData
import com.raxdenstudios.commons.coMap
import com.raxdenstudios.commons.pagination.Pagination
import com.raxdenstudios.commons.pagination.model.*
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.core.parameter.parametersOf

internal class MovieListViewModel(
  private val getMoviesUseCase: GetMoviesUseCase,
  private val movieListItemModelMapper: MovieListItemModelMapper,
) : BaseViewModel(), KoinComponent {

  private val pagination: Pagination<MovieListItemModel> by inject { parametersOf(viewModelScope) }

  private val mState = MutableLiveData<MovieListUIState>()
  val state: LiveData<MovieListUIState> = mState

  override fun onCleared() {
    pagination.clear()
    super.onCleared()
  }

  fun movieSelected(model: MovieListModel, item: MovieListItemModel) {

  }

  fun addMovieToWatchList(model: MovieListModel, item: MovieListItemModel) {

  }

  fun removeMovieFromWatchList(model: MovieListModel, item: MovieListItemModel) {

  }

  fun refreshMovies(params: MovieListParams) {
    pagination.clear()
    requestFirstPage(params)
  }

  fun loadMovies(params: MovieListParams) {
    if (dataIsReadyOrLoading()) return
    requestFirstPage(params)
  }

  private fun dataIsReadyOrLoading() =
    mState.value is MovieListUIState.Content || mState.value is MovieListUIState.Loading

  private fun requestFirstPage(params: MovieListParams) {
    requestPage(PageIndex.first, MovieListModel.withSearchType(params.searchType))
  }

  private fun requestPage(pageIndex: PageIndex, model: MovieListModel) {
    pagination.requestPage(
      pageIndex = pageIndex,
      pageRequest = { page, pageSize -> pageRequest(model.searchType, page, pageSize) },
      pageResponse = { pageResult -> pageResponse(model, pageResult) }
    )
  }

  fun loadMoreMovies(pageIndex: PageIndex, model: MovieListModel) {
    requestPage(pageIndex, model)
  }

  private fun pageResponse(model: MovieListModel, pageResult: PageResult<MovieListItemModel>) =
    when (pageResult) {
      is PageResult.Content ->
        mState.value = MovieListUIState.Content(model.copy(movies = pageResult.items))
      is PageResult.Error -> mState.value = MovieListUIState.Error(pageResult.throwable)
      PageResult.Loading -> mState.value = MovieListUIState.Loading
      PageResult.NoMoreResults -> Unit
      PageResult.NoResults -> mState.value = MovieListUIState.EmptyContent
    }

  private suspend fun pageRequest(
    searchType: SearchType,
    page: Page,
    pageSize: PageSize
  ): ResultData<PageList<MovieListItemModel>> {
    val useCaseParams = GetMoviesUseCase.Params(searchType, page, pageSize)
    return getMoviesUseCase.execute(useCaseParams)
      .coMap { pageList -> pageList.map { items -> movieListItemModelMapper.transform(items) } }
  }
}