package com.raxdenstudios.commons.property

import android.app.Dialog
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class DialogBindingDelegate<T : ViewBinding>(
  bindingClass: Class<T>
) : ReadOnlyProperty<Dialog, T> {

  private val inflateMethod = bindingClass.getMethod(
    "inflate",
    LayoutInflater::class.java,
    ViewGroup::class.java,
    Boolean::class.java
  )
  private var binding: T? = null

  override fun getValue(
    thisRef: Dialog,
    property: KProperty<*>
  ): T = binding ?: createBinding(thisRef).also { binding = it }

  @Suppress("UNCHECKED_CAST")
  private fun createBinding(thisRef: Dialog): T {
    val binding = inflateMethod.invoke(null, thisRef.layoutInflater, null, false) as T
    thisRef.setContentView(binding.root)
    return binding
  }
}
