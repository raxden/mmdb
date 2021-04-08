package com.raxdenstudios.commons.ext

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import androidx.core.content.ContextCompat

inline fun <reified T : Activity> Context.intentFor(params: Parcelable? = null) =
  Intent(this, T::class.java).apply {
    if (params != null) putExtra("params", params)
  }

fun Intent.startActivity(activity: Activity, options: Bundle? = null) {
  ContextCompat.startActivity(activity, this, options)
}

fun Intent.startActivityAndFinishCurrent(activity: Activity, options: Bundle? = null) {
  ContextCompat.startActivity(activity, this, options)
  activity.finish()
}

fun Intent.startActivityAndFinishAll(activity: Activity, options: Bundle? = null) {
  flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
  ContextCompat.startActivity(activity, this, options)
}

