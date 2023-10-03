package com.raxdenstudios.app.core.data.local.datasource

import com.raxdenstudios.app.core.data.local.mapper.ExceptionToErrorMapper
import com.raxdenstudios.app.core.data.local.mapper.HomeModuleEntityToDomainMapper
import com.raxdenstudios.app.core.data.local.mapper.HomeModuleToEntityMapper
import com.raxdenstudios.app.core.database.HomeModuleDatabase
import com.raxdenstudios.app.core.database.dao.HomeModuleDao
import com.raxdenstudios.app.core.database.model.HomeModuleEntity
import com.raxdenstudios.app.core.model.ErrorDomain
import com.raxdenstudios.app.core.model.HomeModule
import com.raxdenstudios.commons.core.ResultData
import com.raxdenstudios.commons.core.ext.coRunCatching
import com.raxdenstudios.commons.core.ext.mapFailure
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class HomeModuleLocalDataSource @Inject constructor(
    private val database: HomeModuleDatabase,
    private val homeModuleEntityToDomainMapper: HomeModuleEntityToDomainMapper,
    private val homeModuleToEntityMapper: HomeModuleToEntityMapper,
    private val exceptionToErrorMapper: ExceptionToErrorMapper,
) {

    private val dao: HomeModuleDao
        get() = database.dao()

    suspend fun save(module: HomeModule): ResultData<Boolean, ErrorDomain> = coRunCatching {
        dao.update(homeModuleToEntityMapper.transform(module))
        true
    }
        .mapFailure { error -> exceptionToErrorMapper.transform(error) }

    suspend fun get(moduleId: Long): ResultData<HomeModule, ErrorDomain> = coRunCatching {
        homeModuleEntityToDomainMapper.transform(dao.get(moduleId))
    }
        .mapFailure { error -> exceptionToErrorMapper.transform(error) }

    fun observe(): Flow<ResultData<List<HomeModule>, ErrorDomain>> = dao.observeAll()
        .map { entityList ->
            if (entityList.isEmpty()) initModules()
            homeModuleEntityToDomainMapper.transform(entityList)
        }
        .map { modules -> ResultData.Success(modules) }
        .catch { error -> ResultData.Failure(exceptionToErrorMapper.transform(error)) }

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
