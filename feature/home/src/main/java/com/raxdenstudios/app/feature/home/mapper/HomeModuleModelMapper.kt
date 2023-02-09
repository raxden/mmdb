package com.raxdenstudios.app.feature.home.mapper

import com.raxdenstudios.app.core.model.HomeModule
import com.raxdenstudios.app.feature.home.model.HomeModuleModel
import com.raxdenstudios.commons.DataMapper
import javax.inject.Inject

class HomeModuleModelMapper @Inject constructor(
    private val carouselModelMapper: CarouselModelMapper,
) : DataMapper<HomeModule, HomeModuleModel>() {

    override fun transform(source: HomeModule): HomeModuleModel = when (source) {
        is HomeModule.Carousel -> carouselModelMapper.transform(source)
        is HomeModule.OtherModule -> HomeModuleModel.OtherModule(source.id)
    }
}
