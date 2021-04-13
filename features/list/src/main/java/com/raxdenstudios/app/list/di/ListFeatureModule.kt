package com.raxdenstudios.app.list.di

import com.raxdenstudios.app.list.view.MovieListViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val listFeatureModule = module {

  viewModel { MovieListViewModel() }
}