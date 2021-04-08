package com.raxdenstudios.app.base

import android.app.Application
import android.content.Context
import com.google.android.play.core.splitcompat.SplitCompat

abstract class BaseApplication: Application() {

  override fun attachBaseContext(base: Context) {
    super.attachBaseContext(base)

    // Enable dynamic feature module
    SplitCompat.install(this)
  }
}
