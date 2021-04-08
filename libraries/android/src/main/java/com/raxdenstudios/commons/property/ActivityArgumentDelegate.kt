package com.raxdenstudios.commons.property

import android.app.Activity
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class ActivityArgumentDelegate<T : Any> : ReadOnlyProperty<Activity, T> {

  private var data: T? = null

  override fun getValue(
    thisRef: Activity,
    property: KProperty<*>
  ): T = data ?: getValueFromExtras(thisRef, property).also { data = it }

  @Suppress("UNCHECKED_CAST")
  private fun getValueFromExtras(
    thisRef: Activity,
    property: KProperty<*>
  ): T = thisRef.intent.extras?.get(property.name) as T
}

