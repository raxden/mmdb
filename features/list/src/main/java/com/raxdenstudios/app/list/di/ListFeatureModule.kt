package com.raxdenstudios.app.list.di

import com.raxdenstudios.app.list.view.MovieListViewModel
import com.raxdenstudios.app.list.view.mapper.MovieListModelMapper
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val listFeatureModule = module {

  factory { MovieListModelMapper(get()) }

  viewModel { MovieListViewModel(get(), get()) }
}