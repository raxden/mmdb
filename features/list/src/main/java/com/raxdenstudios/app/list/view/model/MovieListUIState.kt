package com.raxdenstudios.app.list.view.model

internal sealed class MovieListUIState {
  object Loading : MovieListUIState()
  object EmptyContent : MovieListUIState()
  data class Content(val model: MovieListModel) : MovieListUIState()
  data class Error(val throwable: Throwable) : MovieListUIState()
}