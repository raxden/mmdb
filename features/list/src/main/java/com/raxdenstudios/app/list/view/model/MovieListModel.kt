package com.raxdenstudios.app.list.view.model

import com.raxdenstudios.app.movie.view.model.MediaFilterModel
import com.raxdenstudios.app.movie.view.model.MovieListItemModel
import com.raxdenstudios.commons.ext.replaceItem

data class MovieListModel(
  val mediaFilterModel: MediaFilterModel,
  val logged: Boolean,
  val movies: List<MovieListItemModel>,
) {

  fun replaceMovie(movieListItemModel: MovieListItemModel): MovieListModel = this.copy(
    movies = this.movies.replaceItem(movieListItemModel) { it.id == movieListItemModel.id }
  )

  companion object {
    val empty = MovieListModel(
      logged = false,
      mediaFilterModel = MediaFilterModel.Popular.popularMoviesMediaFilter,
      movies = emptyList()
    )

    fun withFilter(mediaFilterModel: MediaFilterModel) = MovieListModel(
      logged = false,
      mediaFilterModel = mediaFilterModel,
      movies = emptyList()
    )
  }
}