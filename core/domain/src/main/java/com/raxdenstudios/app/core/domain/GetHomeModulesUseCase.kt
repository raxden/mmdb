package com.raxdenstudios.app.core.domain

import com.raxdenstudios.app.core.data.HomeModuleRepository
import com.raxdenstudios.app.core.data.MediaRepository
import com.raxdenstudios.app.core.model.ErrorDomain
import com.raxdenstudios.app.core.model.HomeModule
import com.raxdenstudios.app.core.model.Media
import com.raxdenstudios.app.core.model.MediaCategory
import com.raxdenstudios.app.core.model.MediaFilter
import com.raxdenstudios.app.core.model.MediaType
import com.raxdenstudios.commons.DispatcherProvider
import com.raxdenstudios.commons.ResultData
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
    private val mediaRepository: MediaRepository,
) {

    operator fun invoke(): Flow<ResultData<List<HomeModule>, ErrorDomain>> {
        val observeModules = homeModuleRepository.observe()
        val observeWatchlist = mediaRepository.observeWatchlist()

        return observeModules.combine(observeWatchlist) { modulesResult, watchListResult ->
            val modules = modulesResult.getValueOrDefault(emptyList())
            val mediaWatchlist = watchListResult.getValueOrDefault(emptyList())
            val modulesWithMedias = withContext(dispatcher.io) {
                modules.map { module ->
                    val deferred = async {
                        when (module) {
                            is HomeModule.Carousel -> module.fetchMedias(mediaWatchlist)
                            is HomeModule.OtherModule -> error("Not implemented")
                        }
                    }
                    deferred.await()
                }
            }
            ResultData.Success(modulesWithMedias)
        }
    }

    private suspend fun HomeModule.Carousel.fetchMedias(
        mediaWatchlist: List<Media>,
    ): HomeModule.Carousel = when (mediaCategory) {
        MediaCategory.Watchlist -> copy(medias = mediaWatchlist)
        else -> copy(medias = fetchMedias(mediaType, mediaCategory))
    }

    private suspend fun fetchMedias(
        mediaType: MediaType,
        mediaCategory: MediaCategory,
    ): List<Media> = mediaRepository.medias(
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
