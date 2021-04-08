package com.raxdenstudios.app

import com.raxdenstudios.app.base.BaseApplication
import com.raxdenstudios.app.sdk.GanderSDK
import com.raxdenstudios.app.sdk.KoinSDK
import com.raxdenstudios.app.sdk.ThreeTenSDK
import com.raxdenstudios.app.sdk.TimberSDK

internal class App : BaseApplication() {

  override fun onCreate() {
    KoinSDK.init(this)

    super.onCreate()

    GanderSDK.init()
    TimberSDK.init()
    ThreeTenSDK.init(this)
  }
}
