package com.raxdenstudios.commons.property

import android.app.Activity
import android.view.View
import androidx.annotation.LayoutRes
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class ActivityContentViewDelegate(
  @LayoutRes private val layoutId: Int
) : ReadOnlyProperty<Activity, View> {

  private var view: View? = null

  override fun getValue(
    thisRef: Activity,
    property: KProperty<*>
  ): View = view ?: createView(thisRef).also { view = it }

  private fun createView(
    thisRef: Activity
  ): View = thisRef.setContentView(layoutId).let { thisRef.findViewById(android.R.id.content) }
}
