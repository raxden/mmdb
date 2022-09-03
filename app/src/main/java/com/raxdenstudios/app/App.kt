package com.raxdenstudios.app

import com.raxdenstudios.app.base.BaseApplication
import com.raxdenstudios.app.sdk.GanderSDK
import com.raxdenstudios.app.sdk.ThreeTenSDK
import com.raxdenstudios.app.sdk.TimberSDK
import dagger.hilt.android.HiltAndroidApp

@HiltAndroidApp
internal class App : BaseApplication() {

    override fun onCreate() {
        super.onCreate()

        GanderSDK.init()
        TimberSDK.init()
        ThreeTenSDK.init(this)
    }
}
