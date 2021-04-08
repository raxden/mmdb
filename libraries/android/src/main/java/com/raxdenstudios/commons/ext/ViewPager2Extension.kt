package com.raxdenstudios.commons.ext

import androidx.viewpager2.widget.ViewPager2

fun ViewPager2.isFirstPage() = currentItem == 0

fun ViewPager2.isLastPage() = currentItem == adapter?.let { it.itemCount - 1 } ?: false

fun ViewPager2.previousPage(): Boolean = if (isFirstPage()) false else {
  currentItem -= 1
  true
}

fun ViewPager2.nextPage(): Boolean = if (isLastPage()) false else {
  currentItem += 1
  true
}

fun ViewPager2.registerOnPageChangeCallback(
  onPageScrolled: (position: Int, positionOffset: Float, positionOffsetPixels: Int) -> Unit = { _, _, _ -> },
  onPageSelected: (position: Int) -> Unit = {},
  onPageScrollStateChanged: (state: Int) -> Unit = {}
) {
  registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
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
