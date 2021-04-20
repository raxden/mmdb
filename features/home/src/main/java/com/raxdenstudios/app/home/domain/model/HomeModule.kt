package com.raxdenstudios.app.home.domain.model

import com.raxdenstudios.app.movie.domain.model.MediaType

sealed class HomeModule {

  companion object {
    val popularMovies = Popular(
      mediaType = MediaType.Movie
    )
    val nowPlayingMovies = NowPlaying(
      mediaType = MediaType.Movie
    )
    val topRatedMovies = TopRated(
      mediaType = MediaType.Movie
    )
    val upcomingMovies = Upcoming
    val watchListMovies = WatchList(
      mediaType = MediaType.Movie
    )
  }

  data class Popular(
    val mediaType: MediaType,
  ) : HomeModule()

  data class NowPlaying(
    val mediaType: MediaType,
  ) : HomeModule()

  data class TopRated(
    val mediaType: MediaType,
  ) : HomeModule()

  object Upcoming : HomeModule()

  data class WatchList(
    val mediaType: MediaType,
  ) : HomeModule()
}
