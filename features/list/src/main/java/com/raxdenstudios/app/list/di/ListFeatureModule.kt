package com.raxdenstudios.app.list.di

import com.raxdenstudios.app.list.view.viewmodel.MediaListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val listFeatureModule = module {

  viewModel { MediaListViewModel(get(), get(), get(), get(), get(), get()) }
}