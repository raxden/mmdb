package com.raxdenstudios.app.list.view

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.raxdenstudios.app.base.BaseViewModel
import com.raxdenstudios.app.home.domain.GetMoviesUseCase
import com.raxdenstudios.app.list.view.mapper.MovieListModelMapper
import com.raxdenstudios.app.list.view.model.MovieListParams
import com.raxdenstudios.app.list.view.model.MovieListUIState
import com.raxdenstudios.commons.ext.safeLaunch
import com.raxdenstudios.commons.fold
import com.raxdenstudios.commons.map

internal class MovieListViewModel(
  private val getMoviesUseCase: GetMoviesUseCase,
  private val movieListModelMapper: MovieListModelMapper
) : BaseViewModel() {

  private val mState = MutableLiveData<MovieListUIState>()
  val state: LiveData<MovieListUIState> = mState

  fun loadMovies(params: MovieListParams) = viewModelScope.safeLaunch {
    mState.value = MovieListUIState.Loading

    val useCaseParams = GetMoviesUseCase.Params(params.searchType)
    getMoviesUseCase.execute(useCaseParams)
      .map { pageList -> movieListModelMapper.transform(pageList) }
      .fold(
        onSuccess = { model -> MovieListUIState.Content(model) },
        onError = { error -> MovieListUIState.Error(error) }
      )
  }
}