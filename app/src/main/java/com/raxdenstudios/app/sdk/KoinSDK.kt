package com.raxdenstudios.app.sdk

import android.app.Application
import com.raxdenstudios.app.BuildConfig
import com.raxdenstudios.app.di.appComponent
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

object KoinSDK {

  fun init(application: Application) {
    startKoin {
      // use AndroidLogger as Koin Logger - default Level.INFO
      if (BuildConfig.DEBUG) androidLogger()

      // use the Android context given there
      androidContext(application)

      // load properties from assets/koin.properties file
      androidFileProperties()

      // module list
//      modules(appComponent)
      // TODO Await fix for Koin and replace the explicit invocations
      //  of loadModules() and createRootScope() with a single call to modules()
      //  (https://github.com/InsertKoinIO/koin/issues/847)
      koin.loadModules(appComponent)
      koin.createRootScope()
    }
  }
}
