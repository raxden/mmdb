package com.raxdenstudios.app.navigator

import android.app.Activity
import com.raxdenstudios.app.home.view.HomeActivity
import com.raxdenstudios.app.splash.SplashNavigator
import com.raxdenstudios.commons.ext.startActivityAndFinishCurrent
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject

@ActivityScoped
internal class SplashNavigatorImpl @Inject constructor(
  private val activity: Activity
) : SplashNavigator {

  override fun launchHome() {
    HomeActivity.createIntent(activity)
      .startActivityAndFinishCurrent(activity)
  }
}
