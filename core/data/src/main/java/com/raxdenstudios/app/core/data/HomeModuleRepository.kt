package com.raxdenstudios.app.core.data

import com.raxdenstudios.app.core.data.local.datasource.HomeModuleLocalDataSource
import com.raxdenstudios.app.core.model.ErrorDomain
import com.raxdenstudios.app.core.model.HomeModule
import com.raxdenstudios.commons.ResultData
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface HomeModuleRepository {

    fun observe(): Flow<ResultData<List<HomeModule>, ErrorDomain>>
    suspend fun fetch(moduleId: Long): ResultData<HomeModule, ErrorDomain>
    suspend fun save(module: HomeModule): ResultData<Boolean, ErrorDomain>
}

class HomeModuleRepositoryImpl @Inject constructor(
    private val homeModuleLocalDataSource: HomeModuleLocalDataSource,
) : HomeModuleRepository {

    override fun observe(): Flow<ResultData<List<HomeModule>, ErrorDomain>> =
        homeModuleLocalDataSource.observe()

    override suspend fun fetch(moduleId: Long): ResultData<HomeModule, ErrorDomain> =
        homeModuleLocalDataSource.get(moduleId)

    override suspend fun save(module: HomeModule): ResultData<Boolean, ErrorDomain> =
        homeModuleLocalDataSource.save(module)
}
