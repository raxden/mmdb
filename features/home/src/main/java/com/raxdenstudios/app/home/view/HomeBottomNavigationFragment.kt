package com.raxdenstudios.app.home.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.raxdenstudios.app.home.R
import com.raxdenstudios.app.home.databinding.HomeBottomNavigationFragmentBinding
import com.raxdenstudios.app.home.view.viewmodel.HomeBottomNavigationViewModel
import com.raxdenstudios.commons.ext.observe
import com.raxdenstudios.commons.ext.viewBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeBottomNavigationFragment : Fragment(R.layout.home_bottom_navigation_fragment) {

  private val navController by lazy { requireActivity().findNavController(R.id.nav_host_container) }
  private val binding: HomeBottomNavigationFragmentBinding by viewBinding()
  private val viewModel: HomeBottomNavigationViewModel by viewModels()

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    binding.setUp()

    observe(viewModel.state) { state -> binding.handleState(state) }
  }

  private fun HomeBottomNavigationFragmentBinding.handleState(state: HomeBottomNavigationViewModel.UIState) {
    navigationView.setOnItemSelectedListener { menuItem ->
      state.bottomUIState.onItemSelected(menuItem.itemId)
      NavigationUI.onNavDestinationSelected(menuItem, navController)
      true
    }
    navigationView.setOnItemReselectedListener { menuItem ->
      state.bottomUIState.onItemReselected(menuItem.itemId)
    }
  }

  private fun HomeBottomNavigationFragmentBinding.setUp() {
    navigationView.setupWithNavController(navController)
  }
}
