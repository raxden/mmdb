package com.raxdenstudios.app.test

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen

open class AppTestApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        AndroidThreeTen.init(this)
    }
}
