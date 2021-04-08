package com.raxdenstudios.commons.property

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.DefaultLifecycleObserver
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

class FragmentViewBindingDelegate<T : ViewBinding>(
  bindingClass: Class<T>,
  private val fragment: Fragment
) : ReadOnlyProperty<Fragment, T>, DefaultLifecycleObserver {

  private val bindMethod = bindingClass.getMethod("bind", View::class.java)
  private var binding: T? = null

  init {
    fragment.lifecycle.addObserver(this)
  }

  override fun onCreate(owner: LifecycleOwner) {
    super.onCreate(owner)

    fragment.viewLifecycleOwnerLiveData.observe(fragment) { viewLifecycleOwner ->
      viewLifecycleOwner.lifecycle.addObserver(object : DefaultLifecycleObserver {
        override fun onDestroy(owner: LifecycleOwner) {
          binding = null
        }
      })
    }
  }

  override fun getValue(
    thisRef: Fragment,
    property: KProperty<*>
  ): T = binding ?: createBinding(thisRef).also { binding = it }

  @Suppress("UNCHECKED_CAST")
  private fun createBinding(thisRef: Fragment): T {
    throwErrorIfLifecycleIsNotInitialized(thisRef)
    return bindMethod.invoke(null, thisRef.requireView()) as T
  }

  private fun throwErrorIfLifecycleIsNotInitialized(thisRef: Fragment) {
    val lifecycle = thisRef.viewLifecycleOwner.lifecycle
    if (!lifecycle.currentState.isAtLeast(Lifecycle.State.INITIALIZED))
      error("Cannot access view bindings. View lifecycle is ${lifecycle.currentState}")
  }
}

