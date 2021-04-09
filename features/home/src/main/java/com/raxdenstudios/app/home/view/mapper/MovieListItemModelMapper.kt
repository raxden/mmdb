package com.raxdenstudios.app.home.view.mapper

import com.raxdenstudios.app.home.view.model.MovieListItemModel
import com.raxdenstudios.app.home.view.model.WatchButtonModel
import com.raxdenstudios.app.movie.domain.Movie
import com.raxdenstudios.commons.util.DataMapper

internal class MovieListItemModelMapper : DataMapper<Movie, MovieListItemModel>() {

  override fun transform(source: Movie): MovieListItemModel = source.toModel()

  private fun Movie.toModel() = MovieListItemModel(
    id = id,
    title = title,
    image = poster.thumbnail.url,
    rating = vote.average.toString(),
    releaseDate = release.year.toString(),
    watchButtonModel = when {
      watchList -> WatchButtonModel.Selected
      else -> WatchButtonModel.Unselected
    }
  )
}
