package com.raxdenstudios.app.movie.domain.model

sealed class MediaFilter(
  open val mediaType: MediaType
) {

  companion object {
    val watchListMovies = WatchList(
      mediaType = MediaType.Movie
    )
    val topRatedMovies = TopRated(
      mediaType = MediaType.Movie
    )
    val popularMovies = Popular(
      mediaType = MediaType.Movie
    )
    val nowPlayingMovies = NowPlaying(
      mediaType = MediaType.Movie
    )
  }

  data class NowPlaying(
    override val mediaType: MediaType
  ) : MediaFilter(mediaType)

  data class Popular(
    override val mediaType: MediaType
  ) : MediaFilter(mediaType)

  data class TopRated(
    override val mediaType: MediaType
  ) : MediaFilter(mediaType)

  object Upcoming : MediaFilter(MediaType.Movie)

  data class WatchList(
    override val mediaType: MediaType
  ) : MediaFilter(mediaType)
}