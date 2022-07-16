package com.raxdenstudios.app.list.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.raxdenstudios.app.base.BaseActivity
import com.raxdenstudios.app.error.ErrorManager
import com.raxdenstudios.app.list.MediaListNavigator
import com.raxdenstudios.app.list.databinding.MediaListActivityBinding
import com.raxdenstudios.app.list.view.adapter.MediaListAdapter
import com.raxdenstudios.app.list.view.model.MediaListModel
import com.raxdenstudios.app.list.view.model.MediaListParams
import com.raxdenstudios.app.list.view.viewmodel.MediaListViewModel
import com.raxdenstudios.app.media.view.model.MediaListItemModel
import com.raxdenstudios.commons.ext.addOnScrolledListener
import com.raxdenstudios.commons.ext.observe
import com.raxdenstudios.commons.ext.setResultOK
import com.raxdenstudios.commons.ext.setupToolbar
import com.raxdenstudios.commons.ext.viewBinding
import com.raxdenstudios.commons.pagination.ext.toPageIndex
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MediaListActivity : BaseActivity() {

  companion object {
    fun createIntent(context: Context, params: MediaListParams) =
      Intent(context, MediaListActivity::class.java).apply {
        putExtra("params", params)
      }
  }

  @Inject
  lateinit var navigator: MediaListNavigator

  @Inject
  lateinit var errorManager: ErrorManager

  private val binding: MediaListActivityBinding by viewBinding()
  private val viewModel: MediaListViewModel by viewModels()

  private var onScrolledListener: RecyclerView.OnScrollListener? = null
  private val adapter: MediaListAdapter by lazy { MediaListAdapter() }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding.setUp()

    observe(viewModel.uiState) { state -> binding.handleState(state) }
  }

  private fun MediaListActivityBinding.handleState(state: MediaListViewModel.UIState) =
    when (state) {
      is MediaListViewModel.UIState.Content -> handleContentState(state)
      MediaListViewModel.UIState.EmptyContent -> handleEmptyState()
      is MediaListViewModel.UIState.Error -> handleErrorState(state)
      MediaListViewModel.UIState.Loading -> handleLoadingState()
    }

  private fun MediaListActivityBinding.handleContentState(state: MediaListViewModel.UIState.Content) {
    swipeRefreshLayout.isRefreshing = false

    loadMoreMoviesWhenScrollDown()
    titleValueView.text = state.model.title
    adapter.populateAdapter(state.model)
  }

  private fun MediaListAdapter.populateAdapter(model: MediaListModel) {
    submitList(model.items)
    onMovieClickListener = { TODO() }
    onAddMovieToWatchListClickListener = { item ->
      checkIfLoggedAndAddMovieToWatchList(model, item)
    }
    onRemoveMovieFromWatchListClickListener = { item ->
      checkIfLoggedAndRemoveMovieFromWatchList(model, item)
    }
  }

  private fun checkIfLoggedAndRemoveMovieFromWatchList(
    model: MediaListModel,
    item: MediaListItemModel
  ) {
    removeMovieFromWatchList(model, item)
  }

  private fun removeMovieFromWatchList(model: MediaListModel, item: MediaListItemModel) {
    this@MediaListActivity.setResultOK()
    viewModel.removeMovieFromWatchList(model, item)
  }

  private fun checkIfLoggedAndAddMovieToWatchList(model: MediaListModel, item: MediaListItemModel) {
    addMovieToWatchList(model, item)
  }

  private fun addMovieToWatchList(model: MediaListModel, item: MediaListItemModel) {
    this@MediaListActivity.setResultOK()
    viewModel.addMovieToWatchList(model, item)
  }

  private fun MediaListActivityBinding.loadMoreMoviesWhenScrollDown() {
    onScrolledListener?.run { recyclerView.removeOnScrollListener(this) }
    onScrolledListener = recyclerView.addOnScrolledListener { _, _, _ ->
      val gridLayoutManager = recyclerView.layoutManager as GridLayoutManager
      viewModel.loadMoreMovies(gridLayoutManager.toPageIndex())
    }
  }

  private fun MediaListActivityBinding.handleEmptyState() {
    swipeRefreshLayout.isRefreshing = false
  }

  private fun MediaListActivityBinding.handleErrorState(state: MediaListViewModel.UIState.Error) {
    swipeRefreshLayout.isRefreshing = false
    errorManager.handleError(state.throwable)
  }

  private fun MediaListActivityBinding.handleLoadingState() {
    swipeRefreshLayout.isRefreshing = true
  }

  private fun MediaListActivityBinding.setUp() {
    setupToolbar(toolbarView)
    recyclerView.adapter = adapter
    swipeRefreshLayout.setOnRefreshListener { viewModel.refreshMovies() }
  }
}
