package com.raxdenstudios.app.home.view.mapper

import com.raxdenstudios.app.home.R
import com.raxdenstudios.app.home.domain.model.HomeModule
import com.raxdenstudios.app.home.view.model.CarouselMovieListModel
import com.raxdenstudios.app.movie.domain.Movie
import com.raxdenstudios.commons.provider.StringProvider

internal class CarouselMovieListModelMapper(
  private val stringProvider: StringProvider,
  private val movieListItemModelMapper: MovieListItemModelMapper,
) {

  fun transform(
    homeModule: HomeModule,
    movies: List<Movie>
  ): CarouselMovieListModel = when (homeModule) {
    HomeModule.NowPlayingMovies -> CarouselMovieListModel(
      label = stringProvider.getString(R.string.home_carousel_now_playing_movies),
      description = "",
      movies = movieListItemModelMapper.transform(movies),
    )
    HomeModule.PopularMovies -> CarouselMovieListModel(
      label = stringProvider.getString(R.string.home_carousel_popular_movies),
      description = "",
      movies = movieListItemModelMapper.transform(movies),
    )
    HomeModule.TopRatedMovies -> CarouselMovieListModel(
      label = stringProvider.getString(R.string.home_carousel_top_rated_movies),
      description = "",
      movies = movieListItemModelMapper.transform(movies),
    )
    HomeModule.UpcomingMovies -> CarouselMovieListModel(
      label = stringProvider.getString(R.string.home_carousel_upcoming_movies),
      description = "",
      movies = movieListItemModelMapper.transform(movies),
    )
    HomeModule.WatchListMovies -> CarouselMovieListModel(
      label = stringProvider.getString(R.string.home_carousel_upcoming_movies),
      description = "",
      movies = movieListItemModelMapper.transform(movies),
    )
  }
}
