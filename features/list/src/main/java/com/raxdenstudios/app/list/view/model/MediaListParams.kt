package com.raxdenstudios.app.list.view.model

import android.os.Parcelable
import com.raxdenstudios.app.media.view.model.MediaFilterModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class MediaListParams(
  val mediaFilterModel: MediaFilterModel
) : Parcelable {

  companion object {
    val popularMovies = MediaListParams(
      mediaFilterModel = MediaFilterModel.Popular.popularMoviesMediaFilter
    )
  }
}