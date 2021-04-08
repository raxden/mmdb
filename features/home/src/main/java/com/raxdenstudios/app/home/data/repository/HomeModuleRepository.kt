package com.raxdenstudios.app.home.data.repository

import com.raxdenstudios.app.home.data.local.datasource.HomeModuleLocalDataSource
import com.raxdenstudios.app.home.domain.model.HomeModule
import kotlinx.coroutines.flow.Flow

internal class HomeModuleRepository(
  private val homeModuleLocalDataSource: HomeModuleLocalDataSource,
) {

  fun modules(): Flow<List<HomeModule>> =
    homeModuleLocalDataSource.modules()
}
