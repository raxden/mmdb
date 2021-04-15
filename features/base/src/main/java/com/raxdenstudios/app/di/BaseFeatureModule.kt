package com.raxdenstudios.app.di

import com.raxdenstudios.app.movie.view.mapper.MovieListItemModelMapper
import com.raxdenstudios.commons.pagination.Pagination
import com.raxdenstudios.commons.pagination.model.Page
import com.raxdenstudios.commons.pagination.model.PageSize
import org.koin.dsl.module

val baseFeatureModule = module {

  factory { MovieListItemModelMapper() }

  factory {
    Pagination.Config.default.copy(
      initialPage = Page(1),
      pageSize = PageSize(20),
      prefetchDistance = 4
    )
  }
}