package com.raxdenstudios.app.home.di

import com.raxdenstudios.app.home.data.local.HomeModuleDatabase
import com.raxdenstudios.app.home.data.local.datasource.HomeModuleLocalDataSource
import com.raxdenstudios.app.home.data.local.mapper.HomeModuleEntityToDomainMapper
import com.raxdenstudios.app.home.data.repository.HomeModuleRepository
import com.raxdenstudios.app.home.domain.GetHomeModulesUseCase
import com.raxdenstudios.app.home.view.mapper.CarouselMovieListModelMapper
import com.raxdenstudios.app.home.view.mapper.GetMoviesUseCaseParamsMapper
import com.raxdenstudios.app.home.view.mapper.HomeModuleModelMapper
import com.raxdenstudios.app.home.view.viewmodel.HomeViewModel
import com.raxdenstudios.app.movie.view.model.MovieListItemModel
import com.raxdenstudios.commons.pagination.Pagination
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

  factory { GetHomeModulesUseCase(get()) }

  factory { HomeModuleModelMapper(get()) }
  factory { GetMoviesUseCaseParamsMapper() }
  factory { CarouselMovieListModelMapper(get(), get()) }

  factory { (coroutineScope: CoroutineScope) ->
    Pagination<MovieListItemModel>(
      config = get(),
      logger = { message -> Timber.tag("Pagination").d(message) },
      coroutineScope = coroutineScope
    )
  }

  viewModel { HomeViewModel(get(), get(), get(), get(), get(), get(), get(), get()) }
}
