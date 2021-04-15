package com.raxdenstudios.app.movie.view.model

sealed class WatchButtonModel {

  object Selected : WatchButtonModel()
  object Unselected : WatchButtonModel()
}