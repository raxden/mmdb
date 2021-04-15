package com.raxdenstudios.app.movie.domain.model

import androidx.annotation.VisibleForTesting

sealed class Picture {

  object Empty : Picture()

  data class WithImage(
    val thumbnail: Size.Thumbnail,
    val original: Size.Original,
  ) : Picture() {

    companion object {
      @VisibleForTesting
      val empty = WithImage(
        thumbnail = Size.Thumbnail("", ""),
        original = Size.Original("", ""),
      )
    }
  }
}
