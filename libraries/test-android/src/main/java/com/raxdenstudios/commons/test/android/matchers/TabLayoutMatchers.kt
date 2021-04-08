package com.raxdenstudios.commons.test.android.matchers

import android.view.View
import androidx.test.espresso.PerformException
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import androidx.test.espresso.matcher.ViewMatchers
import com.google.android.material.tabs.TabLayout
import org.hamcrest.Matchers

object TabLayoutMatchers {

  fun selectTabAtPosition(position: Int) = object : ViewAction {

    override fun getConstraints() = Matchers.allOf(
      ViewMatchers.isDisplayed(),
      ViewMatchers.isAssignableFrom(TabLayout::class.java)
    )

    override fun getDescription() = "With tab at index $position"

    override fun perform(uiController: UiController?, view: View?) {
      val tabLayout = view as? TabLayout ?: return
      val tabAtPosition = tabLayout.findTabAt(position)
      tabAtPosition.select()
    }
  }

  private fun TabLayout.findTabAt(position: Int): TabLayout.Tab {
    return getTabAt(position) ?: throw PerformException.Builder()
      .withCause(Throwable("No tab at index $position"))
      .build()
  }
}
