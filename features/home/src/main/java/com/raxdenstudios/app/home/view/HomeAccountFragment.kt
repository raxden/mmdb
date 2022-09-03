package com.raxdenstudios.app.home.view

import com.raxdenstudios.app.base.BaseFragment
import com.raxdenstudios.app.home.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
internal class HomeAccountFragment : BaseFragment(R.layout.home_account_fragment) {

    companion object {
        fun newInstance() = HomeAccountFragment()
    }
}
