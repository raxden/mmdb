package com.raxdenstudios.app.navigator.di

import androidx.fragment.app.FragmentActivity
import com.raxdenstudios.app.home.HomeNavigator
import com.raxdenstudios.app.list.MediaListNavigator
import com.raxdenstudios.app.navigator.HomeNavigatorImpl
import com.raxdenstudios.app.navigator.MediaListNavigatorImpl
import com.raxdenstudios.app.navigator.mapper.MediaListParamsMapper
import com.raxdenstudios.app.navigator.result.LoginActivityResultContract
import com.raxdenstudios.app.navigator.result.MediaListActivityResultContract
import org.koin.dsl.module

val navigatorModule = module {

  factory { MediaListParamsMapper() }

  factory { LoginActivityResultContract() }
  factory { MediaListActivityResultContract(get()) }

  factory<HomeNavigator> { (activity: FragmentActivity) ->
    HomeNavigatorImpl(
      activity,
      get(),
      get()
    )
  }
  factory<MediaListNavigator> { (activity: FragmentActivity) -> MediaListNavigatorImpl(activity) }
}
