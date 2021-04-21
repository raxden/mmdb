package com.raxdenstudios.app.home.view.model

import com.raxdenstudios.app.media.view.model.MediaFilterModel

sealed class HomeModuleModel {

  data class CarouselMedias(
    val mediaFilterModel: MediaFilterModel,
    val carouselMediaListModel: CarouselMediaListModel,
  ) : HomeModuleModel()

  object WatchlistNotLogged : HomeModuleModel()
  object WatchlistWithoutContent : HomeModuleModel()
}
