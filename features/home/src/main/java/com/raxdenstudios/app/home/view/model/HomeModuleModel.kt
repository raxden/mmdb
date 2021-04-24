package com.raxdenstudios.app.home.view.model

import com.raxdenstudios.app.media.view.model.MediaFilterModel

sealed class HomeModuleModel {

  data class CarouselMedias(
    val carouselMediaListModel: CarouselMediaListModel,
  ) : HomeModuleModel()

  object WatchlistNotLogged : HomeModuleModel()
  object WatchlistWithoutContent : HomeModuleModel()

  fun getModuleItemId(): Long = when (this) {
    is CarouselMedias -> when (carouselMediaListModel.mediaFilterModel) {
      is MediaFilterModel.NowPlaying -> 1
      is MediaFilterModel.Popular -> 2
      is MediaFilterModel.TopRated -> 3
      MediaFilterModel.Upcoming -> 4
      is MediaFilterModel.WatchList -> 5
    }
    WatchlistNotLogged -> 6
    WatchlistWithoutContent -> 7
  }
}
