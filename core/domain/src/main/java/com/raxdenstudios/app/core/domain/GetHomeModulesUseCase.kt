package com.raxdenstudios.app.core.domain

import com.raxdenstudios.app.core.data.HomeModuleRepository
import com.raxdenstudios.app.core.data.MediaRepository
import com.raxdenstudios.app.core.model.HomeModule
import com.raxdenstudios.app.core.model.Media
import com.raxdenstudios.app.core.model.MediaCategory
import com.raxdenstudios.app.core.model.MediaFilter
import com.raxdenstudios.app.core.model.MediaType
import com.raxdenstudios.commons.DispatcherProvider
import com.raxdenstudios.commons.ext.getValueOrDefault
import com.raxdenstudios.commons.ext.map
import com.raxdenstudios.commons.pagination.model.Page
import com.raxdenstudios.commons.pagination.model.PageSize
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.withContext
import javax.inject.Inject

class GetHomeModulesUseCase @Inject constructor(
    private val dispatcher: DispatcherProvider,
    private val homeModuleRepository: HomeModuleRepository,
    private val mediasRepository: MediaRepository,
) {

    operator fun invoke(): Flow<Map<HomeModule, List<Media>>> {
        val observeModules = homeModuleRepository.observe()
        val observeWatchlist = mediasRepository.observeWatchlist()

        return observeModules.combine(observeWatchlist) { modules, watchListResult ->
            withContext(dispatcher.io) {
                modules.associateWith { module ->
                    val deferred = async {
                        fetchMedias(module, watchListResult.getValueOrDefault(emptyList()))
                    }
                    deferred.await()
                }
            }
        }
    }

    private suspend fun fetchMedias(
        module: HomeModule,
        watchList: List<Media>
    ): List<Media> = when (module) {
        is HomeModule.Watchlist ->
            watchList
        is HomeModule.NowPlaying ->
            fetchMedias(module.mediaType, MediaCategory.NowPlaying)
        is HomeModule.Popular ->
            fetchMedias(module.mediaType, MediaCategory.Popular)
        is HomeModule.TopRated ->
            fetchMedias(module.mediaType, MediaCategory.TopRated)
        is HomeModule.Upcoming ->
            fetchMedias(module.mediaType, MediaCategory.Upcoming)
    }

    private suspend fun fetchMedias(
        mediaType: MediaType,
        mediaCategory: MediaCategory,
    ): List<Media> = mediasRepository.medias(
        mediaFilter = MediaFilter(
            mediaType = mediaType,
            mediaCategory = mediaCategory,
        ),
        page = firstPage,
        pageSize = pageSize
    )
        .map { pageList -> pageList.items }
        .getValueOrDefault(emptyList())

    companion object {

        private val firstPage = Page(1)
        private val pageSize = PageSize(20)
    }
}
