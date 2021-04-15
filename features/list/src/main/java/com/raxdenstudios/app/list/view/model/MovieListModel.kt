package com.raxdenstudios.app.list.view.model

import com.raxdenstudios.app.movie.domain.model.SearchType
import com.raxdenstudios.app.movie.view.model.MovieListItemModel

data class MovieListModel(
  val searchType: SearchType,
  val movies: List<MovieListItemModel>,
) {

  companion object {
    val empty = MovieListModel(
      searchType = SearchType.Popular,
      movies = emptyList()
    )

    fun withSearchType(searchType: SearchType) = MovieListModel(
      searchType = searchType,
      movies = emptyList()
    )
  }
}