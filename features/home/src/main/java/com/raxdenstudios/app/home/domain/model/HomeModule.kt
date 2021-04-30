package com.raxdenstudios.app.home.domain.model

import com.raxdenstudios.app.media.domain.model.Media
import com.raxdenstudios.app.media.domain.model.MediaType

sealed class HomeModule(
  open val mediaType: MediaType
) {

  abstract val medias: List<Media>

  companion object {
    val popularMovies = Popular(
      mediaType = MediaType.MOVIE,
      medias = emptyList(),
    )
    val nowPlayingMovies = NowPlaying(
      medias = emptyList(),
    )
    val topRatedMovies = TopRated(
      mediaType = MediaType.MOVIE,
      medias = emptyList(),
    )
    val upcomingMovies = Upcoming(
      medias = emptyList(),
    )
    val watchListMovies = WatchList(
      mediaType = MediaType.MOVIE,
      medias = emptyList()
    )
  }

  data class Popular(
    override val mediaType: MediaType,
    override val medias: List<Media>,
  ) : HomeModule(mediaType)

  data class NowPlaying(
    override val medias: List<Media.Movie>,
  ) : HomeModule(MediaType.MOVIE)

  data class TopRated(
    override val mediaType: MediaType,
    override val medias: List<Media>,
  ) : HomeModule(mediaType)

  data class Upcoming(
    override val medias: List<Media.Movie>,
  ) : HomeModule(MediaType.MOVIE)

  data class WatchList(
    override val mediaType: MediaType,
    override val medias: List<Media>,
  ) : HomeModule(mediaType)
}
