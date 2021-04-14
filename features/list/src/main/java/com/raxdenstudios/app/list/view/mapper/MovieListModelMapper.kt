package com.raxdenstudios.app.list.view.mapper

import com.raxdenstudios.app.home.view.mapper.MovieListItemModelMapper
import com.raxdenstudios.app.list.view.model.MovieListModel
import com.raxdenstudios.app.movie.domain.model.Movie
import com.raxdenstudios.commons.pagination.model.PageList

internal class MovieListModelMapper(
  private val movieListItemModelMapper: MovieListItemModelMapper
) {

  fun transform(pageList: PageList<Movie>): MovieListModel = MovieListModel(
    movies = movieListItemModelMapper.transform(pageList.items)
  )
}