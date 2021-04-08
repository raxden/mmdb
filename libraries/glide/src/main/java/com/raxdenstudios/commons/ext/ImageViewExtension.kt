package com.raxdenstudios.commons.ext

import android.graphics.drawable.Drawable
import android.widget.ImageView
import com.raxdenstudios.commons.GlideUtil

enum class ScaleType {
  CIRCLE_CROP, CENTER_CROP, FIT_CENTER
}

fun ImageView.loadImage(
  source: String,
  scaleType: ScaleType = ScaleType.CENTER_CROP
) {
  if (getTag(id) == null || getTag(id) != (source)) {
    setImageBitmap(null)
    setTag(id, source)
    GlideUtil.loadImage(source, this, scaleType = scaleType)
  }
}

fun ImageView.loadImage(
  source: Drawable,
  scaleType: ScaleType = ScaleType.CENTER_CROP
) {
  if (getTag(id) == null || getTag(id) != (source)) {
    setImageBitmap(null)
    setTag(id, source)
    GlideUtil.loadImage(source, this, scaleType = scaleType)
  }
}
