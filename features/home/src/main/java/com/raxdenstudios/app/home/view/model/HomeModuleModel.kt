package com.raxdenstudios.app.home.view.model

import com.raxdenstudios.app.movie.view.model.MediaFilterModel

sealed class HomeModuleModel {

  data class CarouselMovies(
    val mediaFilterModel: MediaFilterModel,
    val carouselMovieListModel: CarouselMovieListModel,
  ) : HomeModuleModel()

  object WatchlistNotLogged : HomeModuleModel()
  object WatchlistWithoutContent : HomeModuleModel()
}
