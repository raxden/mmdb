package com.raxdenstudios.app.base

import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.LayoutRes
import androidx.fragment.app.DialogFragment
import com.raxdenstudios.commons.ext.setTransparentBackground

abstract class BaseFragmentDialog(@LayoutRes private val layoutId: Int) : DialogFragment() {

  private var onDismissListener: MutableList<DialogInterface.OnDismissListener> = mutableListOf()

  override fun onCreateView(
    inflater: LayoutInflater,
    container: ViewGroup?,
    savedInstanceState: Bundle?
  ): View = inflater.inflate(layoutId, container)

  override fun onCreateDialog(savedInstanceState: Bundle?): Dialog =
    super.onCreateDialog(savedInstanceState).also { dialog -> dialog.setTransparentBackground() }

  override fun onStart() {
    super.onStart()

    dialog?.window?.setLayout(
      ViewGroup.LayoutParams.MATCH_PARENT,
      ViewGroup.LayoutParams.MATCH_PARENT
    )
  }

  override fun onDestroy() {
    onDismissListener.clear()
    onDismissListener = mutableListOf()
    super.onDestroy()
  }

  override fun onDismiss(dialog: DialogInterface) {
    super.onDismiss(dialog)
    onDismissListener.forEach { it.onDismiss(dialog) }
  }

  fun addOnDismissListener(onDismissListener: () -> Unit = {}) {
    addOnDismissListener(DialogInterface.OnDismissListener { onDismissListener() })
  }

  fun addOnDismissListener(onDismissListener: DialogInterface.OnDismissListener) {
    this.onDismissListener.add(onDismissListener)
  }

  fun removeOnDismissListener(onDismissListener: DialogInterface.OnDismissListener) {
    this.onDismissListener.remove(onDismissListener)
  }
}
