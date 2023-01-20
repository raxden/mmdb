package com.raxdenstudios.app.core.domain

import com.raxdenstudios.app.core.data.HomeModuleRepository
import com.raxdenstudios.app.core.model.HomeModule
import com.raxdenstudios.commons.ResultData
import com.raxdenstudios.commons.ext.replaceItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update

class FakeHomeModuleRepository : HomeModuleRepository {

    private val modules = MutableStateFlow(
        listOf(
            HomeModule.Popular.empty,
            HomeModule.NowPlaying.empty,
        )
    )

    override fun observe(): Flow<List<HomeModule>> = modules

    override suspend fun get(moduleId: Long): ResultData<HomeModule> =
        ResultData.Success(HomeModule.Watchlist.empty)

    override suspend fun save(module: HomeModule): ResultData<Boolean> {
        modules.update {
            modules.value.toMutableList().also { list ->
                list.add(module)
            }
        }
        return ResultData.Success(true)
    }
}
