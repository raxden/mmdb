package com.raxdenstudios.app.movie.domain

import androidx.annotation.VisibleForTesting

data class Picture(
  val thumbnail: Size.Thumbnail,
  val original: Size.Original,
) {

  companion object {
    @VisibleForTesting
    val empty = Picture(
      thumbnail = Size.Thumbnail("", ""),
      original = Size.Original("", ""),
    )
  }
}
