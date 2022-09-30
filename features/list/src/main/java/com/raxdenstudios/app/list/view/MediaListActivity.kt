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
import com.raxdenstudios.app.list.view.viewmodel.MediaListContract
import com.raxdenstudios.app.list.view.viewmodel.MediaListViewModel
import com.raxdenstudios.app.media.view.model.MediaListItemModel
import com.raxdenstudios.commons.ext.addOnScrolledListener
import com.raxdenstudios.commons.ext.launchAndCollect
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

        launchAndCollect(viewModel.uiState) { state -> binding.handleState(state) }
    }

    private fun MediaListActivityBinding.handleState(state: MediaListContract.UIState) {
        swipeRefreshLayout.isRefreshing = state.isLoading
        state.error?.let { error -> errorManager.handleError(error) }

        loadMoreMoviesWhenScrollDown()
        titleValueView.text = state.model.title
        adapter.populate(state.model)
    }

    private fun MediaListAdapter.populate(model: MediaListModel) {
        submitList(model.items)
        onMovieClickListener = { TODO() }
        onWatchListClickListener = { item -> onWatchButtonClicked(item) }
    }

    private fun onWatchButtonClicked(item: MediaListItemModel) {
        this@MediaListActivity.setResultOK()
        viewModel.setUserEvent(MediaListContract.UserEvent.WatchButtonClicked(item))
    }

    private fun MediaListActivityBinding.loadMoreMoviesWhenScrollDown() {
        onScrolledListener?.run { recyclerView.removeOnScrollListener(this) }
        onScrolledListener = recyclerView.addOnScrolledListener { _, _, _ ->
            val gridLayoutManager = recyclerView.layoutManager as GridLayoutManager
            viewModel.setUserEvent(MediaListContract.UserEvent.LoadMore(gridLayoutManager.toPageIndex()))
        }
    }

    private fun MediaListActivityBinding.setUp() {
        setupToolbar(toolbarView)
        recyclerView.adapter = adapter
        swipeRefreshLayout.setOnRefreshListener { viewModel.setUserEvent(MediaListContract.UserEvent.Refresh) }
    }
}
