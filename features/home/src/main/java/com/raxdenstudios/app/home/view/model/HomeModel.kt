package com.raxdenstudios.app.home.view.model

import androidx.annotation.VisibleForTesting
import com.raxdenstudios.commons.ext.replaceItem

data class HomeModel(
  val logged: Boolean,
  val modules: List<HomeModuleModel>
) {

  fun replaceModule(module: HomeModuleModel): HomeModel = copy(
    modules = modules.replaceItem(module) { moduleToReplace ->
      when (module) {
        is HomeModuleModel.CarouselMovies.NowPlaying ->
          moduleToReplace is HomeModuleModel.CarouselMovies.NowPlaying
        is HomeModuleModel.CarouselMovies.Popular ->
          moduleToReplace is HomeModuleModel.CarouselMovies.Popular
        is HomeModuleModel.CarouselMovies.TopRated ->
          moduleToReplace is HomeModuleModel.CarouselMovies.TopRated
        is HomeModuleModel.CarouselMovies.Upcoming ->
          moduleToReplace is HomeModuleModel.CarouselMovies.Upcoming
        HomeModuleModel.WatchList.NotLogged ->
          moduleToReplace is HomeModuleModel.WatchList.NotLogged
        is HomeModuleModel.WatchList.WithContent ->
          moduleToReplace is HomeModuleModel.WatchList.WithContent
        HomeModuleModel.WatchList.WithoutContent ->
          moduleToReplace is HomeModuleModel.WatchList.WithoutContent
      }
    }
  )

  companion object {
    @VisibleForTesting
    val empty = HomeModel(
      logged = false,
      modules = emptyList()
    )
  }
}
