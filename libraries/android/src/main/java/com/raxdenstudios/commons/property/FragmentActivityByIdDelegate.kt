package com.raxdenstudios.commons.property

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

@Suppress("UNCHECKED_CAST")
class FragmentActivityByIdDelegate<T : Fragment>(
  @IdRes private val fragmentId: Int
) : ReadOnlyProperty<FragmentActivity, T> {

  private var fragment: T? = null

  override fun getValue(
    thisRef: FragmentActivity,
    property: KProperty<*>
  ): T = fragment ?: findFragmentById(thisRef).also { fragment = it }

  private fun findFragmentById(
    thisRef: FragmentActivity
  ): T = thisRef.supportFragmentManager.findFragmentById(fragmentId) as T
}
