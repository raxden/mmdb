package com.raxdenstudios.app.home.view.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

internal sealed class HomeMediaListParams: Parcelable {

  @Parcelize
  object Movies: HomeMediaListParams()
  @Parcelize
  object TVShows: HomeMediaListParams()
}
