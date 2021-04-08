package com.raxdenstudios.app.home.view.model

sealed class WatchButtonModel {

  object Selected : WatchButtonModel()
  object Unselected : WatchButtonModel()
}
