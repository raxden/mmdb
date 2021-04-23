package com.raxdenstudios.app.home.domain.model

import com.raxdenstudios.app.media.domain.model.MediaType

sealed class HomeModule(
  open val mediaType: MediaType
) {

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
    override val mediaType: MediaType,
  ) : HomeModule(mediaType)

  data class NowPlaying(
    override val mediaType: MediaType,
  ) : HomeModule(mediaType)

  data class TopRated(
    override val mediaType: MediaType,
  ) : HomeModule(mediaType)

  object Upcoming : HomeModule(MediaType.Movie)

  data class WatchList(
    override val mediaType: MediaType,
  ) : HomeModule(mediaType)
}
