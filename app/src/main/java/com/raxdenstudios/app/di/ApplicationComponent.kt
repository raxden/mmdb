package com.raxdenstudios.app.di

import com.raxdenstudios.app.account.di.accountDataModule
import com.raxdenstudios.app.base.di.baseModule
import com.raxdenstudios.app.error.di.errorFeatureModule
import com.raxdenstudios.app.home.di.homeFeatureModule
import com.raxdenstudios.app.list.di.listFeatureModule
import com.raxdenstudios.app.login.di.loginFeatureModule
import com.raxdenstudios.app.movie.di.movieDataModule
import com.raxdenstudios.app.navigator.di.navigatorModule
import com.raxdenstudios.app.network.di.networkDataModule
import com.raxdenstudios.app.tmdb.di.tmdbFeatureModule
import org.koin.core.module.Module

val appModules: List<Module> = listOf(
  baseModule,
  navigatorModule,
)

val dataModules: List<Module> = listOf(
  networkDataModule,
  movieDataModule,
  accountDataModule,
)

val featureModules: List<Module> = listOf(
  baseFeatureModule,
  homeFeatureModule,
  loginFeatureModule,
  tmdbFeatureModule,
  errorFeatureModule,
  listFeatureModule,
)

val appComponent: List<Module> = mutableListOf<Module>().apply {
  addAll(appModules)
  addAll(dataModules)
  addAll(featureModules)
}
