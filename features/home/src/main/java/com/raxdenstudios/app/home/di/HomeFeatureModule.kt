package com.raxdenstudios.app.home.di

import com.raxdenstudios.app.home.data.local.HomeModuleDatabase
import com.raxdenstudios.app.home.data.local.datasource.HomeModuleLocalDataSource
import com.raxdenstudios.app.home.data.local.mapper.HomeModuleEntityToDomainMapper
import com.raxdenstudios.app.home.data.repository.HomeModuleRepository
import com.raxdenstudios.app.home.domain.GetHomeModulesUseCase
import com.raxdenstudios.app.home.view.mapper.CarouselMediasModelMapper
import com.raxdenstudios.app.home.view.mapper.GetMediasUseCaseParamsMapper
import com.raxdenstudios.app.home.view.mapper.HomeModelMapper
import com.raxdenstudios.app.home.view.mapper.HomeModuleModelMapper
import com.raxdenstudios.app.home.view.viewmodel.HomeMediaListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val homeFeatureModule = module {

  single { HomeModuleDatabase.getInstance(get()) }
  factory { get<HomeModuleDatabase>().dao() }

  factory { HomeModuleLocalDataSource(get(), get()) }
  factory { HomeModuleEntityToDomainMapper() }

  factory { HomeModuleRepository(get()) }

  factory { GetHomeModulesUseCase(get(), get(), get()) }

  factory { HomeModelMapper(get()) }
  factory { HomeModuleModelMapper(get()) }
  factory { GetMediasUseCaseParamsMapper() }
  factory { CarouselMediasModelMapper(get(), get()) }

  viewModel { HomeMediaListViewModel(get(), get(), get(), get(), get(), get(), get()) }
}
