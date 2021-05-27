package com.raxdenstudios.app.list.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.raxdenstudios.app.base.BaseActivity
import com.raxdenstudios.app.error.ErrorManager
import com.raxdenstudios.app.list.MediaListNavigator
import com.raxdenstudios.app.list.R
import com.raxdenstudios.app.list.databinding.MediaListActivityBinding
import com.raxdenstudios.app.list.view.adapter.MediaListAdapter
import com.raxdenstudios.app.list.view.model.MediaListModel
import com.raxdenstudios.app.list.view.model.MediaListParams
import com.raxdenstudios.app.list.view.model.MediaListUIState
import com.raxdenstudios.app.list.view.viewmodel.MediaListViewModel
import com.raxdenstudios.app.media.domain.model.MediaType
import com.raxdenstudios.app.media.view.model.MediaListItemModel
import com.raxdenstudios.commons.ext.*
import com.raxdenstudios.commons.pagination.ext.toPageIndex
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MediaListActivity : BaseActivity() {

  companion object {
    fun createIntent(context: Context, params: MediaListParams) =
      Intent(context, MediaListActivity::class.java).apply {
        putExtra("params", params)
      }
  }

  private val binding: MediaListActivityBinding by viewBinding()
  private val viewModel: MediaListViewModel by viewModel()
  private val navigator: MediaListNavigator by inject { parametersOf(this) }
  private val errorManager: ErrorManager by inject { parametersOf(this) }
  private val params: MediaListParams by argument()

  private var onScrolledListener: RecyclerView.OnScrollListener? = null
  private val adapter: MediaListAdapter by lazy { MediaListAdapter() }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding.setUp()

    viewModel.loadMedias(params)

    observe(viewModel.state) { state -> binding.handleState(state) }

    lifecycle.addObserver(navigator)
  }

  private fun MediaListActivityBinding.handleState(state: MediaListUIState) = when (state) {
    is MediaListUIState.Content -> handleContentState(state)
    MediaListUIState.EmptyContent -> handleEmptyState()
    is MediaListUIState.Error -> handleErrorState(state)
    MediaListUIState.Loading -> handleLoadingState()
  }

  private fun MediaListActivityBinding.handleContentState(state: MediaListUIState.Content) {
    swipeRefreshLayout.isRefreshing = false

    loadMoreMoviesWhenScrollDown()
    adapter.populateAdapter(state.model)
  }

  private fun MediaListAdapter.populateAdapter(model: MediaListModel) {
    submitList(model.media)
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
      viewModel.loadMoreMovies(gridLayoutManager.toPageIndex(), params)
    }
  }

  private fun MediaListActivityBinding.handleEmptyState() {
    swipeRefreshLayout.isRefreshing = false
  }

  private fun MediaListActivityBinding.handleErrorState(state: MediaListUIState.Error) {
    swipeRefreshLayout.isRefreshing = false
    errorManager.handleError(state.throwable)
  }

  private fun MediaListActivityBinding.handleLoadingState() {
    swipeRefreshLayout.isRefreshing = true
  }

  private fun MediaListActivityBinding.setUp() {
    setupToolbar(toolbarView)
    val titleResourceId = when (params) {
      MediaListParams.NowPlaying -> R.string.home_carousel_now_playing_movies
      is MediaListParams.Popular -> when (params.mediaType) {
        MediaType.MOVIE -> R.string.list_popular_movies
        MediaType.TV_SHOW -> R.string.list_popular_tv_shows
      }
      is MediaListParams.TopRated -> when (params.mediaType) {
        MediaType.MOVIE -> R.string.list_top_rated_movies
        MediaType.TV_SHOW -> R.string.list_top_rated_tv_shows
      }
      MediaListParams.Upcoming -> R.string.home_carousel_upcoming
      is MediaListParams.WatchList -> R.string.home_carousel_watch_list
    }
    titleValueView.text = getString(titleResourceId)
    recyclerView.adapter = adapter
    swipeRefreshLayout.setOnRefreshListener { viewModel.refreshMovies(params) }
  }
}
