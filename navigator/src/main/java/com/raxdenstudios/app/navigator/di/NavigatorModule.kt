package com.raxdenstudios.app.navigator.di


import com.raxdenstudios.app.home.HomeMediaListNavigator
import com.raxdenstudios.app.list.MediaListNavigator
import com.raxdenstudios.app.navigator.HomeMediaListNavigatorImpl
import com.raxdenstudios.app.navigator.MediaListNavigatorImpl
import com.raxdenstudios.app.navigator.SplashNavigatorImpl
import com.raxdenstudios.app.splash.SplashNavigator
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ActivityComponent

@Module
@InstallIn(ActivityComponent::class)
abstract class NavigatorModule {

  @Binds
  internal abstract fun splashNavigator(navigator: SplashNavigatorImpl): SplashNavigator

  @Binds
  internal abstract fun mediaListNavigator(navigator: MediaListNavigatorImpl): MediaListNavigator

  @Binds
  internal abstract fun homeNavigator(navigator: HomeMediaListNavigatorImpl): HomeMediaListNavigator
}
