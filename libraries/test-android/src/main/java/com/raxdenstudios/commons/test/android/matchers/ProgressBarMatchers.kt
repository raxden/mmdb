package com.raxdenstudios.commons.test.android.matchers

import android.view.View
import android.widget.ProgressBar
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

object ProgressBarMatchers {

  fun hasProgress(progress: Int, max: Int) = object : TypeSafeMatcher<View>() {
    override fun describeTo(description: Description) {}

    override fun matchesSafely(view: View): Boolean {
      val progressBar = (view as? ProgressBar) ?: return false
      return progressBar.progress == progress && progressBar.max == max
    }
  }
}
