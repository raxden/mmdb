package com.raxdenstudios.app.home.view.mapper

import com.raxdenstudios.app.home.domain.model.HomeModule
import com.raxdenstudios.app.home.view.model.HomeModel

internal class HomeModelMapper(
  private val homeModuleModelMapper: HomeModuleModelMapper,
) {

  fun transform(
    accountIsLogged: Boolean,
    homeModules: List<HomeModule>,
  ): HomeModel = HomeModel(
    modules = homeModules.map { homeModule ->
      homeModuleModelMapper.transform(accountIsLogged, homeModule)
    },
    logged = accountIsLogged,
  )
}