package com.raxdenstudios.commons.ext

import android.graphics.drawable.Drawable
import androidx.core.graphics.BlendModeColorFilterCompat
import androidx.core.graphics.BlendModeCompat

fun Drawable.setColorFilter(
  color: Int,
  blendModeCompat: BlendModeCompat = BlendModeCompat.SRC_ATOP
) {
  colorFilter = BlendModeColorFilterCompat.createBlendModeColorFilterCompat(color, blendModeCompat)
}
