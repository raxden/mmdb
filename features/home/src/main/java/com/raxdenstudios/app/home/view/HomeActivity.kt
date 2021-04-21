package com.raxdenstudios.app.home.view

import android.content.Context
import android.os.Bundle
import com.raxdenstudios.app.base.BaseActivity
import com.raxdenstudios.app.error.ErrorManager
import com.raxdenstudios.app.home.HomeNavigator
import com.raxdenstudios.app.home.databinding.HomeActivityBinding
import com.raxdenstudios.app.home.view.adapter.HomeModuleListAdapter
import com.raxdenstudios.app.home.view.model.CarouselMediaListModel
import com.raxdenstudios.app.home.view.model.HomeModel
import com.raxdenstudios.app.home.view.model.HomeModuleModel
import com.raxdenstudios.app.home.view.model.HomeUIState
import com.raxdenstudios.app.home.view.viewmodel.HomeViewModel
import com.raxdenstudios.app.media.view.model.MediaListItemModel
import com.raxdenstudios.commons.ext.*
import com.raxdenstudios.commons.util.SDK
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

internal class HomeActivity : BaseActivity() {

  companion object {
    fun createIntent(context: Context) = context.intentFor<HomeActivity>()
  }

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
    adapter.onSigInClickListener = { doLoginAndRefreshDataIfSuccess() }
    adapter.onMediaClickListener =
      { moduleModel, carouselMediaListModel, mediaListItemModel ->
        mediaSelected(model, moduleModel, carouselMediaListModel, mediaListItemModel)
      }
    adapter.onAddMediaToWatchListClickListener =
      { moduleModel, carouselMediaListModel, mediaListItemModel ->
        addMediaToWatchList(model, moduleModel, carouselMediaListModel, mediaListItemModel)
      }
    adapter.onRemoveMediaFromWatchListClickListener =
      { moduleModel, carouselMediaListModel, mediaListItemModel ->
        removeMediaFromWatchList(model, moduleModel, carouselMediaListModel, mediaListItemModel)
      }
    adapter.onCarouselMediasModel = { moduleModel, _ ->
      navigator.medias(moduleModel.mediaFilterModel) { viewModel.refreshData() }
    }
  }

  private fun doLoginAndRefreshDataIfSuccess() {
    navigator.login { viewModel.refreshData() }
  }

  private fun addMediaToWatchList(
    model: HomeModel,
    moduleModel: HomeModuleModel.CarouselMedias,
    carouselMediaListModel: CarouselMediaListModel,
    mediaListItemModel: MediaListItemModel
  ) {
    if (model.logged)
      viewModel.addMediaToWatchList(
        model,
        moduleModel,
        carouselMediaListModel,
        mediaListItemModel
      )
    else navigator.login {
      viewModel.addMediaToWatchList(
        model.copy(logged = true),
        moduleModel,
        carouselMediaListModel,
        mediaListItemModel
      )
    }
  }

  private fun removeMediaFromWatchList(
    model: HomeModel,
    moduleModel: HomeModuleModel.CarouselMedias,
    carouselMediaListModel: CarouselMediaListModel,
    mediaListItemModel: MediaListItemModel
  ) {
    viewModel.removeMediaFromWatchList(
      model,
      moduleModel,
      carouselMediaListModel,
      mediaListItemModel
    )
  }

  private fun mediaSelected(
    model: HomeModel,
    moduleModel: HomeModuleModel.CarouselMedias,
    carouselMediaListModel: CarouselMediaListModel,
    mediaListItemModel: MediaListItemModel
  ) {
    viewModel.mediaSelected(
      model,
      moduleModel,
      carouselMediaListModel,
      mediaListItemModel
    )
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
