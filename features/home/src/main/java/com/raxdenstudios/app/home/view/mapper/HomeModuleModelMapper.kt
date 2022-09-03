package com.raxdenstudios.app.home.view.mapper

import com.raxdenstudios.app.home.domain.model.HomeModule
import com.raxdenstudios.app.home.view.model.HomeModuleModel
import javax.inject.Inject

internal class HomeModuleModelMapper @Inject constructor(
    private val carouselMediasModelMapper: CarouselMediasModelMapper,
) {

    fun transform(
        homeModule: HomeModule,
    ): HomeModuleModel {
        return carouselMediasModelMapper.transform(homeModule)
    }
}
