package com.raxdenstudios.app.list.view.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.raxdenstudios.app.account.domain.IsAccountLoggedUseCase
import com.raxdenstudios.app.base.BaseViewModel
import com.raxdenstudios.app.list.view.model.MediaListParams
import com.raxdenstudios.app.list.view.model.MovieListModel
import com.raxdenstudios.app.list.view.model.MovieListUIState
import com.raxdenstudios.app.movie.domain.AddMovieToWatchListUseCase
import com.raxdenstudios.app.movie.domain.GetMoviesUseCase
import com.raxdenstudios.app.movie.domain.RemoveMovieFromWatchListUseCase
import com.raxdenstudios.app.movie.view.mapper.MediaFilterModelToDomainMapper
import com.raxdenstudios.app.movie.view.mapper.MovieListItemModelMapper
import com.raxdenstudios.app.movie.view.model.MediaFilterModel
import com.raxdenstudios.app.movie.view.model.MovieListItemModel
import com.raxdenstudios.app.movie.view.model.WatchButtonModel
import com.raxdenstudios.commons.ResultData
import com.raxdenstudios.commons.coMap
import com.raxdenstudios.commons.ext.safeLaunch
import com.raxdenstudios.commons.onFailure
import com.raxdenstudios.commons.onSuccess
import com.raxdenstudios.commons.pagination.Pagination
import com.raxdenstudios.commons.pagination.model.*
import org.koin.core.KoinComponent
import org.koin.core.inject
import org.koin.core.parameter.parametersOf

internal class MovieListViewModel(
  private val getMoviesUseCase: GetMoviesUseCase,
  private val isAccountLoggedUseCase: IsAccountLoggedUseCase,
  private val addMovieToWatchListUseCase: AddMovieToWatchListUseCase,
  private val removeMovieFromWatchListUseCase: RemoveMovieFromWatchListUseCase,
  private val mediaFilterModelToDomainMapper: MediaFilterModelToDomainMapper,
  private val movieListItemModelMapper: MovieListItemModelMapper,
) : BaseViewModel(), KoinComponent {

  private val pagination: Pagination<MovieListItemModel> by inject { parametersOf(viewModelScope) }

  private val mState = MutableLiveData<MovieListUIState>()
  val state: LiveData<MovieListUIState> = mState

  override fun onCleared() {
    pagination.clear()
    super.onCleared()
  }

  fun addMovieToWatchList(model: MovieListModel, item: MovieListItemModel) =
    viewModelScope.safeLaunch {
      val itemToReplace = item.copy(watchButtonModel = WatchButtonModel.Selected)
      val params = AddMovieToWatchListUseCase.Params(item.id, item.mediaType)
      addMovieToWatchListUseCase.execute(params)
        .onFailure { error -> mState.value = MovieListUIState.Error(error) }
        .onSuccess { mState.value = MovieListUIState.Content(model.replaceMovie(itemToReplace)) }
    }

  fun removeMovieFromWatchList(model: MovieListModel, item: MovieListItemModel) =
    viewModelScope.safeLaunch {
      val itemToReplace = item.copy(watchButtonModel = WatchButtonModel.Unselected)
      val params = RemoveMovieFromWatchListUseCase.Params(item.id, item.mediaType)
      removeMovieFromWatchListUseCase.execute(params)
        .onFailure { error -> mState.value = MovieListUIState.Error(error) }
        .onSuccess { mState.value = MovieListUIState.Content(model.replaceMovie(itemToReplace)) }
    }

  fun refreshMovies(params: MediaListParams) {
    pagination.clear()
    requestFirstPage(params)
  }

  fun loadMovies(params: MediaListParams) {
    if (dataIsReadyOrLoading()) return
    requestFirstPage(params)
  }

  private fun dataIsReadyOrLoading() =
    mState.value is MovieListUIState.Content || mState.value is MovieListUIState.Loading

  private fun requestFirstPage(params: MediaListParams) {
    requestPage(PageIndex.first, MovieListModel.withFilter(params.mediaFilterModel))
  }

  private fun requestPage(pageIndex: PageIndex, model: MovieListModel) {
    pagination.requestPage(
      pageIndex = pageIndex,
      pageRequest = { page, pageSize -> pageRequest(model.mediaFilterModel, page, pageSize) },
      pageResponse = { pageResult -> pageResponse(model, pageResult) }
    )
  }

  fun loadMoreMovies(pageIndex: PageIndex, model: MovieListModel) {
    requestPage(pageIndex, model)
  }

  private fun pageResponse(model: MovieListModel, pageResult: PageResult<MovieListItemModel>) =
    when (pageResult) {
      is PageResult.Content -> handlePageResultContent(model, pageResult)
      is PageResult.Error -> mState.value = MovieListUIState.Error(pageResult.throwable)
      PageResult.Loading -> mState.value = MovieListUIState.Loading
      PageResult.NoMoreResults -> Unit
      PageResult.NoResults -> mState.value = MovieListUIState.EmptyContent
    }

  private fun handlePageResultContent(
    model: MovieListModel,
    pageResult: PageResult.Content<MovieListItemModel>
  ) {
    viewModelScope.safeLaunch {
      val isAccountLogged = isAccountLoggedUseCase.execute()
      mState.value = MovieListUIState.Content(
        model.copy(
          logged = isAccountLogged,
          movies = pageResult.items
        )
      )
    }
  }

  private suspend fun pageRequest(
    mediaFilterModel: MediaFilterModel,
    page: Page,
    pageSize: PageSize
  ): ResultData<PageList<MovieListItemModel>> {
    val mediaFilter = mediaFilterModelToDomainMapper.transform(mediaFilterModel)
    val useCaseParams = GetMoviesUseCase.Params(mediaFilter, page, pageSize)
    return getMoviesUseCase.execute(useCaseParams)
      .coMap { pageList -> pageList.map { items -> movieListItemModelMapper.transform(items) } }
  }
}

