package com.raxdenstudios.commons.util

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.os.Build

object SDK {

  val isEmulator: Boolean
    get() = (Build.FINGERPRINT.startsWith("generic")
        || Build.FINGERPRINT.startsWith("unknown")
        || Build.MODEL.contains("google_sdk")
        || Build.MODEL.contains("Emulator")
        || Build.MODEL.contains("Android SDK built for x86")
        || Build.MANUFACTURER.contains("Genymotion")
        || Build.BRAND.startsWith("generic") && Build.DEVICE.startsWith("generic")
        || "google_sdk" == Build.PRODUCT)

  fun hasVirtualNavigationBar(context: Context): Boolean {
    val id = context.resources.getIdentifier("config_showNavigationBar", "bool", "android")
    return (id > 0 && context.resources.getBoolean(id)) || isEmulator
  }

  fun getVirtualNavigationBarHeight(context: Context): Int {
    if (!hasVirtualNavigationBar(context)) return 0
    val resourceId = context.resources.getIdentifier("navigation_bar_height", "dimen", "android")
    return if (resourceId > 0) {
      context.resources.getDimensionPixelSize(resourceId)
    } else 0
  }

  /**
   * Only works when is called in onCreate
   */
  fun getStatusBarHeight(context: Context): Int {
    val resourceId: Int = context.resources.getIdentifier("status_bar_height", "dimen", "android")
    return if (resourceId > 0) {
      context.resources.getDimensionPixelSize(resourceId)
    } else 0
  }

  fun getPackageName(context: Context): String {
    var packageName = ""
    try {
      val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
      packageName = pInfo.packageName
    } catch (e: PackageManager.NameNotFoundException) {
    }

    return packageName
  }

  fun getVersionName(context: Context): String {
    var versionName = ""
    try {
      val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
      versionName = pInfo.versionName
    } catch (e: Exception) {
    }
    return versionName
  }

  fun getVersionCode(context: Context): Long {
    var versionCode = 0L
    try {
      val pInfo = context.packageManager.getPackageInfo(context.packageName, 0)
      versionCode = getVersionCode(pInfo)
    } catch (e: Exception) {
    }
    return versionCode
  }

  @Suppress("DEPRECATION")
  private fun getVersionCode(pInfo: PackageInfo) = if (hasPie()) {
    pInfo.longVersionCode
  } else {
    pInfo.versionCode.toLong()
  }

  /**
   * Checks if the device has Lolllipop or higher version.
   * @return `true` if device is a tablet.
   */
  fun hasLollipop(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP

  /**
   * Checks if the device has Marshmallow or higher version.
   * @return `true` if device is a tablet.
   */
  fun hasMarshmallow(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M

  /**
   * Checks if the device has Marshmallow or higher version.
   * @return `true` if device is a tablet.
   */
  fun hasNougat(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.N

  fun hasPie(): Boolean = Build.VERSION.SDK_INT >= Build.VERSION_CODES.P
}
