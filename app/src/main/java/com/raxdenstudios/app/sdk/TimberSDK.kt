package com.raxdenstudios.app.sdk

import com.raxdenstudios.app.BuildConfig
import com.raxdenstudios.app.util.FirebaseCrashlyticsTree
import timber.log.Timber

object TimberSDK {

  fun init() {
    Timber.plant(
      when (BuildConfig.DEBUG) {
        true -> Timber.DebugTree()
        else -> FirebaseCrashlyticsTree()
      }
    )
  }
}
