package com.raxdenstudios.app.navigator.di

import androidx.fragment.app.FragmentActivity
import com.raxdenstudios.app.home.HomeNavigator
import com.raxdenstudios.app.navigator.HomeNavigatorImpl
import org.koin.dsl.module

val navigatorModule = module {

  factory<HomeNavigator> { (activity: FragmentActivity) -> HomeNavigatorImpl(activity) }
}
