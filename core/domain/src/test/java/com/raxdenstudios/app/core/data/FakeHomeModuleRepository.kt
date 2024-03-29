package com.raxdenstudios.app.core.data

import com.raxdenstudios.app.core.model.ErrorDomain
import com.raxdenstudios.app.core.model.HomeModule
import com.raxdenstudios.app.core.model.MediaType
import com.raxdenstudios.commons.core.ResultData
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

    override fun observe(): Flow<ResultData<List<HomeModule>, ErrorDomain>> =
        modules.map { ResultData.Success(it) }

    override suspend fun fetch(moduleId: Long): ResultData<HomeModule, ErrorDomain> =
        ResultData.Success(HomeModule.Carousel.watchlist(0, 0, MediaType.Movie))

    override suspend fun save(module: HomeModule): ResultData<Boolean, ErrorDomain> {
        modules.update { value ->
            value.toMutableList().also { list ->
                list.add(module)
            }
        }
        return ResultData.Success(true)
    }
}
