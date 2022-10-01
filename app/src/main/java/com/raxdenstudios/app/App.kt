package com.raxdenstudios.app

import android.app.Application
import android.content.Context
import coil.ImageLoader
import coil.ImageLoaderFactory
import com.google.android.play.core.splitcompat.SplitCompat
import com.raxdenstudios.app.util.GanderSDK
import com.raxdenstudios.app.util.ThreeTenSDK
import com.raxdenstudios.app.util.TimberSDK
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class App : Application(),
    ImageLoaderFactory {

    @Inject
    lateinit var imageLoader: ImageLoader

    override fun attachBaseContext(base: Context) {
        super.attachBaseContext(base)

        // Enable dynamic feature module
        SplitCompat.install(this)
    }

    override fun newImageLoader(): ImageLoader = imageLoader

    override fun onCreate() {
        super.onCreate()

        GanderSDK.init()
        TimberSDK.init()
        ThreeTenSDK.init(this)
    }
}
