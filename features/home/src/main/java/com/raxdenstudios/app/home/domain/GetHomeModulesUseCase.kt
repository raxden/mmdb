package com.raxdenstudios.app.home.domain

import com.raxdenstudios.app.home.data.repository.HomeModuleRepository
import com.raxdenstudios.app.home.domain.model.HomeModule
import com.raxdenstudios.app.media.data.repository.MediaRepository
import com.raxdenstudios.app.media.domain.model.Media
import com.raxdenstudios.app.media.domain.model.MediaFilter
import com.raxdenstudios.commons.DispatcherFacade
import com.raxdenstudios.commons.getValueOrDefault
import com.raxdenstudios.commons.map
import com.raxdenstudios.commons.pagination.model.Page
import com.raxdenstudios.commons.pagination.model.PageSize
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

internal class GetHomeModulesUseCase(
  private val dispatcherFacade: DispatcherFacade,
  private val homeModuleRepository: HomeModuleRepository,
  private val mediasRepository: MediaRepository,
) {

  companion object {
    private val firstPage = Page(1)
    private val pageSize = PageSize(20)
  }

  fun execute(): Flow<List<HomeModule>> =
    homeModuleRepository.observeModules()
      .map { modules ->
        withContext(dispatcherFacade.io()) {
          modules
            .map { module -> async { fetchMediasFromModule(module) } }
            .map { deferred -> deferred.await() }
        }
      }

  private suspend fun fetchMediasFromModule(module: HomeModule) =
    when (module) {
      is HomeModule.NowPlaying ->
        module.copy(medias = fetchMedias(MediaFilter.NowPlaying) as List<Media.Movie>)
      is HomeModule.Popular ->
        module.copy(medias = fetchMedias(MediaFilter.Popular(module.mediaType)))
      is HomeModule.TopRated ->
        module.copy(medias = fetchMedias(MediaFilter.TopRated(module.mediaType)))
      is HomeModule.Upcoming ->
        module.copy(medias = fetchMedias(MediaFilter.Upcoming) as List<Media.Movie>)
      is HomeModule.WatchList ->
        module.copy(medias = fetchMedias(MediaFilter.WatchList(module.mediaType)))
    }

  private suspend fun fetchMedias(mediaFilter: MediaFilter) =
    mediasRepository.medias(mediaFilter, firstPage, pageSize)
      .map { pageList -> pageList.items }
      .getValueOrDefault(emptyList())
}
