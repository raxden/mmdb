package com.raxdenstudios.app.list.view

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.raxdenstudios.app.base.BaseActivity
import com.raxdenstudios.app.error.ErrorManager
import com.raxdenstudios.app.list.MovieListNavigator
import com.raxdenstudios.app.list.databinding.MovieListActivityBinding
import com.raxdenstudios.app.list.view.adapter.MovieListAdapter
import com.raxdenstudios.app.list.view.model.MovieListModel
import com.raxdenstudios.app.list.view.model.MovieListParams
import com.raxdenstudios.app.list.view.model.MovieListUIState
import com.raxdenstudios.app.list.view.viewmodel.MovieListViewModel
import com.raxdenstudios.app.movie.data.remote.exception.UserNotLoggedException
import com.raxdenstudios.commons.ext.*
import com.raxdenstudios.commons.pagination.ext.toPageIndex
import com.raxdenstudios.commons.util.SDK
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class MovieListActivity : BaseActivity() {

  companion object {
    fun createIntent(context: Context, params: MovieListParams) =
      Intent(context, MovieListActivity::class.java).apply {
        putExtra("params", params)
      }
  }

  private val binding: MovieListActivityBinding by viewBinding()
  private val viewModel: MovieListViewModel by viewModel()
  private val navigator: MovieListNavigator by inject { parametersOf(this) }
  private val errorManager: ErrorManager by inject { parametersOf(this) }
  private val params: MovieListParams by argument()

  private var onScrolledListener: RecyclerView.OnScrollListener? = null
  private val adapter: MovieListAdapter by lazy { MovieListAdapter() }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)

    binding.setUp()

    observe(viewModel.state) { state -> binding.handleState(state) }

    viewModel.loadMovies(params)

    lifecycle.addObserver(navigator)
  }

  private fun MovieListActivityBinding.handleState(state: MovieListUIState) = when (state) {
    is MovieListUIState.Content -> handleContentState(state)
    MovieListUIState.EmptyContent -> handleEmptyState()
    is MovieListUIState.Error -> handleErrorState(state)
    MovieListUIState.Loading -> handleLoadingState()
  }

  private fun MovieListActivityBinding.handleContentState(state: MovieListUIState.Content) {
    swipeRefreshLayout.isRefreshing = false

    loadMoreMoviesWhenScrollDown(state.model)
    adapter.populateAdapter(state.model)
  }

  private fun MovieListAdapter.populateAdapter(model: MovieListModel) {
    submitList(model.movies)
    onMovieClickListener = { TODO() }
    onAddMovieToWatchListClickListener = { item -> viewModel.addMovieToWatchList(model, item) }
    onRemoveMovieFromWatchListClickListener =
      { item -> viewModel.removeMovieFromWatchList(model, item) }
  }

  private fun MovieListActivityBinding.loadMoreMoviesWhenScrollDown(model: MovieListModel) {
    onScrolledListener?.run { recyclerView.removeOnScrollListener(this) }
    onScrolledListener = recyclerView.addOnScrolledListener { _, _, _ ->
      val gridLayoutManager = recyclerView.layoutManager as GridLayoutManager
      viewModel.loadMoreMovies(gridLayoutManager.toPageIndex(), model)
    }
  }

  private fun MovieListActivityBinding.handleEmptyState() {
    swipeRefreshLayout.isRefreshing = false
  }

  private fun MovieListActivityBinding.handleErrorState(state: MovieListUIState.Error) {
    swipeRefreshLayout.isRefreshing = false
    when (state.throwable) {
      is UserNotLoggedException -> navigator.login()
      else -> errorManager.handleError(state.throwable)
    }
  }

  private fun MovieListActivityBinding.handleLoadingState() {
    swipeRefreshLayout.isRefreshing = true
  }

  private fun MovieListActivityBinding.setUp() {
    val statusBarHeight = SDK.getStatusBarHeight(this@MovieListActivity)
    recyclerView.setPaddingTop(statusBarHeight)
    recyclerView.adapter = adapter
    swipeRefreshLayout.addProgressViewEndTarget(end = statusBarHeight)
    swipeRefreshLayout.setOnRefreshListener { viewModel.refreshMovies(params) }
  }
}