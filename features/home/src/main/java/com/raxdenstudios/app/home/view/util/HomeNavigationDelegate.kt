package com.raxdenstudios.app.home.view.util

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.raxdenstudios.app.home.R
import com.raxdenstudios.app.home.databinding.HomeActivityBinding
import com.raxdenstudios.app.home.view.HomeAccountFragment
import com.raxdenstudios.app.home.view.HomeActivity
import com.raxdenstudios.app.home.view.HomeMediaListFragment
import com.raxdenstudios.app.home.view.HomeSearchMediaListFragment
import com.raxdenstudios.commons.delegate.FragmentBottomNavigationDelegate

internal class HomeNavigationDelegate(
  private val activity: HomeActivity,
  private val binding: HomeActivityBinding
) {

  private var movieListFragmentHome: HomeMediaListFragment? = null
  private var homeSearchMediaListFragment: HomeSearchMediaListFragment? = null
  private var homeAccountFragment: HomeAccountFragment? = null

  private val homeNavigationDelegate by lazy {
    FragmentBottomNavigationDelegate(
      fragmentManager = activity.supportFragmentManager,
      fragmentContainerView = binding.navigationFragmentContainer,
      bottomNavigationView = binding.navigationView,
      onCreateFragment = ::createFragment
    )
  }

  fun onSaveInstanceState(outState: Bundle) {
    homeNavigationDelegate.saveState(outState)
  }

  fun onCreate(savedInstanceState: Bundle?) {
    homeNavigationDelegate.init(savedInstanceState)
    homeNavigationDelegate.onFragmentLoaded = ::fragmentLoaded
  }

  private fun createFragment(itemId: Int): Fragment = when (itemId) {
    R.id.home_navigation_home -> HomeMediaListFragment.newInstance()
    R.id.home_navigation_search -> HomeSearchMediaListFragment.newInstance()
    R.id.home_navigation_account -> HomeAccountFragment.newInstance()
    else -> throw IllegalStateException("ItemId with value $itemId is invalid")
  }

  private fun fragmentLoaded(itemId: Int, fragment: Fragment) {
    when (itemId) {
      R.id.home_navigation_home ->
        movieListFragmentHome = fragment as HomeMediaListFragment
      R.id.home_navigation_search ->
        homeSearchMediaListFragment = fragment as HomeSearchMediaListFragment
      R.id.home_navigation_account ->
        homeAccountFragment = fragment as HomeAccountFragment
    }
  }
}
