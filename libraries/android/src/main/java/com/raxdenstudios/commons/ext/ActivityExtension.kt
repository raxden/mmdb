package com.raxdenstudios.commons.ext

import android.app.Activity
import android.content.Intent
import android.os.Parcelable
import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import com.raxdenstudios.commons.property.ActivityArgumentDelegate
import com.raxdenstudios.commons.property.ActivityContentViewDelegate
import com.raxdenstudios.commons.property.ActivityViewBindingDelegate
import com.raxdenstudios.commons.property.FragmentActivityByIdDelegate
import com.raxdenstudios.commons.util.SDK

fun Activity.setVirtualNavigationBarSafeArea(view: View) {
  view.setPaddingRelative(
    view.paddingStart,
    view.paddingTop,
    view.paddingEnd,
    view.paddingBottom + SDK.getVirtualNavigationBarHeight(this)
  )
}

fun Activity.setStatusBarSafeArea(view: View) {
  view.setPaddingRelative(
    view.paddingStart,
    view.paddingTop + SDK.getStatusBarHeight(this),
    view.paddingEnd,
    view.paddingBottom
  )
}

fun Activity.setFullScreen() {
  window.decorView.apply {
    systemUiVisibility = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN
  }
}

fun <T : Parcelable> Activity.loadParcelable(
  key: String,
  defaultValue: T? = null,
  operation: (value: T) -> Unit
) {
  val value = intent?.extras?.getParcelable(key) ?: defaultValue
  if (value != null) operation(value)
}

fun Activity.setResultOK() {
  setResult(Activity.RESULT_OK)
}

fun Activity.setResultOKAndFinish() {
  setResultOK()
  finish()
}

inline fun <reified T : Parcelable> Activity.setResultOKWithData(value: T) {
  setResult(
    Activity.RESULT_OK,
    Intent().apply { putExtra(T::class.java.simpleName, value) }
  )
}

inline fun <reified T : Parcelable> Activity.setResultOKWithDataAndFinish(value: T) {
  setResultOKWithData(value)
  finish()
}

fun Activity.contentView(@LayoutRes resId: Int) = ActivityContentViewDelegate(resId)

inline fun <reified T : Any> Activity.argument() = ActivityArgumentDelegate<T>()

inline fun <reified T : ViewBinding> Activity.viewBinding() =
  ActivityViewBindingDelegate(T::class.java)

fun <T : Fragment> Activity.fragmentById(
  @IdRes fragmentId: Int
) = FragmentActivityByIdDelegate<T>(fragmentId)
