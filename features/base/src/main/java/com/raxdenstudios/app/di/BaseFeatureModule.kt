package com.raxdenstudios.app.di

import com.raxdenstudios.app.media.view.mapper.MediaListItemModelMapper
import com.raxdenstudios.app.media.view.model.MediaListItemModel
import com.raxdenstudios.commons.pagination.Pagination
import com.raxdenstudios.commons.pagination.model.Page
import com.raxdenstudios.commons.pagination.model.PageSize
import kotlinx.coroutines.CoroutineScope
import org.koin.dsl.module
import timber.log.Timber

val baseFeatureModule = module {

  factory { MediaListItemModelMapper() }

  factory {
    Pagination.Config.default.copy(
      initialPage = Page(1),
      pageSize = PageSize(20),
      prefetchDistance = 4
    )
  }

  factory { (coroutineScope: CoroutineScope) ->
    Pagination<MediaListItemModel>(
      config = get(),
      logger = { message -> Timber.tag("Pagination").d(message) },
      coroutineScope = coroutineScope
    )
  }
}