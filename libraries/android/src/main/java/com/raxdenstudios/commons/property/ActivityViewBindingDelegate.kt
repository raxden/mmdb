package com.raxdenstudios.commons.property

import android.app.Activity
import android.view.LayoutInflater
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class ActivityViewBindingDelegate<T : ViewBinding>(
  bindingClass: Class<T>
) : ReadOnlyProperty<Activity, T> {

  private val inflateMethod = bindingClass.getMethod("inflate", LayoutInflater::class.java)
  private var binding: T? = null

  override fun getValue(
    thisRef: Activity,
    property: KProperty<*>
  ): T = binding ?: createBinding(thisRef).also { binding = it }

  @Suppress("UNCHECKED_CAST")
  private fun createBinding(thisRef: Activity): T {
    val binding = inflateMethod.invoke(null, thisRef.layoutInflater) as T
    thisRef.setContentView(binding.root)
    return binding
  }
}
