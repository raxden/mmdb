package com.raxdenstudios.app.home.view

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.raxdenstudios.app.error.ErrorManager
import com.raxdenstudios.app.home.HomeMediaListNavigator
import com.raxdenstudios.app.home.R
import com.raxdenstudios.app.home.databinding.HomeMediaListFragmentBinding
import com.raxdenstudios.app.home.view.adapter.HomeModuleListAdapter
import com.raxdenstudios.app.home.view.model.HomeMediaListModel
import com.raxdenstudios.app.home.view.viewmodel.HomeMediaListViewModel
import com.raxdenstudios.commons.ext.addProgressViewEndTarget
import com.raxdenstudios.commons.ext.launchAndCollect
import com.raxdenstudios.commons.ext.setPaddingTop
import com.raxdenstudios.commons.ext.viewBinding
import com.raxdenstudios.commons.util.SDK
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
internal class HomeMediaListFragment : Fragment(R.layout.home_media_list_fragment) {

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

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.setUp()

        launchAndCollect(viewModel.state) { state -> binding.handleState(state) }
    }

    private fun HomeMediaListFragmentBinding.handleState(state: HomeMediaListViewModel.UIState) {
        swipeRefreshLayout.isRefreshing = state.loading
        state.error?.let { error -> errorManager.handleError(error) }
        state.events.firstOrNull()?.let { event -> handleEvent(event) }
        adapter.populate(state.model)
    }

    private fun handleEvent(event: HomeMediaListViewModel.UIEvent) {
        when (event) {
            is HomeMediaListViewModel.UIEvent.NavigateToMediaList -> {
                navigator.launchMediaList(event.carouselMedias) { viewModel.loadData() }
                viewModel.eventConsumed(event)
            }
        }
    }

    private fun HomeModuleListAdapter.populate(model: HomeMediaListModel) {
        submitList(model.modules)
        onCarouselMediaClickListener = { carouselMedias, mediaListItemModel ->
            viewModel.mediaSelected(model, carouselMedias, mediaListItemModel)
        }
        onCarouselWatchListClickListener = { _, mediaListItemModel ->
            viewModel.watchListPressed(model, mediaListItemModel)
        }
        onCarouselSeeAllClickListener = { carouselMedias ->
            viewModel.viewAllButtonSelected(carouselMedias)
        }
        onCarouselFilterChanged = { carouselMedias, mediaType ->
            viewModel.filterChanged(model, carouselMedias, mediaType)
        }
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
