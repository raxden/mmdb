package com.raxdenstudios.app.home.view.model

sealed class HomeModuleModel {

  sealed class CarouselMovies : HomeModuleModel() {

    abstract val carouselMovieListModel: CarouselMovieListModel

    fun replaceCarousel(carousel: CarouselMovieListModel): CarouselMovies = when (this) {
      is NowPlaying -> copy(carouselMovieListModel = carousel)
      is Popular -> copy(carouselMovieListModel = carousel)
      is TopRated -> copy(carouselMovieListModel = carousel)
      is Upcoming -> copy(carouselMovieListModel = carousel)
    }

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
}
