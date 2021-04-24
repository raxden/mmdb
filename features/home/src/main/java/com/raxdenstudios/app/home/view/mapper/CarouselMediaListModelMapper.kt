package com.raxdenstudios.app.home.view.mapper

import com.raxdenstudios.app.home.R
import com.raxdenstudios.app.home.domain.model.HomeModule
import com.raxdenstudios.app.home.view.model.CarouselMediaListModel
import com.raxdenstudios.app.media.domain.model.Media
import com.raxdenstudios.app.media.view.mapper.MediaListItemModelMapper
import com.raxdenstudios.app.media.view.model.MediaFilterModel
import com.raxdenstudios.commons.provider.StringProvider

internal class CarouselMediaListModelMapper(
  private val stringProvider: StringProvider,
  private val mediaListItemModelMapper: MediaListItemModelMapper,
) {

  fun transform(
    homeModule: HomeModule,
    media: List<Media>
  ): CarouselMediaListModel = when (homeModule) {
    is HomeModule.NowPlaying -> CarouselMediaListModel(
      label = stringProvider.getString(R.string.home_carousel_now_playing_movies),
      description = "",
      mediaFilterModel = MediaFilterModel.nowPlayingMovies,
      medias = mediaListItemModelMapper.transform(media),
    )
    is HomeModule.Popular -> CarouselMediaListModel(
      label = stringProvider.getString(R.string.home_carousel_popular_movies),
      description = "",
      mediaFilterModel = MediaFilterModel.popularMovies,
      medias = mediaListItemModelMapper.transform(media),
    )
    is HomeModule.TopRated -> CarouselMediaListModel(
      label = stringProvider.getString(R.string.home_carousel_top_rated_movies),
      description = "",
      mediaFilterModel = MediaFilterModel.topRatedMovies,
      medias = mediaListItemModelMapper.transform(media),
    )
    is HomeModule.Upcoming -> CarouselMediaListModel(
      label = stringProvider.getString(R.string.home_carousel_upcoming_movies),
      description = "",
      mediaFilterModel = MediaFilterModel.Upcoming,
      medias = mediaListItemModelMapper.transform(media),
    )
    is HomeModule.WatchList -> CarouselMediaListModel(
      label = stringProvider.getString(R.string.home_carousel_from_your_watchlist),
      description = "",
      mediaFilterModel = MediaFilterModel.watchlistMovies,
      medias = mediaListItemModelMapper.transform(media),
    )
  }
}
