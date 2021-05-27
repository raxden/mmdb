package com.raxdenstudios.app.home.view.mapper

import com.raxdenstudios.app.home.domain.model.HomeModule
import com.raxdenstudios.app.home.view.model.HomeModuleModel

internal class HomeModuleModelMapper(
  private val carouselMediasModelMapper: CarouselMediasModelMapper
) {

  fun transform(
    homeModule: HomeModule,
  ): HomeModuleModel {
    return carouselMediasModelMapper.transform(homeModule)
  }
}
