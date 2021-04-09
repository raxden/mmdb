package com.raxdenstudios.app.home.view.model

sealed class HomeModuleModel(
  open val itemId: Long
) {

  fun replaceCarousel(carousel: CarouselMovieListModel): HomeModuleModel = when (this) {
    is CarouselMovies.NowPlaying -> copy(carouselMovieListModel = carousel)
    is CarouselMovies.Popular -> copy(carouselMovieListModel = carousel)
    is CarouselMovies.TopRated -> copy(carouselMovieListModel = carousel)
    is CarouselMovies.Upcoming -> copy(carouselMovieListModel = carousel)
    WatchList.NotLogged -> throw IllegalStateException("This module hasn't carousel")
    is WatchList.WithContent -> copy(carouselMovieListModel = carousel)
    WatchList.WithoutContent -> throw IllegalStateException("This module hasn't carousel")
  }

  sealed class CarouselMovies(
    override val itemId: Long
  ) : HomeModuleModel(itemId) {

    abstract val carouselMovieListModel: CarouselMovieListModel

    data class Popular(
      override val carouselMovieListModel: CarouselMovieListModel
    ) : CarouselMovies(1)

    data class NowPlaying(
      override val carouselMovieListModel: CarouselMovieListModel
    ) : CarouselMovies(2)

    data class TopRated(
      override val carouselMovieListModel: CarouselMovieListModel
    ) : CarouselMovies(3)

    data class Upcoming(
      override val carouselMovieListModel: CarouselMovieListModel
    ) : CarouselMovies(4)
  }

  sealed class WatchList(
    override val itemId: Long
  ) : HomeModuleModel(itemId) {
    object NotLogged: WatchList(5)
    object WithoutContent: WatchList(6)
    data class WithContent(
      val carouselMovieListModel: CarouselMovieListModel
    ): WatchList(7)
  }
}
