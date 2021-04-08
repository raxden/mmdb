package com.raxdenstudios.commons.test.android.matchers

import android.view.View
import android.widget.Checkable
import androidx.recyclerview.widget.RecyclerView
import org.hamcrest.Description
import org.hamcrest.TypeSafeMatcher

object RecyclerViewMatchers {

  fun hasRecyclerViewItemCount(itemCount: Int) = object : TypeSafeMatcher<View>() {

    override fun describeTo(description: Description) {}

    override fun matchesSafely(view: View): Boolean {
      val recyclerView = view as? RecyclerView ?: return false
      return recyclerView.adapter?.itemCount == itemCount
    }
  }

  fun checkBoxState(itemCount: Int, checkBoxId: Int, isChecked: Boolean) =
    object : TypeSafeMatcher<View>() {
      override fun describeTo(description: Description) {}

      override fun matchesSafely(view: View): Boolean {
        val recyclerView = view as? RecyclerView ?: return false
        val viewHolder = recyclerView.findViewHolderForAdapterPosition(itemCount) ?: return false
        val checkableView =
          (viewHolder.itemView.findViewById<View>(checkBoxId) as? Checkable) ?: return false
        return checkableView.isChecked == isChecked
      }
    }
}
