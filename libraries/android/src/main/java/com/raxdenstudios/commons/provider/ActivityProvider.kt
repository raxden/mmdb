package com.raxdenstudios.commons.provider

import android.app.Activity
import android.app.Application
import android.os.Bundle

class ActivityProvider(
  application: Application
) {
  var activeActivity: Activity? = null

  init {
    application.registerActivityLifecycleCallbacks(object : Application.ActivityLifecycleCallbacks {
      override fun onActivityPaused(activity: Activity) {
        activeActivity = null
      }

      override fun onActivityResumed(activity: Activity) {
        activeActivity = activity
      }

      override fun onActivityStarted(activity: Activity) {}

      override fun onActivityDestroyed(activity: Activity) {}

      override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}

      override fun onActivityStopped(activity: Activity) {}

      override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        activeActivity = activity
      }
    })
  }
}
