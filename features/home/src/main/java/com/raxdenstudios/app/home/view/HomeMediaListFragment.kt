package com.raxdenstudios.app.home.view

import androidx.core.os.bundleOf
import com.raxdenstudios.app.base.BaseFragment
import com.raxdenstudios.app.home.R
import com.raxdenstudios.app.home.view.model.HomeMediaListParams

internal class HomeMediaListFragment : BaseFragment(R.layout.home_media_list_fragment) {

  companion object {
    fun newInstance(params: HomeMediaListParams) = HomeMediaListFragment().apply {
      arguments = bundleOf("params" to params)
    }
  }
}
