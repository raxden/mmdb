package com.raxdenstudios.app.movie.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

sealed class SearchType : Parcelable {

  @Parcelize
  object Popular : SearchType()

  @Parcelize
  object NowPlaying : SearchType()

  @Parcelize
  object TopRated : SearchType()

  @Parcelize
  object Upcoming : SearchType()

  @Parcelize
  object WatchList : SearchType()
}
