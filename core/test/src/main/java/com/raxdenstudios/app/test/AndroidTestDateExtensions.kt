package com.raxdenstudios.app.test

import androidx.test.platform.app.InstrumentationRegistry
import com.jakewharton.threetenabp.AndroidThreeTen

fun initAndroidThreeTen() {
    val appContext = InstrumentationRegistry.getInstrumentation().targetContext.applicationContext
    AndroidThreeTen.init(appContext)
}
