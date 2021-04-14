package com.raxdenstudios.app.list.di

import com.raxdenstudios.app.home.view.model.MovieListItemModel
import com.raxdenstudios.app.list.view.MovieListViewModel
import com.raxdenstudios.commons.pagination.Pagination
import com.raxdenstudios.commons.pagination.model.Page
import kotlinx.coroutines.CoroutineScope
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import timber.log.Timber

val listFeatureModule = module {

  factory { Pagination.Config.default.copy(initialPage = Page(1)) }

  factory { (coroutineScope: CoroutineScope) ->
    Pagination<MovieListItemModel>(
      coroutineScope = coroutineScope,
      logger = { message -> Timber.d(message) },
      config = get()
    )
  }

  viewModel { MovieListViewModel(get(), get()) }
}