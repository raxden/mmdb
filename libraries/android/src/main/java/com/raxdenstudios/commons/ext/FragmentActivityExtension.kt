package com.raxdenstudios.commons.ext

import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.commit

inline fun <reified TFragment : Fragment> FragmentActivity.loadFragment(
  containerView: View,
  savedInstanceState: Bundle?,
  create: () -> TFragment
): TFragment = savedInstanceState?.let {
  supportFragmentManager.findFragmentById(containerView.id) as? TFragment
} ?: create().also { fragment ->
  supportFragmentManager.commit {
    replace(containerView.id, fragment, fragment.javaClass.simpleName)
  }
}

fun FragmentActivity.closeFragment(
  fragment: Fragment
) {
  supportFragmentManager.commit { remove(fragment) }
}

inline fun <reified TFragment : DialogFragment> FragmentActivity.loadDialogFragment(
  tag: String,
  isCancelable: Boolean = false,
  create: () -> TFragment
): TFragment {
  val dialogFragment =
    supportFragmentManager.findFragmentByTag(tag) as? TFragment ?: create().also { fragment ->
      supportFragmentManager.commit(true) { add(fragment, tag) }
    }
  dialogFragment.isCancelable = isCancelable
  return dialogFragment
}
