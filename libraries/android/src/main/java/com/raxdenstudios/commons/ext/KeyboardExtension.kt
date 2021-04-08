package com.raxdenstudios.commons.ext

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.IBinder
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.fragment.app.Fragment

@SuppressLint("ClickableViewAccessibility")
fun Activity.closeKeyboardWhenTouchScreen(view: View = findViewById(android.R.id.content)) {
  // Set up touch listener for non-text box views to hide keyboard.
  if (view !is EditText) {
    view.setOnTouchListener { _, _ ->
      closeKeyboard()
      false
    }
  }
  //If a layout container, iterate over children and seed recursion.
  if (view is ViewGroup) {
    for (i in 0 until view.childCount) {
      closeKeyboardWhenTouchScreen(view.getChildAt(i))
    }
  }
}

fun Fragment.closeKeyboard() {
  requireActivity().closeKeyboard()
}

fun Fragment.closeKeyboard(binder: IBinder) {
  requireActivity().closeKeyboard(binder)
}

fun Activity.closeKeyboard() {
  val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
  imm.hideSoftInputFromWindow(findViewById<View>(android.R.id.content).windowToken, 0)
}

fun Activity.closeKeyboard(binder: IBinder) {
  val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
  imm.hideSoftInputFromWindow(binder, 0)
}

fun Activity.openKeyboard() {
  val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
  imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY)
}

fun Fragment.openKeyboard() {
  requireActivity().openKeyboard()
}
