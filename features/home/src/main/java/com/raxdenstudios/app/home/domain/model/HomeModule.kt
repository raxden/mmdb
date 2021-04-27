package com.raxdenstudios.app.home.domain.model

import com.raxdenstudios.app.media.domain.model.MediaType

sealed class HomeModule(
  open val mediaType: MediaType
) {

  companion object {
    val popularMovies = Popular(
      mediaType = MediaType.MOVIE
    )
    val nowPlayingMovies = NowPlaying(
      mediaType = MediaType.MOVIE
    )
    val topRatedMovies = TopRated(
      mediaType = MediaType.MOVIE
    )
    val upcomingMovies = Upcoming
    val watchListMovies = WatchList(
      mediaType = MediaType.MOVIE
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

  object Upcoming : HomeModule(MediaType.MOVIE)

  data class WatchList(
    override val mediaType: MediaType,
  ) : HomeModule(mediaType)
}
