package com.raxdenstudios.commons.property

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class ViewBindingDelegate<T : ViewBinding>(
  bindingClass: Class<T>
) : ReadOnlyProperty<ViewGroup, T> {

  private val inflateMethod = bindingClass.getMethod(
    "inflate",
    LayoutInflater::class.java,
    ViewGroup::class.java,
    Boolean::class.java
  )
  private var binding: T? = null

  override fun getValue(
    thisRef: ViewGroup,
    property: KProperty<*>
  ): T = binding ?: createBinding(thisRef).also { binding = it }

  @Suppress("UNCHECKED_CAST")
  private fun createBinding(thisRef: ViewGroup): T {
    return inflateMethod.invoke(null, LayoutInflater.from(thisRef.context), thisRef, true) as T
  }
}
