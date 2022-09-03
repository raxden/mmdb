package com.raxdenstudios.app.home.view.mapper

import com.raxdenstudios.app.home.domain.model.HomeModule
import com.raxdenstudios.app.home.view.model.HomeMediaListModel
import javax.inject.Inject

internal class HomeModelMapper @Inject constructor(
    private val homeModuleModelMapper: HomeModuleModelMapper,
) {

    fun transform(
        homeModules: List<HomeModule>,
    ): HomeMediaListModel = HomeMediaListModel(
        modules = homeModules.map { homeModule ->
            homeModuleModelMapper.transform(homeModule)
        },
    )
}
