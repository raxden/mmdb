package com.raxdenstudios.app.movie.view.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class MediaFilterModel(
  open val mediaTypeModel: MediaTypeModel
) : Parcelable {

  companion object {
    val popularMovies = Popular(
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
      val popularMoviesMediaFilter = Popular(
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
