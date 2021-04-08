package com.raxdenstudios.commons.ext

import android.content.Context
import android.os.Environment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.io.File
import java.text.SimpleDateFormat
import java.util.*

fun Context.createTemporalImageFile(): File {
  val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
  val imageFileName = "JPEG_" + timeStamp + "_"
  val storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES)
  return File.createTempFile(
    imageFileName, /* prefix */
    ".jpg", /* suffix */
    storageDir      /* directory */
  )
}

fun Context.hasVirtualNavigationBar(): Boolean {
  val id = resources.getIdentifier("config_showNavigationBar", "bool", "android")
  return id > 0 && resources.getBoolean(id)
}

fun Context.getVirtualNavigationBarHeight(): Int {
  val id = resources.getIdentifier("navigation_bar_height", "dimen", "android")
  return if (id > 0) {
    resources.getDimensionPixelSize(id)
  } else 0
}

fun Context.showSimpleDialog(
  title: Int,
  message: Int,
  positiveButton: Int = android.R.string.ok
) {
  showSimpleDialog(getString(title), getString(message), getString(positiveButton))
}

fun Context.showSimpleDialog(
  title: String,
  message: String,
  positiveButton: String = getString(android.R.string.ok)
) {
  MaterialAlertDialogBuilder(this)
    .setTitle(title)
    .setMessage(message)
    .setPositiveButton(positiveButton) { dialog, _ -> dialog.dismiss() }
    .create()
    .show()
}

fun Context.showDialog(
  title: Int,
  message: Int,
  positiveButton: Int = android.R.string.ok,
  onPositiveClickButton: () -> Unit = {},
  negativeButton: Int = android.R.string.cancel,
  onNegativeClickButton: () -> Unit = {}
) {
  showDialog(
    getString(title),
    getString(message),
    getString(positiveButton),
    onPositiveClickButton,
    getString(negativeButton),
    onNegativeClickButton
  )
}

fun Context.showDialog(
  title: String,
  message: String,
  positiveButton: String = getString(android.R.string.ok),
  onPositiveClickButton: () -> Unit = {},
  negativeButton: String = getString(android.R.string.cancel),
  onNegativeClickButton: () -> Unit = {}
) {
  MaterialAlertDialogBuilder(this)
    .setTitle(title)
    .setMessage(message)
    .setPositiveButton(positiveButton) { dialog, _ ->
      onPositiveClickButton()
      dialog.dismiss()
    }
    .setNegativeButton(negativeButton) { dialog, _ ->
      onNegativeClickButton()
      dialog.dismiss()
    }
    .create()
    .show()
}
