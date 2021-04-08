package com.raxdenstudios.app.home.view.model

sealed class HomeUIState {
  object Loading : HomeUIState()
  object EmptyContent : HomeUIState()
  data class Content(val model: HomeModel) : HomeUIState()
  data class Error(val throwable: Throwable) : HomeUIState()
}
