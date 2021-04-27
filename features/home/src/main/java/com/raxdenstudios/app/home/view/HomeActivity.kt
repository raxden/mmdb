package com.raxdenstudios.app.home.view

import android.os.Bundle
import com.raxdenstudios.app.base.BaseActivity
import com.raxdenstudios.app.error.ErrorManager
import com.raxdenstudios.app.home.HomeNavigator
import com.raxdenstudios.app.home.databinding.HomeActivityBinding
import com.raxdenstudios.app.home.view.adapter.HomeModuleListAdapter
import com.raxdenstudios.app.home.view.model.HomeModel
import com.raxdenstudios.app.home.view.model.HomeUIState
import com.raxdenstudios.app.home.view.viewmodel.HomeViewModel
import com.raxdenstudios.commons.ext.*
import com.raxdenstudios.commons.util.SDK
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

internal class HomeActivity : BaseActivity() {

  private val binding: HomeActivityBinding by viewBinding()
  private val viewModel: HomeViewModel by viewModel()
  private val navigator: HomeNavigator by inject { parametersOf(this) }
  private val errorManager: ErrorManager by inject { parametersOf(this) }

  private val adapter: HomeModuleListAdapter by lazy {
    HomeModuleListAdapter().apply {
      setHasStableIds(true)
    }
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding.setUp()

    observe(viewModel.state) { state -> binding.handleState(state) }

    lifecycle.addObserver(navigator)
  }

  private fun HomeActivityBinding.handleState(state: HomeUIState) = when (state) {
    is HomeUIState.Content -> handleContentState(state.model)
    is HomeUIState.Error -> handleErrorState(state.throwable)
    HomeUIState.Loading -> handleLoadingState()
    HomeUIState.EmptyContent -> handleEmptyState()
  }

  private fun HomeActivityBinding.handleEmptyState() {
    swipeRefreshLayout.isRefreshing = false
  }

  private fun HomeActivityBinding.handleContentState(model: HomeModel) {
    swipeRefreshLayout.isRefreshing = false
    adapter.submitList(model.modules)
    adapter.onCarouselSigInClickListener = { doLoginAndRefreshDataIfSuccess() }
    adapter.onCarouselMediaClickListener = { carouselMedias, mediaListItemModel ->
      viewModel.mediaSelected(model, carouselMedias, mediaListItemModel)
    }
    adapter.onCarouselAddToWatchListClickListener = { _, mediaListItemModel ->
      if (model.logged) viewModel.addMediaToWatchList(model, mediaListItemModel)
      else navigator.launchLogin {
        viewModel.addMediaToWatchList(model.copy(logged = true), mediaListItemModel)
      }
    }
    adapter.onCarouselRemoveFromWatchListClickListener = { _, mediaListItemModel ->
      viewModel.removeMediaFromWatchList(model, mediaListItemModel)
    }
    adapter.onCarouselSeeAllClickListener = { carouselMedias ->
      navigator.launchMediaList(carouselMedias) { viewModel.refreshData() }
    }
    adapter.onCarouselFilterChanged = { carouselMedias, mediaType ->
      viewModel.filterChanged(model, carouselMedias, mediaType)
    }
  }

  private fun doLoginAndRefreshDataIfSuccess() {
    navigator.launchLogin { viewModel.refreshData() }
  }

  private fun HomeActivityBinding.handleErrorState(throwable: Throwable) {
    swipeRefreshLayout.isRefreshing = false
    errorManager.handleError(throwable)
  }

  private fun HomeActivityBinding.handleLoadingState() {
    swipeRefreshLayout.isRefreshing = true
  }

  private fun HomeActivityBinding.setUp() {
    val statusBarHeight = SDK.getStatusBarHeight(this@HomeActivity)
    recyclerView.setPaddingTop(statusBarHeight)
    recyclerView.itemAnimator = null
    recyclerView.adapter = adapter
    swipeRefreshLayout.addProgressViewEndTarget(end = statusBarHeight)
    swipeRefreshLayout.setOnRefreshListener { viewModel.refreshData() }
  }
}
