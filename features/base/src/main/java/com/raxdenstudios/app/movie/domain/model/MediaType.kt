package com.raxdenstudios.app.movie.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class MediaType : Parcelable {
  @Parcelize
  object Movie : MediaType()

  @Parcelize
  object TVShow : MediaType()
}
