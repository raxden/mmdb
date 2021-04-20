package com.raxdenstudios.app.movie.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class SearchType : Parcelable {

  @Parcelize
  data class Popular(
    val mediaType: MediaType
  ) : SearchType()

  @Parcelize
  data class NowPlaying(
    val mediaType: MediaType
  ) : SearchType()

  @Parcelize
  data class TopRated(
    val mediaType: MediaType
  ) : SearchType()

  @Parcelize
  object Upcoming : SearchType()

  @Parcelize
  data class WatchList(
    val mediaType: MediaType
  ) : SearchType() {

    companion object {
      val empty = WatchList(
        mediaType = MediaType.Movie
      )
    }
  }
}
