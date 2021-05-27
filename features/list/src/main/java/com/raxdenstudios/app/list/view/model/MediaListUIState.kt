package com.raxdenstudios.app.list.view.model

internal sealed class MediaListUIState {
  object Loading : MediaListUIState()
  object EmptyContent : MediaListUIState()
  data class Content(val model: MediaListModel) : MediaListUIState()
  data class Error(val throwable: Throwable) : MediaListUIState()
}
