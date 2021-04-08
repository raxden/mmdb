package com.raxdenstudios.commons.delegate

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.raxdenstudios.commons.ext.isFirstPage
import com.raxdenstudios.commons.ext.isLastPage
import com.raxdenstudios.commons.ext.registerOnPageChangeCallback

/*
 * ViewPager2 is an improved version of the ViewPager library that offers enhanced functionality and
 * addresses common difficulties with using ViewPager. The primary reason to migrate is that
 * ViewPager2 is receiving active development support and ViewPager is not. However, ViewPager2 also
 * offers several other specific advantages.
 *  - ViewPager2 supports vertical paging in addition to traditional horizontal paging.
 *  - ViewPager2 supports right-to-left (RTL) paging
 *  - ViewPager2 supports paging through a modifiable collection of fragments, calling
 * notifyDatasetChanged() to update the UI when the underlying collection changes. This means that
 * your app can dynamically modify the fragment collection at runtime, and ViewPager2 will correctly
 * display the modified collection
 *  - ViewPager2 is built on RecyclerView, which means it has access to the DiffUtil utility class.
 * This results in several benefits, but most notably it means that ViewPager2 objects natively take
 * advantage of the dataset change animations from the RecyclerView class.
 *
 * More info -> https://developer.android.com/training/animation/vp2-migration
 */
class FragmentViewPager2Delegate<TFragment : Fragment>(
  fragmentManager: FragmentManager,
  lifecycle: Lifecycle,
  private val callback: Callback<TFragment>
) {

  constructor(
    activity: FragmentActivity,
    callback: Callback<TFragment>
  ) : this(activity.supportFragmentManager, activity.lifecycle, callback)

  constructor(
    fragment: Fragment,
    callback: Callback<TFragment>
  ) : this(fragment.childFragmentManager, fragment.lifecycle, callback)

  interface Callback<TFragment> {
    val fragmentCount: Int

    fun onCreateViewPager(): ViewPager2
    fun onCreateFragment(position: Int): TFragment
  }

  interface TabLayoutCallback<TFragment> : Callback<TFragment> {
    fun onCreateTabLayout(): TabLayout
    fun onFragmentPageTitle(position: Int): String
  }

  private val viewPager2: ViewPager2 = callback.onCreateViewPager()

  var onPageScroll: (position: Int, positionOffset: Float, positionOffsetPixels: Int) -> Unit =
    { _, _, _ -> }
  var onPageScrolled: (position: Int) -> Unit = {}
  var onPageSelected: (position: Int) -> Unit = {}
  var onLastPageReached: () -> Unit = {}
  var onPageScrollStateChanged: (state: Int) -> Unit = {}

  val numPages: Int
    get() = viewPager2.adapter?.itemCount ?: 0

  var currentPage: Int
    get() = viewPager2.currentItem
    set(value) {
      viewPager2.currentItem = value
    }

  val isFirstPage: Boolean
    get() = viewPager2.isFirstPage()

  val isLastPage: Boolean
    get() = viewPager2.isLastPage()

  init {
    viewPager2.offscreenPageLimit = 2
    viewPager2.adapter = FragmentStateAdapterDelegate(fragmentManager, lifecycle)
    viewPager2.registerOnPageChangeCallback(
      onPageScrolled = ::pageScrolled,
      onPageSelected = ::pageSelected,
      onPageScrollStateChanged = { state -> onPageScrollStateChanged(state) }
    )

    if (callback is TabLayoutCallback) {
      val tabLayout = callback.onCreateTabLayout()
      TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
        tab.text = callback.onFragmentPageTitle(position)
      }.attach()
    }
  }

  private fun pageSelected(position: Int) {
    onPageSelected(position)
    if (isLastPage) onLastPageReached()
  }

  private fun pageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
    onPageScroll(position, positionOffset, positionOffsetPixels)
    if (positionOffset != 0.0f || positionOffsetPixels != 0) return
    onPageScrolled(position)
  }

  private inner class FragmentStateAdapterDelegate constructor(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
  ) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount() = callback.fragmentCount

    override fun createFragment(position: Int) = callback.onCreateFragment(position)
  }
}
