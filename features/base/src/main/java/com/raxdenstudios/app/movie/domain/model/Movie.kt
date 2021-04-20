package com.raxdenstudios.app.movie.domain.model

import androidx.annotation.VisibleForTesting
import org.threeten.bp.LocalDate

data class Movie(
  val id: Long,
  val mediaType: MediaType,
  val title: String,
  val backdrop: Picture,
  val poster: Picture,
  val release: LocalDate,
  val vote: Vote,
  val watchList: Boolean,
) {

  companion object {
    fun withId(id: Long) = Movie(
      id = id,
      mediaType = MediaType.Movie,
      title = "",
      backdrop = Picture.Empty,
      poster = Picture.Empty,
      release = LocalDate.of(1970, 1, 1),
      vote = Vote.empty,
      watchList = false,
    )
    @VisibleForTesting
    val empty = Movie(
      id = 0L,
      mediaType = MediaType.Movie,
      title = "",
      backdrop = Picture.Empty,
      poster = Picture.Empty,
      release = LocalDate.of(1970, 1, 1),
      vote = Vote.empty,
      watchList = false,
    )
  }
}
