package com.raxdenstudios.app.list.di

import com.raxdenstudios.app.list.view.viewmodel.MovieListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val listFeatureModule = module {

  viewModel { MovieListViewModel(get(), get(), get(), get(), get()) }
}