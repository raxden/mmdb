package com.raxdenstudios.commons.ext

import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.Observer
import com.raxdenstudios.commons.Event

fun <T : Any, L : LiveData<T>> LifecycleOwner.observe(
  liveData: L,
  body: (T) -> Unit
) {
  liveData.observe(this, Observer(body))
}

fun <T : Any, L : LiveData<T>> Fragment.observe(
  liveData: L,
  body: (T) -> Unit
) {
  liveData.observe(viewLifecycleOwner, Observer(body))
}

fun <T : Any, L : LiveData<T>> View.observe(
  liveData: L,
  body: (T) -> Unit
) {
  liveData.observe(context as LifecycleOwner, Observer(body))
}

inline fun <T : Any, L : LiveData<Event<T>>> LifecycleOwner.observeEvent(
  liveData: L,
  crossinline body: (T) -> Unit
) {
  liveData.observe(this, { it?.getContentIfNotHandled()?.let(body) })
}

inline fun <T : Any, L : LiveData<Event<T>>> Fragment.observeEvent(
  liveData: L,
  crossinline body: (T) -> Unit
) {
  liveData.observe(viewLifecycleOwner, { it?.getContentIfNotHandled()?.let(body) })
}

inline fun <T : Any, L : LiveData<Event<T>>> View.observeEvent(
  liveData: L,
  crossinline body: (T) -> Unit
) {
  liveData.observe(context as LifecycleOwner, { it?.getContentIfNotHandled()?.let(body) })
}
