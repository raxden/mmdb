package com.raxdenstudios.commons.provider

import android.content.Context
import androidx.annotation.StringRes

class StringProvider(
  private val context: Context
) {

  fun getString(@StringRes resId: Int) = context.getString(resId)

  fun getString(@StringRes resId: Int, vararg formatArgs: Any?) =
    context.getString(resId, *formatArgs)
}
