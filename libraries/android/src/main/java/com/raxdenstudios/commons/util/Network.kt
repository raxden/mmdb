package com.raxdenstudios.commons.util

import android.annotation.SuppressLint
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

object Network {

  fun isNetworkConnected(context: Context): Boolean {
    val connectivityManager = getConnectivityManager(context)
    return isNetworkConnected(connectivityManager)
  }

  private fun getConnectivityManager(context: Context) =
    context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

  @Suppress("DEPRECATION")
  @SuppressLint("MissingPermission")
  private fun isNetworkConnected(manager: ConnectivityManager): Boolean {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
      val activeNetwork = manager.activeNetwork ?: return false
      val networkCapabilities = manager.getNetworkCapabilities(activeNetwork) ?: return false
      return when {
        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
        else -> false
      }
    } else {
      val activeNetwork = manager.activeNetworkInfo ?: return false
      return when (activeNetwork.type) {
        ConnectivityManager.TYPE_WIFI -> true
        ConnectivityManager.TYPE_MOBILE -> true
        ConnectivityManager.TYPE_ETHERNET -> true
        else -> false
      }
    }
  }
}
