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

fun Intent.startActivity(context: Context, options: Bundle? = null) {
  ContextCompat.startActivity(context, this, options)
}

fun Intent.startActivityAndFinishCurrent(context: Context, options: Bundle? = null) {
  check(context is Activity) { "context must be an instance of Activity" }
  ContextCompat.startActivity(context, this, options)
  context.finish()
}

fun Intent.startActivityAndFinishAll(context: Context, options: Bundle? = null) {
  flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
  ContextCompat.startActivity(context, this, options)
}

