package com.raxdenstudios.app.media.view.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class MediaFilterModel(
  open val mediaTypeModel: MediaTypeModel
) : Parcelable {

  fun copyWith(mediaTypeModel: MediaTypeModel): MediaFilterModel = when (this) {
    is NowPlaying -> copy(mediaTypeModel = mediaTypeModel)
    is Popular -> copy(mediaTypeModel = mediaTypeModel)
    is TopRated -> copy(mediaTypeModel = mediaTypeModel)
    Upcoming -> this
    is WatchList -> copy(mediaTypeModel = mediaTypeModel)
  }

  companion object {
    val popularMovies = Popular(
      mediaTypeModel = MediaTypeModel.Movie
    )
    val topRatedMovies = TopRated(
      mediaTypeModel = MediaTypeModel.Movie
    )
    val nowPlayingMovies = NowPlaying(
      mediaTypeModel = MediaTypeModel.Movie
    )
    val watchlistMovies = WatchList(
      mediaTypeModel = MediaTypeModel.Movie
    )
  }

  @Parcelize
  data class NowPlaying(
    override val mediaTypeModel: MediaTypeModel
  ) : MediaFilterModel(mediaTypeModel)

  @Parcelize
  data class Popular(
    override val mediaTypeModel: MediaTypeModel
  ) : MediaFilterModel(mediaTypeModel) {

    companion object {
      val popularMovies = Popular(
        mediaTypeModel = MediaTypeModel.Movie
      )
    }
  }

  @Parcelize
  data class TopRated(
    override val mediaTypeModel: MediaTypeModel
  ) : MediaFilterModel(mediaTypeModel)

  @Parcelize
  object Upcoming : MediaFilterModel(MediaTypeModel.Movie)

  @Parcelize
  data class WatchList(
    override val mediaTypeModel: MediaTypeModel
  ) : MediaFilterModel(mediaTypeModel)
}
