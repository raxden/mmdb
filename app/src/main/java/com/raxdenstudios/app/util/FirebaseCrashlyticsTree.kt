package com.raxdenstudios.app.util

import android.util.Log
import com.google.firebase.crashlytics.FirebaseCrashlytics
import timber.log.Timber

class FirebaseCrashlyticsTree : Timber.Tree() {

    override fun log(priority: Int, tag: String?, message: String, throwable: Throwable?) {
        if (priority == Log.VERBOSE || priority == Log.DEBUG) return
        if (messageCondition(message)) return
        if (throwable != null) FirebaseCrashlytics.getInstance().recordException(throwable)
    }

    private fun messageCondition(message: String): Boolean {
        return (message.contains("java.net.UnknownHostException")
                || message.contains("java.net.ConnectException")
                || message.contains("java.net.SocketTimeoutException"))
    }
}
