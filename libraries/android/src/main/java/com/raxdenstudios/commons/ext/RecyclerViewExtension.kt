package com.raxdenstudios.commons.ext

import androidx.recyclerview.widget.RecyclerView

fun RecyclerView.addOnScrolledListener(
  onScrolled: (RecyclerView, Int, Int) -> Unit = { _, _, _ -> }
): RecyclerView.OnScrollListener {
  val listener = object : RecyclerView.OnScrollListener() {
    override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
      onScrolled(recyclerView, dx, dy)
    }
  }
  addOnScrollListener(listener)
  return listener
}
