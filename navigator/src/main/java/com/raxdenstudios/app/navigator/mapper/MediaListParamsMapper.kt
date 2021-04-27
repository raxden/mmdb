package com.raxdenstudios.app.navigator.mapper

import com.raxdenstudios.app.home.view.model.HomeModuleModel
import com.raxdenstudios.app.list.view.model.MediaListParams
import com.raxdenstudios.commons.util.DataMapper

internal class MediaListParamsMapper :
  DataMapper<HomeModuleModel.CarouselMedias, MediaListParams>() {

  override fun transform(source: HomeModuleModel.CarouselMedias): MediaListParams =
    when (source) {
      is HomeModuleModel.CarouselMedias.NowPlaying -> MediaListParams.NowPlaying
      is HomeModuleModel.CarouselMedias.Popular -> MediaListParams.Popular(source.mediaType)
      is HomeModuleModel.CarouselMedias.TopRated -> MediaListParams.TopRated(source.mediaType)
      is HomeModuleModel.CarouselMedias.Upcoming -> MediaListParams.Upcoming
      is HomeModuleModel.CarouselMedias.WatchList -> MediaListParams.WatchList(source.mediaType)
    }
}