package com.raxdenstudios.app.home.data.local.datasource

import com.raxdenstudios.app.home.data.local.HomeModuleDao
import com.raxdenstudios.app.home.data.local.mapper.HomeModuleEntityToDomainMapper
import com.raxdenstudios.app.home.data.local.model.HomeModuleEntity
import com.raxdenstudios.app.home.domain.model.HomeModule
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

internal class HomeModuleLocalDataSource(
  private val dao: HomeModuleDao,
  private val homeModuleEntityToDomainMapper: HomeModuleEntityToDomainMapper,
) {

  fun modules(): Flow<List<HomeModule>> = dao.observeAll()
    .map { entityList ->
      if (entityList.isEmpty()) initModules()
      homeModuleEntityToDomainMapper.transform(entityList)
    }

  private suspend fun initModules() {
    val modules = listOf(
      HomeModuleEntity.popular,
      HomeModuleEntity.nowPlaying,
      HomeModuleEntity.topRated,
      HomeModuleEntity.upcoming,
      HomeModuleEntity.watchList,
    )
    dao.insert(modules)
  }
}
