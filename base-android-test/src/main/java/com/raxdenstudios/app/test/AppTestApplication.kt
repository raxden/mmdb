package com.raxdenstudios.app.test

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen

internal class AppTestApplication : Application() {

  override fun onCreate() {
    super.onCreate()

    AndroidThreeTen.init(this)
  }
}
