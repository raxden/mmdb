package com.raxdenstudios.commons.ext

import android.graphics.Bitmap
import android.graphics.Matrix
import android.util.Base64
import java.io.ByteArrayOutputStream

fun Bitmap.toBase64(maxSize: Int, quality: Int): String {
  val byteArrayOutputStream = ByteArrayOutputStream()
  resize(this, maxSize).compress(Bitmap.CompressFormat.JPEG, quality, byteArrayOutputStream)
  return Base64.encodeToString(byteArrayOutputStream.toByteArray(), Base64.DEFAULT)
}

fun Bitmap.rotate(rotation: Int): Bitmap {
  if (rotation != 0) {
    val matrix = Matrix()
    matrix.postRotate(rotation.toFloat())
    return Bitmap.createBitmap(this, 0, 0, this.width, this.height, matrix, true)
  }
  return this
}

fun Bitmap.resize(bitmap: Bitmap, maxSize: Int): Bitmap {
  var width = bitmap.width.toFloat()
  var height = bitmap.height.toFloat()
  val aspectRatio: Float
  if (bitmap.width > bitmap.height) {
    aspectRatio = width / height
    if (bitmap.width > maxSize) width = maxSize.toFloat()
    height = width / aspectRatio
  } else {
    aspectRatio = height / width
    if (bitmap.height > maxSize) height = maxSize.toFloat()
    width = height / aspectRatio
  }
  return Bitmap.createScaledBitmap(bitmap, width.toInt(), height.toInt(), true)
}
