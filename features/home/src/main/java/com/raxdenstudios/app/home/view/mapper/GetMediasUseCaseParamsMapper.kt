package com.raxdenstudios.app.home.view.mapper

import com.raxdenstudios.app.home.domain.model.HomeModule
import com.raxdenstudios.app.home.view.model.HomeModuleModel
import com.raxdenstudios.app.media.domain.GetMediasUseCase
import com.raxdenstudios.app.media.domain.model.MediaFilter
import com.raxdenstudios.app.media.domain.model.MediaType

internal class GetMediasUseCaseParamsMapper {

  fun transform(source: HomeModule): GetMediasUseCase.Params {
    val mediaFilter = when (source) {
      is HomeModule.NowPlaying -> MediaFilter.NowPlaying
      is HomeModule.Popular -> MediaFilter.Popular(source.mediaType)
      is HomeModule.TopRated -> MediaFilter.TopRated(source.mediaType)
      is HomeModule.Upcoming -> MediaFilter.Upcoming
      is HomeModule.WatchList -> MediaFilter.WatchList(source.mediaType)
    }
    return GetMediasUseCase.Params(mediaFilter)
  }

  fun transform(
    carouselMedias: HomeModuleModel.CarouselMedias,
    mediaType: MediaType
  ): GetMediasUseCase.Params {
    val mediaFilter = when (carouselMedias) {
      is HomeModuleModel.CarouselMedias.NowPlaying -> MediaFilter.NowPlaying
      is HomeModuleModel.CarouselMedias.Popular -> MediaFilter.Popular(mediaType)
      is HomeModuleModel.CarouselMedias.TopRated -> MediaFilter.TopRated(mediaType)
      is HomeModuleModel.CarouselMedias.Upcoming -> MediaFilter.Upcoming
      is HomeModuleModel.CarouselMedias.WatchList -> MediaFilter.WatchList(mediaType)
    }
    return GetMediasUseCase.Params(mediaFilter)
  }
}
