package com.raxdenstudios.app.navigator

import androidx.fragment.app.FragmentActivity
import com.raxdenstudios.app.home.view.HomeV2Activity
import com.raxdenstudios.app.splash.SplashNavigator
import com.raxdenstudios.commons.ext.startActivityAndFinishCurrent

internal class SplashNavigatorImpl(
  private val activity: FragmentActivity
) : SplashNavigator {

  override fun launchHome() {
    HomeV2Activity.createIntent(activity)
      .startActivityAndFinishCurrent(activity)
  }
}