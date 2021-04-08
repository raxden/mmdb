package com.raxdenstudios.app.home.data.local.datasource

import com.raxdenstudios.app.home.data.local.HomeModuleDatabase
import com.raxdenstudios.app.home.data.local.mapper.HomeModuleEntityToDomainMapper
import com.raxdenstudios.app.home.domain.model.HomeModule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class HomeModuleLocalDataSource(
  database: HomeModuleDatabase,
  private val homeModuleEntityToDomainMapper: HomeModuleEntityToDomainMapper,
) {

  private val dao = database.dao()

  fun modules(): Flow<List<HomeModule>> = dao.observeAll()
    .map { entityList -> homeModuleEntityToDomainMapper.transform(entityList) }
}
