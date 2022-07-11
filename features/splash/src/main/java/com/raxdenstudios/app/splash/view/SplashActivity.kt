package com.raxdenstudios.app.splash.view

import android.os.Bundle
import com.raxdenstudios.app.base.BaseActivity
import com.raxdenstudios.app.splash.SplashNavigator
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
internal class SplashActivity : BaseActivity() {

  @Inject
  lateinit var navigator: SplashNavigator

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    navigator.launchHome()
  }
}
