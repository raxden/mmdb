package com.raxdenstudios.app.media.domain.model

sealed class MediaFilter(
  open val mediaType: MediaType
) {

  companion object {
    val popularMovies = Popular(
      mediaType = MediaType.MOVIE
    )
    val topRatedMovies = TopRated(
      mediaType = MediaType.MOVIE
    )
    val watchListMovies = WatchList(
      mediaType = MediaType.MOVIE
    )
  }

  object NowPlaying : MediaFilter(MediaType.MOVIE)

  data class Popular(
    override val mediaType: MediaType
  ) : MediaFilter(mediaType)

  data class TopRated(
    override val mediaType: MediaType
  ) : MediaFilter(mediaType)

  object Upcoming : MediaFilter(MediaType.MOVIE)

  data class WatchList(
    override val mediaType: MediaType
  ) : MediaFilter(mediaType)
}