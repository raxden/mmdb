package com.raxdenstudios.app.home.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.viewModels
import com.raxdenstudios.app.base.BaseFragment
import com.raxdenstudios.app.error.ErrorManager
import com.raxdenstudios.app.home.HomeMediaListNavigator
import com.raxdenstudios.app.home.R
import com.raxdenstudios.app.home.databinding.HomeMediaListFragmentBinding
import com.raxdenstudios.app.home.view.adapter.HomeModuleListAdapter
import com.raxdenstudios.app.home.view.model.HomeMediaListModel
import com.raxdenstudios.app.home.view.viewmodel.HomeMediaListViewModel
import com.raxdenstudios.commons.ext.addProgressViewEndTarget
import com.raxdenstudios.commons.ext.observe
import com.raxdenstudios.commons.ext.setPaddingTop
import com.raxdenstudios.commons.ext.viewBinding
import com.raxdenstudios.commons.util.SDK
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
internal class HomeMediaListFragment : BaseFragment(R.layout.home_media_list_fragment) {

  companion object {
    fun newInstance() = HomeMediaListFragment()
  }

  @Inject
  lateinit var navigator: HomeMediaListNavigator

  @Inject
  lateinit var errorManager: ErrorManager

  private val binding: HomeMediaListFragmentBinding by viewBinding()
  private val viewModel: HomeMediaListViewModel by viewModels()

  private val adapter: HomeModuleListAdapter by lazy {
    HomeModuleListAdapter().apply {
      setHasStableIds(true)
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
  }

  override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    super.onViewCreated(view, savedInstanceState)

    binding.setUp()

    observe(viewModel.state) { state -> binding.handleState(state) }
  }

  private fun HomeMediaListFragmentBinding.handleState(state: HomeMediaListViewModel.UIState) =
    when (state) {
      is HomeMediaListViewModel.UIState.Content -> handleContentState(state.model)
      is HomeMediaListViewModel.UIState.Error -> handleErrorState(state.throwable)
      HomeMediaListViewModel.UIState.Loading -> handleLoadingState()
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
