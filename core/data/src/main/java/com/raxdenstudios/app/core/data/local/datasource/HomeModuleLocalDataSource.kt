package com.raxdenstudios.app.core.data.local.datasource

import com.raxdenstudios.app.core.data.local.mapper.HomeModuleEntityToDomainMapper
import com.raxdenstudios.app.core.data.local.mapper.HomeModuleToEntityMapper
import com.raxdenstudios.app.core.database.HomeModuleDatabase
import com.raxdenstudios.app.core.database.dao.HomeModuleDao
import com.raxdenstudios.app.core.database.model.HomeModuleEntity
import com.raxdenstudios.app.core.model.HomeModule
import com.raxdenstudios.commons.ResultData
import com.raxdenstudios.commons.ext.runCatching
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class HomeModuleLocalDataSource @Inject constructor(
    private val database: HomeModuleDatabase,
    private val homeModuleEntityToDomainMapper: HomeModuleEntityToDomainMapper,
    private val homeModuleToEntityMapper: HomeModuleToEntityMapper,
) {

    private val dao: HomeModuleDao
        get() {
            return database.dao()
        }

    suspend fun save(module: HomeModule): ResultData<Boolean> = runCatching {
        dao.update(homeModuleToEntityMapper.transform(module))
        true
    }

    suspend fun get(moduleId: Long): ResultData<HomeModule> = runCatching {
        homeModuleEntityToDomainMapper.transform(dao.get(moduleId))
    }

    fun observe(): Flow<List<HomeModule>> = dao.observeAll()
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
