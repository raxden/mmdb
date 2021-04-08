package com.raxdenstudios.commons.test.android.matchers

import android.view.View
import android.view.ViewGroup
import android.widget.Checkable
import androidx.test.espresso.UiController
import androidx.test.espresso.ViewAction
import org.hamcrest.BaseMatcher
import org.hamcrest.CoreMatchers.isA
import org.hamcrest.Description

object ViewMatchers {

  fun clickOnChildViewWithId(resId: Int) = object : ViewAction {
    override fun getDescription(): String = ""

    override fun getConstraints() = object : BaseMatcher<View>() {

      override fun describeTo(description: Description) {}

      override fun matches(item: Any) = isA(ViewGroup::class.java).matches(item)
    }

    override fun perform(uiController: UiController?, view: View?) {
      val foundedView = view?.findViewById<View>(resId) ?: return
      foundedView.performClick()
    }
  }

  fun setChecked(checked: Boolean) = object : ViewAction {
    override fun getConstraints() = object : BaseMatcher<View>() {

      override fun describeTo(description: Description) {}

      override fun matches(item: Any) = isA(Checkable::class.java).matches(item)
    }

    override fun getDescription(): String = ""

    override fun perform(uiController: UiController, view: View) {
      val checkableView = view as? Checkable ?: return
      checkableView.isChecked = checked
    }
  }
}
