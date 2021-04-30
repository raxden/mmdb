package com.raxdenstudios.app.sdk

import android.app.Application
import com.raxdenstudios.app.BuildConfig
import com.raxdenstudios.app.di.appComponent
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

object KoinSDK {

  fun init(application: Application) {
    startKoin {
      // use AndroidLogger as Koin Logger - default Level.INFO
      if (BuildConfig.DEBUG) androidLogger()
      // use the Android context given there
      androidContext(application)
      // module list
      modules(appComponent)
    }
  }
}
