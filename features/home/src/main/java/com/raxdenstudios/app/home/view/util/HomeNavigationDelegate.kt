package com.raxdenstudios.app.home.view.util

//internal class HomeNavigationDelegate(
//  private val activity: HomeActivity,
//  private val binding: HomeActivityBinding
//) {
//
//  private var movieListFragmentHome: HomeMediaListFragment? = null
//  private var homeSearchMediaListFragment: HomeSearchMediaListFragment? = null
//  private var homeAccountFragment: HomeAccountFragment? = null
//
//  private val homeNavigationDelegate by lazy {
//    FragmentBottomNavigationDelegate(
//      fragmentManager = activity.supportFragmentManager,
//      fragmentContainerView = binding.navigationFragmentContainer,
//      bottomNavigationView = binding.navigationView,
//      onCreateFragment = ::createFragment
//    )
//  }
//
//  fun onSaveInstanceState(outState: Bundle) {
//    homeNavigationDelegate.saveState(outState)
//  }
//
//  fun onCreate(savedInstanceState: Bundle?) {
//    homeNavigationDelegate.init(savedInstanceState)
//    homeNavigationDelegate.onFragmentLoaded = ::fragmentLoaded
//  }
//
//  private fun createFragment(itemId: Int): Fragment = when (itemId) {
//    R.id.homeMediaListFragment -> HomeMediaListFragment.newInstance()
//    R.id.homeSearchFragment -> HomeSearchMediaListFragment.newInstance()
//    R.id.homeAccountFragment -> HomeAccountFragment.newInstance()
//    else -> throw IllegalStateException("ItemId with value $itemId is invalid")
//  }
//
//  private fun fragmentLoaded(itemId: Int, fragment: Fragment) {
//    when (itemId) {
//      R.id.homeMediaListFragment ->
//        movieListFragmentHome = fragment as HomeMediaListFragment
//      R.id.homeSearchFragment ->
//        homeSearchMediaListFragment = fragment as HomeSearchMediaListFragment
//      R.id.homeAccountFragment ->
//        homeAccountFragment = fragment as HomeAccountFragment
//    }
//  }
//}
