package com.raxdenstudios.app.movie.view.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class MediaTypeModel : Parcelable {
  @Parcelize
  object TVShow : MediaTypeModel()

  @Parcelize
  object Movie : MediaTypeModel()
}