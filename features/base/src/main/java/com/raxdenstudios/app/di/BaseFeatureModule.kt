package com.raxdenstudios.app.di

import com.raxdenstudios.app.home.view.mapper.MovieListItemModelMapper
import org.koin.dsl.module

val baseFeatureModule = module {

  factory { MovieListItemModelMapper() }
}