package com.raxdenstudios.app.home.view.component

import android.content.Context
import android.util.AttributeSet
import android.widget.FrameLayout
import com.raxdenstudios.app.home.R
import com.raxdenstudios.app.home.databinding.HomeCarouselMediasModuleViewBinding
import com.raxdenstudios.app.home.view.adapter.CarouselMediaListAdapter
import com.raxdenstudios.app.home.view.model.HomeModuleModel
import com.raxdenstudios.app.media.domain.model.MediaType
import com.raxdenstudios.app.media.view.model.MediaListItemModel
import com.raxdenstudios.commons.ext.doItGone
import com.raxdenstudios.commons.ext.doItVisible
import com.raxdenstudios.commons.ext.inflateView
import com.raxdenstudios.commons.ext.setSafeOnClickListener
import com.raxdenstudios.commons.ext.viewBinding
import com.raxdenstudios.commons.ext.visibleGone

internal class HomeCarouselMediasModuleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0,
) : FrameLayout(context, attrs, defStyleAttr, defStyleRes) {

    private val binding: HomeCarouselMediasModuleViewBinding by viewBinding()

    private val adapter: CarouselMediaListAdapter by lazy { CarouselMediaListAdapter() }

    var onAddToWatchListClickListener: (HomeModuleModel.CarouselMedias, MediaListItemModel) -> Unit =
        { _, _ -> }
    var onRemoveFromWatchListClickListener: (HomeModuleModel.CarouselMedias, MediaListItemModel) -> Unit =
        { _, _ -> }
    var onMediaClickListener: (HomeModuleModel.CarouselMedias, MediaListItemModel) -> Unit =
        { _, _ -> }
    var onSeeAllClickListener: (HomeModuleModel.CarouselMedias) -> Unit = {}
    var onMediaTypeFilterChanged: (HomeModuleModel.CarouselMedias, MediaType) -> Unit = { _, _ -> }

    init {
        if (isInEditMode) {
            inflateView(R.layout.home_carousel_medias_module_view, true)
        } else {
            binding.recyclerView.adapter = adapter
        }
    }

    fun setModel(model: HomeModuleModel.CarouselMedias) {
        binding.populate(model)
    }

    private fun HomeCarouselMediasModuleViewBinding.populate(model: HomeModuleModel.CarouselMedias) {
        setTitle(model)
        adapter.populate(model)
        seeAllButton(model)
        filterSelector(model)

        val hasMedias = model.hasMedias()
        val modelIsWatchList = model is HomeModuleModel.CarouselMedias.WatchList
        when {
            hasMedias -> {
                recyclerView.doItVisible()
                emptyWatchListView.root.doItGone()
            }
            modelIsWatchList -> {
                recyclerView.doItGone()
                emptyWatchListView.root.doItVisible()
            }
        }
    }

    private fun HomeCarouselMediasModuleViewBinding.setTitle(model: HomeModuleModel.CarouselMedias) {
        homeCarouselMediasTitleModuleView.carouselTitleView.text = model.label
    }

    private fun CarouselMediaListAdapter.populate(model: HomeModuleModel.CarouselMedias) {
        submitList(model.medias)
        onClickListener = { item -> onMediaClickListener(model, item) }
        onAddToWatchListClickListener = { item -> onAddToWatchListClickListener(model, item) }
        onRemoveFromWatchListClickListener = { item -> onRemoveFromWatchListClickListener(model, item) }
    }

    private fun HomeCarouselMediasModuleViewBinding.seeAllButton(
        model: HomeModuleModel.CarouselMedias,
    ) {
        homeCarouselMediasTitleModuleView.carouselSeeAllArrowViewGroup.apply {
            visibleGone(model.hasMedias())
            setSafeOnClickListener { onSeeAllClickListener(model) }
        }
    }

    private fun HomeCarouselMediasModuleViewBinding.filterSelector(
        model: HomeModuleModel.CarouselMedias,
    ) {
        mediaTypeChipGroup.visibleGone(model.hasMediaTypeFilter)
        moviesChip.isChecked = model.mediaType == MediaType.MOVIE
        tvSeriesChip.isChecked = model.mediaType == MediaType.TV_SHOW
        mediaTypeChipGroup.setOnCheckedChangeListener { _, checkedId ->
            val mediaType = when (checkedId) {
                R.id.movies_chip -> MediaType.MOVIE
                R.id.tv_series_chip -> MediaType.TV_SHOW
                else -> throw IllegalStateException("Invalid mediaType option selected")
            }
            onMediaTypeFilterChanged(model, mediaType)
        }
    }
}
