package com.raxdenstudios.commons.ext

import android.view.View
import androidx.core.widget.NestedScrollView

const val SHORT_DELAY: Long = 250

fun NestedScrollView.scrollToBottom() {
  post { fullScroll(View.FOCUS_DOWN) }
}

fun NestedScrollView.scrollToBottomWithDelay(delayInMillis: Long = SHORT_DELAY) {
  postDelayed(
    { fullScroll(View.FOCUS_DOWN) },
    delayInMillis
  )
}
