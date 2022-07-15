package com.raxdenstudios.app.home.view

import com.raxdenstudios.app.base.BaseFragment
import com.raxdenstudios.app.home.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class HomeSearchMediaListFragment :
  BaseFragment(R.layout.home_search_media_list_fragment) {

  companion object {
    fun newInstance() = HomeSearchMediaListFragment()
  }
}
