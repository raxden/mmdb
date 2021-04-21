package com.raxdenstudios.app.media.view.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class MediaTypeModel : Parcelable {
  @Parcelize
  object TVShow : MediaTypeModel()

  @Parcelize
  object Movie : MediaTypeModel()
}