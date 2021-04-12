package com.raxdenstudios.app.movie.data.local.mapper

import com.raxdenstudios.app.movie.data.local.model.MovieWatchListEntity
import com.raxdenstudios.app.movie.domain.Movie
import com.raxdenstudios.commons.util.DataMapper

internal class MovieToMovieWatchListDataMapper: DataMapper<Movie, MovieWatchListEntity>() {

  override fun transform(source: Movie) = MovieWatchListEntity(
    movieId = source.id
  )
}