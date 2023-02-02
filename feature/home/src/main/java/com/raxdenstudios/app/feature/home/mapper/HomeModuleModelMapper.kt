package com.raxdenstudios.app.feature.home.mapper

import com.raxdenstudios.app.core.model.HomeModule
import com.raxdenstudios.app.feature.home.model.HomeModuleModel
import com.raxdenstudios.app.core.model.Media
import javax.inject.Inject

class HomeModuleModelMapper @Inject constructor(
    private val carouselModelMapper: CarouselModelMapper,
) {

    fun transform(
        modules: Map<HomeModule, List<Media>>,
    ): List<HomeModuleModel> = modules
        .filter { module -> module.value.isNotEmpty() }
        .map { (module, medias) -> carouselModelMapper.transform(module, medias)
    }
}
