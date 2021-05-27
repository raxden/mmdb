package com.raxdenstudios.app.list.di

import com.raxdenstudios.app.list.view.mapper.GetMediasUseCaseParamsMapper
import com.raxdenstudios.app.list.view.viewmodel.MediaListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val listFeatureModule = module {

  factory { GetMediasUseCaseParamsMapper() }

  viewModel { MediaListViewModel(get(), get(), get(), get(), get()) }
}
