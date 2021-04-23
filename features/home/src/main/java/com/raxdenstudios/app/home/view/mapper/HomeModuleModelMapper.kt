package com.raxdenstudios.app.home.view.mapper

import com.raxdenstudios.app.home.domain.model.HomeModule
import com.raxdenstudios.app.home.view.model.HomeModuleModel
import com.raxdenstudios.app.media.data.remote.exception.UserNotLoggedException
import com.raxdenstudios.app.media.domain.model.Media
import com.raxdenstudios.app.media.view.model.MediaFilterModel
import com.raxdenstudios.app.media.view.model.MediaTypeModel
import com.raxdenstudios.commons.ResultData
import com.raxdenstudios.commons.getValueOrNull
import com.raxdenstudios.commons.pagination.model.PageList

internal class HomeModuleModelMapper(
  private val carouselMediaListModelMapper: CarouselMediaListModelMapper
) {

  fun transform(
    homeModule: HomeModule,
    resultData: ResultData<PageList<Media>>
  ): HomeModuleModel? {
    val mediaList = resultData.getValueOrNull()?.items ?: emptyList()
    val carouselMediaList = carouselMediaListModelMapper.transform(homeModule, mediaList)
    val hasContent = carouselMediaList.hasMedias()
    return when (homeModule) {
      is HomeModule.NowPlaying,
      is HomeModule.Popular,
      is HomeModule.TopRated,
      is HomeModule.Upcoming -> when {
        hasContent -> HomeModuleModel.CarouselMedias(carouselMediaList)
        else -> null
      }
      is HomeModule.WatchList -> when (resultData) {
        is ResultData.Error -> {
          if (resultData.throwable is UserNotLoggedException) HomeModuleModel.WatchlistNotLogged
          else null
        }
        is ResultData.Success -> when {
          hasContent -> HomeModuleModel.CarouselMedias(carouselMediaList)
          else -> HomeModuleModel.WatchlistWithoutContent
        }
      }
    }
  }
}
