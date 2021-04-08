package com.raxdenstudios.app.movie.domain

import androidx.annotation.VisibleForTesting
import org.threeten.bp.LocalDate

data class Movie(
  val id: Long,
  val title: String,
  val backdrop: Picture?,
  val poster: Picture,
  val release: LocalDate,
  val vote: Vote,
) {

  companion object {
    @VisibleForTesting
    val empty = Movie(
      id = 0L,
      title = "",
      backdrop = null,
      poster = Picture.empty,
      release = LocalDate.of(1970, 1, 1),
      vote = Vote.empty,
    )
  }
}
