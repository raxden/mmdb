package com.raxdenstudios.commons.property

import androidx.fragment.app.Fragment
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class FragmentArgumentDelegate<T : Any> : ReadOnlyProperty<Fragment, T> {

  private var data: T? = null

  override fun getValue(
    thisRef: Fragment,
    property: KProperty<*>
  ): T = data ?: getValueFromArguments(thisRef, property).also { data = it }

  @Suppress("UNCHECKED_CAST")
  private fun getValueFromArguments(
    thisRef: Fragment,
    property: KProperty<*>
  ): T = thisRef.arguments?.get(property.name) as T
}
