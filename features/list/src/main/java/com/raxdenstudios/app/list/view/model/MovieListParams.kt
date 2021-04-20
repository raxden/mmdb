package com.raxdenstudios.app.list.view.model

import android.os.Parcelable
import com.raxdenstudios.app.movie.view.model.MediaFilterModel
import kotlinx.parcelize.Parcelize

@Parcelize
data class MovieListParams(
  val mediaFilterModel: MediaFilterModel
) : Parcelable {

  companion object {
    val popularMovies = MovieListParams(
      mediaFilterModel = MediaFilterModel.Popular.popularMoviesMediaFilter
    )
  }
}