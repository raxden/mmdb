package com.raxdenstudios.app.home.view.mapper

import com.raxdenstudios.app.home.domain.model.HomeModule
import com.raxdenstudios.app.home.view.model.HomeModuleModel
import com.raxdenstudios.app.media.domain.model.Media
import com.raxdenstudios.commons.ResultData
import com.raxdenstudios.commons.getValueOrNull
import com.raxdenstudios.commons.pagination.model.PageList

internal class HomeModuleModelMapper(
  private val carouselMediasModelMapper: CarouselMediasModelMapper
) {

  fun transform(
    homeModule: HomeModule,
    accountIsLogged: Boolean,
    resultData: ResultData<PageList<Media>>
  ): HomeModuleModel {
    val mediaList = resultData.getValueOrNull()?.items ?: emptyList()
    return carouselMediasModelMapper.transform(homeModule, accountIsLogged, mediaList)
  }
}
