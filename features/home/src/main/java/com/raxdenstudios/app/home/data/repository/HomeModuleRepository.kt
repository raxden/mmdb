package com.raxdenstudios.app.home.data.repository

import com.raxdenstudios.app.home.data.local.datasource.HomeModuleLocalDataSource
import com.raxdenstudios.app.home.domain.model.HomeModule
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

internal class HomeModuleRepository @Inject constructor(
  private val homeModuleLocalDataSource: HomeModuleLocalDataSource,
) {

  fun observeModules(): Flow<List<HomeModule>> =
    homeModuleLocalDataSource.observe()
}
