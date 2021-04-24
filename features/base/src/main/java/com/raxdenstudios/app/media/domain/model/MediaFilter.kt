package com.raxdenstudios.app.media.domain.model

sealed class MediaFilter(
  open val mediaType: MediaType
) {

  companion object {
    val popularMovies = Popular(
      mediaType = MediaType.Movie
    )
    val topRatedMovies = TopRated(
      mediaType = MediaType.Movie
    )
    val nowPlayingMovies = NowPlaying(
      mediaType = MediaType.Movie
    )
    val watchListMovies = WatchList(
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