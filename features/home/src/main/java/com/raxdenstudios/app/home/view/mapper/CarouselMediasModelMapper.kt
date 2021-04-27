package com.raxdenstudios.app.home.view.mapper

import com.raxdenstudios.app.home.R
import com.raxdenstudios.app.home.domain.model.HomeModule
import com.raxdenstudios.app.home.view.model.HomeModuleModel
import com.raxdenstudios.app.media.domain.model.Media
import com.raxdenstudios.app.media.view.mapper.MediaListItemModelMapper
import com.raxdenstudios.commons.provider.StringProvider

internal class CarouselMediasModelMapper(
  private val stringProvider: StringProvider,
  private val mediaListItemModelMapper: MediaListItemModelMapper,
) {

  fun transform(
    homeModule: HomeModule,
    accountIsLogged: Boolean,
    media: List<Media>
  ): HomeModuleModel.CarouselMedias = when (homeModule) {
    is HomeModule.NowPlaying -> HomeModuleModel.CarouselMedias.NowPlaying(
      label = stringProvider.getString(R.string.home_carousel_now_playing_movies),
      medias = mediaListItemModelMapper.transform(media),
    )
    is HomeModule.Popular -> HomeModuleModel.CarouselMedias.Popular(
      label = stringProvider.getString(R.string.home_carousel_popular_movies),
      medias = mediaListItemModelMapper.transform(media),
      mediaType = homeModule.mediaType,
    )
    is HomeModule.TopRated -> HomeModuleModel.CarouselMedias.TopRated(
      label = stringProvider.getString(R.string.home_carousel_top_rated_movies),
      medias = mediaListItemModelMapper.transform(media),
      mediaType = homeModule.mediaType,
    )
    is HomeModule.Upcoming -> HomeModuleModel.CarouselMedias.Upcoming(
      label = stringProvider.getString(R.string.home_carousel_upcoming_movies),
      medias = mediaListItemModelMapper.transform(media),
    )
    is HomeModule.WatchList -> HomeModuleModel.CarouselMedias.WatchList(
      label = stringProvider.getString(R.string.home_carousel_from_your_watchlist),
      medias = mediaListItemModelMapper.transform(media),
      mediaType = homeModule.mediaType,
      requireSigIn = !accountIsLogged,
    )
  }
}
