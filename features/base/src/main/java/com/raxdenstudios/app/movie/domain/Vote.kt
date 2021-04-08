package com.raxdenstudios.app.movie.domain

import androidx.annotation.VisibleForTesting

data class Vote(
  val average: Float,
  val count: Int,
) {

  companion object {
    @VisibleForTesting
    val empty = Vote(
      average = 0.0f,
      count = 0,
    )
  }
}
