package com.raxdenstudios.app.core.domain

import com.raxdenstudios.app.core.data.HomeModuleRepository
import com.raxdenstudios.app.core.model.HomeModule
import com.raxdenstudios.app.core.model.MediaType
import com.raxdenstudios.commons.ResultData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.update

class FakeHomeModuleRepository : HomeModuleRepository {

    private val modules = MutableStateFlow(
        listOf<HomeModule>(
            HomeModule.Carousel.popular(0, 0, MediaType.Movie),
            HomeModule.Carousel.nowPlaying(0, 0),
        )
    )

    override fun observe(): Flow<ResultData<List<HomeModule>>> = modules.map { ResultData.Success(it) }

    override suspend fun fetch(moduleId: Long): ResultData<HomeModule> =
        ResultData.Success(HomeModule.Carousel.watchlist(0, 0, MediaType.Movie))

    override suspend fun save(module: HomeModule): ResultData<Boolean> {
        modules.update { value ->
            value.toMutableList().also { list ->
                list.add(module)
            }
        }
        return ResultData.Success(true)
    }
}
