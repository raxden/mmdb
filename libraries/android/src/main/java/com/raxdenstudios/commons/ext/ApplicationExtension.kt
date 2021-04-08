package com.raxdenstudios.commons.ext

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.raxdenstudios.commons.util.SDK

fun Application.initCompatVector() {
  if (SDK.hasLollipop()) AppCompatDelegate.setCompatVectorFromResourcesEnabled(true)
}
