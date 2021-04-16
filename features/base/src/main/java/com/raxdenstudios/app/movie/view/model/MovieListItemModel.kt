package com.raxdenstudios.app.movie.view.model

import androidx.annotation.VisibleForTesting

data class MovieListItemModel(
  val id: Long,
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
      title = "",
      image = "",
      rating = "0.0",
      releaseDate = "1970",
      watchButtonModel = WatchButtonModel.Unselected
    )
  }
}