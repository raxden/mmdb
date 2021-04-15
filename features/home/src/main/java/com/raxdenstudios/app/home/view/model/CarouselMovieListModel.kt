package com.raxdenstudios.app.home.view.model

import androidx.annotation.VisibleForTesting
import com.raxdenstudios.app.movie.view.model.MovieListItemModel
import com.raxdenstudios.commons.ext.replaceItem

data class CarouselMovieListModel(
  val label: String,
  val description: String,
  val movies: List<MovieListItemModel>,
) {

  fun hasMovies() = movies.isNotEmpty()

  fun replaceMovie(movie: MovieListItemModel): CarouselMovieListModel = copy(
    movies = movies.replaceItem(movie) { movieToReplace -> movieToReplace.id == movie.id }
  )

  companion object {
    @VisibleForTesting
    val empty = CarouselMovieListModel(
      label = "",
      description = "",
      movies = emptyList(),
    )
  }
}
