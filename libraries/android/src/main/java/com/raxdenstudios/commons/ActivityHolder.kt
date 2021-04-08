package com.raxdenstudios.commons

import androidx.fragment.app.FragmentActivity
import java.lang.ref.WeakReference

class ActivityHolder {

  private var activityReference: WeakReference<FragmentActivity>? = null
  var activity: FragmentActivity?
    get() = activityReference?.get()
    private set(value) {
      activityReference = if (value == null) null
      else WeakReference(value)
    }

  fun attach(activity: FragmentActivity) {
    this.activity = activity
  }

  fun detach(activity: FragmentActivity) {
    if (this.activity == activity) this.activity = null
  }
}
