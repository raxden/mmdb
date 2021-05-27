package com.raxdenstudios.app.home.view

import android.os.Bundle
import android.view.View
import com.raxdenstudios.app.base.BaseFragment
import com.raxdenstudios.app.error.ErrorManager
import com.raxdenstudios.app.home.HomeNavigator
import com.raxdenstudios.app.home.R
import com.raxdenstudios.app.home.databinding.HomeMediaListFragmentBinding
import com.raxdenstudios.app.home.view.adapter.HomeModuleListAdapter
import com.raxdenstudios.app.home.view.model.HomeMediaListModel
import com.raxdenstudios.app.home.view.viewmodel.HomeMediaListUIState
import com.raxdenstudios.app.home.view.viewmodel.HomeMediaListViewModel
import com.raxdenstudios.commons.ext.addProgressViewEndTarget
import com.raxdenstudios.commons.ext.observe
import com.raxdenstudios.commons.ext.setPaddingTop
import com.raxdenstudios.commons.ext.viewBinding
import com.raxdenstudios.commons.util.SDK
import org.koin.android.ext.android.get
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

internal class HomeMediaListFragment : BaseFragment(R.layout.home_media_list_fragment) {

  companion object {
    fun newInstance() = HomeMediaListFragment()
  }

  private val binding: HomeMediaListFragmentBinding by viewBinding()
  private val viewModel: HomeMediaListViewModel by viewModel()
  private val navigator: HomeNavigator by lazy { requireActivity().get() }
  private val errorManager: ErrorManager by inject { parametersOf(requireActivity()) }

  private val adapter: HomeModuleListAdapter by lazy {
    HomeModuleListAdapter().apply {
      setHasStableIds(true)
    }
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    binding.setUp()

    observe(viewModel.state) { state -> binding.handleState(state) }
  }

  private fun HomeMediaListFragmentBinding.handleState(state: HomeMediaListUIState) = when (state) {
    is HomeMediaListUIState.Content -> handleContentState(state.model)
    is HomeMediaListUIState.Error -> handleErrorState(state.throwable)
    HomeMediaListUIState.Loading -> handleLoadingState()
  }

  private fun HomeMediaListFragmentBinding.handleContentState(model: HomeMediaListModel) {
    swipeRefreshLayout.isRefreshing = false
    adapter.submitList(model.modules)
    adapter.onCarouselMediaClickListener = { carouselMedias, mediaListItemModel ->
      viewModel.mediaSelected(model, carouselMedias, mediaListItemModel)
    }
    adapter.onCarouselAddToWatchListClickListener = { _, mediaListItemModel ->
      viewModel.addMediaToWatchList(model, mediaListItemModel)
    }
    adapter.onCarouselRemoveFromWatchListClickListener = { _, mediaListItemModel ->
      viewModel.removeMediaFromWatchList(model, mediaListItemModel)
    }
    adapter.onCarouselSeeAllClickListener = { carouselMedias ->
      navigator.launchMediaList(carouselMedias) { viewModel.loadData() }
    }
    adapter.onCarouselFilterChanged = { carouselMedias, mediaType ->
      viewModel.filterChanged(model, carouselMedias, mediaType)
    }
  }

  private fun HomeMediaListFragmentBinding.handleErrorState(throwable: Throwable) {
    swipeRefreshLayout.isRefreshing = false
    errorManager.handleError(throwable)
  }

  private fun HomeMediaListFragmentBinding.handleLoadingState() {
    swipeRefreshLayout.isRefreshing = true
  }

  private fun HomeMediaListFragmentBinding.setUp() {
    val statusBarHeight = SDK.getStatusBarHeight(requireContext())
    recyclerView.setPaddingTop(statusBarHeight)
    recyclerView.itemAnimator = null
    recyclerView.adapter = adapter
    swipeRefreshLayout.addProgressViewEndTarget(end = statusBarHeight)
    swipeRefreshLayout.setOnRefreshListener { viewModel.loadData() }
  }
}
