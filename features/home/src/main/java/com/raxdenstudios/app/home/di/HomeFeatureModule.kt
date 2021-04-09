package com.raxdenstudios.app.home.di

import com.raxdenstudios.app.home.data.local.HomeModuleDatabase
import com.raxdenstudios.app.home.data.local.datasource.HomeModuleLocalDataSource
import com.raxdenstudios.app.home.data.local.mapper.HomeModuleEntityToDomainMapper
import com.raxdenstudios.app.home.data.repository.HomeModuleRepository
import com.raxdenstudios.app.home.domain.GetHomeModulesUseCase
import com.raxdenstudios.app.home.domain.GetMoviesUseCase
import com.raxdenstudios.app.home.view.mapper.CarouselMovieListModelMapper
import com.raxdenstudios.app.home.view.mapper.GetMoviesUseCaseParamsMapper
import com.raxdenstudios.app.home.view.mapper.HomeModuleModelMapper
import com.raxdenstudios.app.home.view.mapper.MovieListItemModelMapper
import com.raxdenstudios.app.home.view.model.MovieListItemModel
import com.raxdenstudios.app.home.view.viewmodel.HomeViewModel
import com.raxdenstudios.commons.pagination.Pagination
import com.raxdenstudios.commons.pagination.model.Page
import com.raxdenstudios.commons.pagination.model.PageSize
import kotlinx.coroutines.CoroutineScope
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import timber.log.Timber

val homeFeatureModule = module {

  single { HomeModuleDatabase.getInstance(get()) }
  factory { get<HomeModuleDatabase>().dao() }

  factory { HomeModuleLocalDataSource(get(), get()) }
  factory { HomeModuleEntityToDomainMapper() }

  factory { HomeModuleRepository(get()) }

  factory { GetMoviesUseCase(get()) }
  factory { GetHomeModulesUseCase(get()) }

  factory { MovieListItemModelMapper() }
  factory { HomeModuleModelMapper(get()) }
  factory { GetMoviesUseCaseParamsMapper() }
  factory { CarouselMovieListModelMapper(get(), get()) }

  factory {
    Pagination.Config.default.copy(
      initialPage = Page(1),
      pageSize = PageSize(20),
      prefetchDistance = 4
    )
  }
  factory { (coroutineScope: CoroutineScope) ->
    Pagination<MovieListItemModel>(
      config = get(),
      logger = { message -> Timber.tag("Pagination").d(message) },
      coroutineScope = coroutineScope
    )
  }

  viewModel { HomeViewModel(get(), get(), get(), get(), get(), get(), get()) }
}
