package com.raxdenstudios.app.home.view.mapper

import com.raxdenstudios.app.home.R
import com.raxdenstudios.app.home.domain.model.HomeModule
import com.raxdenstudios.app.home.view.model.HomeModuleModel
import com.raxdenstudios.app.media.view.mapper.MediaListItemModelMapper
import com.raxdenstudios.commons.provider.StringProvider
import javax.inject.Inject

internal class CarouselMediasModelMapper @Inject constructor(
  private val stringProvider: StringProvider,
  private val mediaListItemModelMapper: MediaListItemModelMapper,
) {

  fun transform(
    homeModule: HomeModule,
  ): HomeModuleModel.CarouselMedias = when (homeModule) {
    is HomeModule.NowPlaying -> HomeModuleModel.CarouselMedias.NowPlaying(
      label = stringProvider.getString(R.string.home_carousel_now_playing_movies),
      medias = mediaListItemModelMapper.transform(homeModule.medias),
    )
    is HomeModule.Popular -> HomeModuleModel.CarouselMedias.Popular(
      label = stringProvider.getString(R.string.home_carousel_popular),
      medias = mediaListItemModelMapper.transform(homeModule.medias),
      mediaType = homeModule.mediaType,
    )
    is HomeModule.TopRated -> HomeModuleModel.CarouselMedias.TopRated(
      label = stringProvider.getString(R.string.home_carousel_top_rated),
      medias = mediaListItemModelMapper.transform(homeModule.medias),
      mediaType = homeModule.mediaType,
    )
    is HomeModule.Upcoming -> HomeModuleModel.CarouselMedias.Upcoming(
      label = stringProvider.getString(R.string.home_carousel_upcoming),
      medias = mediaListItemModelMapper.transform(homeModule.medias),
    )
    is HomeModule.WatchList -> HomeModuleModel.CarouselMedias.WatchList(
      label = stringProvider.getString(R.string.home_carousel_from_your_watchlist),
      medias = mediaListItemModelMapper.transform(homeModule.medias),
      mediaType = homeModule.mediaType,
    )
  }
}
