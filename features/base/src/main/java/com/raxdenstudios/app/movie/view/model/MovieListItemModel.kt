package com.raxdenstudios.app.movie.view.model

import androidx.annotation.VisibleForTesting
import com.raxdenstudios.app.movie.domain.model.MediaType

data class MovieListItemModel(
  val id: Long,
  val mediaType: MediaType,
  val title: String,
  val image: String,
  val rating: String,
  val releaseDate: String,
  val watchButtonModel: WatchButtonModel,
) {

  companion object {
    @VisibleForTesting
    val empty = MovieListItemModel(
      id = 0L,
      mediaType = MediaType.Movie,
      title = "",
      image = "",
      rating = "0.0",
      releaseDate = "1970",
      watchButtonModel = WatchButtonModel.Unselected
    )
  }
}