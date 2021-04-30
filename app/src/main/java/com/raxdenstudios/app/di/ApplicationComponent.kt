package com.raxdenstudios.app.di

import com.raxdenstudios.app.account.di.accountDataModule
import com.raxdenstudios.app.base.di.baseModule
import com.raxdenstudios.app.error.di.errorFeatureModule
import com.raxdenstudios.app.home.di.homeFeatureModule
import com.raxdenstudios.app.list.di.listFeatureModule
import com.raxdenstudios.app.login.di.loginFeatureModule
import com.raxdenstudios.app.media.di.mediaDataModule
import com.raxdenstudios.app.navigator.di.navigatorModule
import com.raxdenstudios.app.network.di.networkLibraryModule
import com.raxdenstudios.app.splash.di.splashFeatureModule
import com.raxdenstudios.app.tmdb.di.tmdbFeatureModule
import org.koin.core.module.Module

val appModules: List<Module> = listOf(
  baseModule,
  navigatorModule,
)

val dataModules: List<Module> = listOf(
  mediaDataModule,
  accountDataModule,
)

val libraryModules: List<Module> = listOf(
  networkLibraryModule,
)

val featureModules: List<Module> = listOf(
  baseFeatureModule,
  splashFeatureModule,
  homeFeatureModule,
  loginFeatureModule,
  tmdbFeatureModule,
  errorFeatureModule,
  listFeatureModule,
)

val appComponent: List<Module> = mutableListOf<Module>().apply {
  addAll(appModules)
  addAll(dataModules)
  addAll(libraryModules)
  addAll(featureModules)
}
