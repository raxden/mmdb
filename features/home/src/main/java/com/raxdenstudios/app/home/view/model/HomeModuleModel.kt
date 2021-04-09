package com.raxdenstudios.app.home.view.model

sealed class HomeModuleModel {

  fun replaceCarousel(carousel: CarouselMovieListModel): HomeModuleModel = when (this) {
    is CarouselMovies.NowPlaying -> copy(carouselMovieListModel = carousel)
    is CarouselMovies.Popular -> copy(carouselMovieListModel = carousel)
    is CarouselMovies.TopRated -> copy(carouselMovieListModel = carousel)
    is CarouselMovies.Upcoming -> copy(carouselMovieListModel = carousel)
    WatchList.NotLogged -> throw IllegalStateException("This module hasn't carousel")
    is WatchList.WithContent -> copy(carouselMovieListModel = carousel)
    WatchList.WithoutContent -> throw IllegalStateException("This module hasn't carousel")
  }

  sealed class CarouselMovies : HomeModuleModel() {

    abstract val carouselMovieListModel: CarouselMovieListModel

    data class Popular(
      override val carouselMovieListModel: CarouselMovieListModel
    ) : CarouselMovies()

    data class NowPlaying(
      override val carouselMovieListModel: CarouselMovieListModel
    ) : CarouselMovies()

    data class TopRated(
      override val carouselMovieListModel: CarouselMovieListModel
    ) : CarouselMovies()

    data class Upcoming(
      override val carouselMovieListModel: CarouselMovieListModel
    ) : CarouselMovies()
  }

  sealed class WatchList : HomeModuleModel() {
    object NotLogged: WatchList()
    object WithoutContent: WatchList()
    data class WithContent(
      val carouselMovieListModel: CarouselMovieListModel
    ): WatchList()
  }
}
