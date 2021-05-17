package com.raxdenstudios.app.home.view.util

import android.os.Bundle
import androidx.fragment.app.Fragment
import com.raxdenstudios.app.home.R
import com.raxdenstudios.app.home.databinding.HomeV2ActivityBinding
import com.raxdenstudios.app.home.view.HomeAccountFragment
import com.raxdenstudios.app.home.view.HomeV2Activity
import com.raxdenstudios.app.home.view.HomeMediaListFragment
import com.raxdenstudios.app.home.view.HomeSearchMediaListFragment
import com.raxdenstudios.app.home.view.model.HomeMediaListParams
import com.raxdenstudios.commons.delegate.FragmentBottomNavigationDelegate

internal class HomeNavigationDelegate(
  private val activity: HomeV2Activity,
  private val binding: HomeV2ActivityBinding
) {

  private var movieListFragmentHome: HomeMediaListFragment? = null
  private var tvShowListFragmentHome: HomeMediaListFragment? = null
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
    R.id.home_navigation_movies -> HomeMediaListFragment.newInstance(HomeMediaListParams.Movies)
    R.id.home_navigation_tv_shows -> HomeMediaListFragment.newInstance(HomeMediaListParams.TVShows)
    R.id.home_navigation_search -> HomeSearchMediaListFragment.newInstance()
    R.id.home_navigation_account -> HomeAccountFragment.newInstance()
    else -> throw IllegalStateException("ItemId with value $itemId is invalid")
  }

  private fun fragmentLoaded(itemId: Int, fragment: Fragment) {
    when (itemId) {
      R.id.home_navigation_movies ->
        movieListFragmentHome = fragment as HomeMediaListFragment
      R.id.home_navigation_tv_shows ->
        tvShowListFragmentHome = fragment as HomeMediaListFragment
      R.id.home_navigation_search ->
        homeSearchMediaListFragment = fragment as HomeSearchMediaListFragment
      R.id.home_navigation_account ->
        homeAccountFragment = fragment as HomeAccountFragment
    }
  }
}
