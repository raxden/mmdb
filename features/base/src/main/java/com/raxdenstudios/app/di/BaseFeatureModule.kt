package com.raxdenstudios.app.di

import com.raxdenstudios.app.movie.view.mapper.MovieListItemModelMapper
import org.koin.dsl.module

val baseFeatureModule = module {

  factory { MovieListItemModelMapper() }
}