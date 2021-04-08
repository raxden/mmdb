package com.raxdenstudios.commons.ext

import androidx.viewpager.widget.ViewPager

fun ViewPager.isFirstPage() = currentItem == 0

fun ViewPager.isLastPage() = currentItem == adapter?.let { it.count - 1 } ?: false

fun ViewPager.previousPage(): Boolean = if (isFirstPage()) false else {
  currentItem -= 1
  true
}

fun ViewPager.nextPage(): Boolean = if (isLastPage()) false else {
  currentItem += 1
  true
}

fun ViewPager.addOnPageChangeListener(
  onPageScrolled: (position: Int, positionOffset: Float, positionOffsetPixels: Int) -> Unit,
  onPageSelected: (position: Int) -> Unit,
  onPageScrollStateChanged: (state: Int) -> Unit
) {
  addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
    override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
      onPageScrolled(position, positionOffset, positionOffsetPixels)
    }

    override fun onPageSelected(position: Int) {
      onPageSelected(position)
    }

    override fun onPageScrollStateChanged(state: Int) {
      onPageScrollStateChanged(state)
    }
  })
}
