package com.raxdenstudios.app.home.view.model

import androidx.annotation.VisibleForTesting

data class HomeModel(
  val logged: Boolean,
  val modules: List<HomeModuleModel>
) {

  companion object {
    @VisibleForTesting
    val empty = HomeModel(
      logged = false,
      modules = emptyList()
    )
  }
}
