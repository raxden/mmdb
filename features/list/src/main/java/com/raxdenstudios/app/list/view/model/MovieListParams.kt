package com.raxdenstudios.app.list.view.model

import android.os.Parcelable
import com.raxdenstudios.app.movie.domain.model.SearchType
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieListParams(
  val searchType: SearchType
) : Parcelable