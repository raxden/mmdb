package com.raxdenstudios.app.home.view.mapper

import com.raxdenstudios.app.home.R
import com.raxdenstudios.app.home.domain.model.HomeModule
import com.raxdenstudios.app.home.view.model.CarouselMovieListModel
import com.raxdenstudios.app.movie.domain.model.Media
import com.raxdenstudios.app.movie.view.mapper.MovieListItemModelMapper
import com.raxdenstudios.commons.provider.StringProvider

internal class CarouselMovieListModelMapper(
  private val stringProvider: StringProvider,
  private val movieListItemModelMapper: MovieListItemModelMapper,
) {

  fun transform(
    homeModule: HomeModule,
    media: List<Media>
  ): CarouselMovieListModel = when (homeModule) {
    is HomeModule.NowPlaying -> CarouselMovieListModel(
      label = stringProvider.getString(R.string.home_carousel_now_playing_movies),
      description = "",
      medias = movieListItemModelMapper.transform(media),
    )
    is HomeModule.Popular -> CarouselMovieListModel(
      label = stringProvider.getString(R.string.home_carousel_popular_movies),
      description = "",
      medias = movieListItemModelMapper.transform(media),
    )
    is HomeModule.TopRated -> CarouselMovieListModel(
      label = stringProvider.getString(R.string.home_carousel_top_rated_movies),
      description = "",
      medias = movieListItemModelMapper.transform(media),
    )
    is HomeModule.Upcoming -> CarouselMovieListModel(
      label = stringProvider.getString(R.string.home_carousel_upcoming_movies),
      description = "",
      medias = movieListItemModelMapper.transform(media),
    )
    is HomeModule.WatchList -> CarouselMovieListModel(
      label = stringProvider.getString(R.string.home_carousel_from_your_watchlist),
      description = "",
      medias = movieListItemModelMapper.transform(media),
    )
  }
}
