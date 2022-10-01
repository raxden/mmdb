package com.raxdenstudios.app.util

import android.app.Application
import com.jakewharton.threetenabp.AndroidThreeTen

object ThreeTenSDK {

    fun init(application: Application) {
        // Initialize the timezone information
        AndroidThreeTen.init(application)
    }
}
