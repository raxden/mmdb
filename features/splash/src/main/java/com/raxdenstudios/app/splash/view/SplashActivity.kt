package com.raxdenstudios.app.splash.view

import android.os.Bundle
import com.raxdenstudios.app.base.BaseActivity
import com.raxdenstudios.app.splash.SplashNavigator
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

internal class SplashActivity : BaseActivity() {

  private val navigator: SplashNavigator by inject { parametersOf(this) }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    navigator.launchHome()
  }
}