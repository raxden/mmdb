package com.raxdenstudios.commons.test.android.matchers

import android.view.View
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

object SwipeRefreshLayoutMatchers {

  fun isSwipeRefreshLayoutRefreshing() = object : TypeSafeMatcher<View>() {
    override fun describeTo(description: Description) {}

    override fun matchesSafely(view: View): Boolean {
      val swipeRefreshLayout = (view as? SwipeRefreshLayout) ?: return false
      return swipeRefreshLayout.isRefreshing
    }
  }
}
