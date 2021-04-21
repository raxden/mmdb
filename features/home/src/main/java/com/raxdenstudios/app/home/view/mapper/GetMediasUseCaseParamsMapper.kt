package com.raxdenstudios.app.home.view.mapper

import com.raxdenstudios.app.home.domain.model.HomeModule
import com.raxdenstudios.app.movie.domain.GetMediasUseCase
import com.raxdenstudios.app.movie.domain.model.MediaFilter
import com.raxdenstudios.commons.util.DataMapper

internal class GetMediasUseCaseParamsMapper : DataMapper<HomeModule, GetMediasUseCase.Params>() {

  override fun transform(source: HomeModule): GetMediasUseCase.Params {
    val mediaFilter = when (source) {
      is HomeModule.NowPlaying -> MediaFilter.NowPlaying(source.mediaType)
      is HomeModule.Popular -> MediaFilter.Popular(source.mediaType)
      is HomeModule.TopRated -> MediaFilter.TopRated(source.mediaType)
      is HomeModule.Upcoming -> MediaFilter.Upcoming
      is HomeModule.WatchList -> MediaFilter.WatchList(source.mediaType)
    }
    return GetMediasUseCase.Params(mediaFilter)
  }
}
