package com.raxdenstudios.commons.ext

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import androidx.viewbinding.ViewBinding
import com.raxdenstudios.commons.property.DialogBindingDelegate

fun Dialog.setTransparentBackground() {
  window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
}

inline fun <reified T : ViewBinding> Dialog.viewBinding() = DialogBindingDelegate(T::class.java)
