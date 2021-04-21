package com.raxdenstudios.app.media.domain.model

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
